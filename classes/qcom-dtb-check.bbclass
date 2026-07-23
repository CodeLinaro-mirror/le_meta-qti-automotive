#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

# qcom-dtb-check.bbclass
#
# Standalone GVM firmware memory contiguity checker.
# Inheriting recipes set FW_MEM_CHECK_DTB_DIR to the directory containing
# the final merged DTBs to check, then wire up do_check_fw_mem as a task.
#
# Usage in a recipe:
#   inherit qcom-dtb-check
#   FW_MEM_CHECK_DTB_DIR = "${SOME_OUTPUT_DIR}"
#   addtask check_fw_mem after do_make_dtb before do_deploy

DEPENDS += "dtc-native"

FW_MEM_CHECK_DTB_DIR ?= ""

python do_check_fw_mem() {
    import subprocess, glob, os

    # BitBake python tasks do not inherit the native sysroot PATH that shell
    # tasks get automatically — add it explicitly so fdtget can be found.
    native_bindir = d.getVar('STAGING_BINDIR_NATIVE')
    os.environ['PATH'] = native_bindir + ':' + os.environ.get('PATH', '')

    dtb_dir = d.getVar('FW_MEM_CHECK_DTB_DIR')
    if not dtb_dir:
        bb.warn("FW_MEM_CHECK_DTB_DIR is not set, skipping GVM memory region check")
        return

    dtb_files = glob.glob(os.path.join(dtb_dir, '*.dtb'))
    if not dtb_files:
        bb.warn("No DTB files found in {}, skipping GVM memory region check".format(dtb_dir))
        return

    def fdtget(*args):
        r = subprocess.run(['fdtget'] + list(args), capture_output=True, text=True)
        return r.returncode, r.stdout.strip()

    def fdtget_phandles(dtb, path, prop):
        rc, out = fdtget('-t', 'u', dtb, path, prop)
        if rc != 0:
            return []
        return [int(x) for x in out.split()]

    def find_node_by_phandle(dtb, phandle):
        rc, out = fdtget('-l', dtb, '/reserved-memory')
        if rc != 0:
            return None
        for node in out.split():
            path = '/reserved-memory/' + node
            for prop in ('phandle', 'linux,phandle'):
                rc2, val = fdtget('-t', 'u', dtb, path, prop)
                if rc2 == 0 and int(val) == phandle:
                    return path
        return None

    def find_gvm_nodes(dtb):
        nodes = []
        rc, out = fdtget('-l', dtb, '/')
        if rc != 0:
            return nodes
        search_parents = ['/'] + ['/' + n for n in out.split()]
        for parent in search_parents:
            rc2, children = fdtget('-l', dtb, parent)
            if rc2 != 0:
                continue
            for child in children.split():
                path = parent.rstrip('/') + '/' + child
                rc3, compat = fdtget('-t', 's', dtb, path, 'compatible')
                if rc3 == 0 and 'qcom,gh-secure-vm-loader' in compat:
                    nodes.append(path)
        return nodes

    def check_dtb(dtb):
        gvm_nodes = find_gvm_nodes(dtb)
        if not gvm_nodes:
            return

        for gvm_path in gvm_nodes:
            phandles = fdtget_phandles(dtb, gvm_path, 'memory-region')
            if not phandles:
                continue

            regions = []
            for ph in phandles:
                node_path = find_node_by_phandle(dtb, ph)
                if not node_path:
                    continue
                rc, props = fdtget('-p', dtb, node_path)
                if rc != 0 or 'no-map' not in props.split():
                    continue
                rc, reg = fdtget('-t', 'x', dtb, node_path, 'reg')
                if rc != 0:
                    continue
                vals = reg.split()
                if len(vals) == 4:
                    base = (int(vals[0], 16) << 32) | int(vals[1], 16)
                    size = (int(vals[2], 16) << 32) | int(vals[3], 16)
                elif len(vals) == 2:
                    base = int(vals[0], 16)
                    size = int(vals[1], 16)
                else:
                    continue
                regions.append((base, size, node_path))

            regions.sort(key=lambda r: r[0])

            for i in range(len(regions) - 1):
                b0, s0, p0 = regions[i]
                b1, s1, p1 = regions[i+1]
                if b0 + s0 >= b1:
                    bb.fatal(
                        '{}: gh-secure-vm-loader {} has contiguous no-map memory-region nodes'
                        ' that must be merged into a single DTS node:\n'
                        '  {}: base=0x{:016x} size=0x{:x}\n'
                        '  {}: base=0x{:016x} size=0x{:x}'.format(
                            dtb, gvm_path, p0, b0, s0, p1, b1, s1))

    for dtb in dtb_files:
        check_dtb(dtb)
}
