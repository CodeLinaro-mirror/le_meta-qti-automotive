FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI += "${PATH_TO_REPO}/lv-sepolicy/.git;protocol=${PROTO};destsuffix=lv-sepolicy;usehead=1 \
"
SRCREV = "${AUTOREV}"

def copy_files_with_extensions(source_dir, target_dir, extensions):
    import shutil
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)
    for root, _, files in os.walk(source_dir):
        for file in files:
            _, ext = os.path.splitext(file)
            if ext in extensions:
                src_file = os.path.join(root, file)
                shutil.copy(src_file, target_dir)
                print(f"Copied {src_file} to {target_dir}")

def generate_if_file(te_file_path):
    base_filename, _ = os.path.splitext(te_file_path)
    if_file_path = base_filename + ".if"
    if not os.path.exists(if_file_path):
        with open(if_file_path, "w") as if_file:
            if_file.write(f"## <summary> This is a blank interface file for compiling </summary>\n")

def generate_fc_file(te_file_path):
    base_filename, _ = os.path.splitext(te_file_path)
    fc_file_path = base_filename + ".fc"
    if not os.path.exists(fc_file_path):
        with open(fc_file_path, "w") as fc_file:
            fc_file.write(f"## <summary> This is a blank file_context file for compiling </summary>\n")

# Becase refpolicy requires a module to have an .if file
def normalize_module_format(src_dir):
    for root, _, files in os.walk(src_dir):
        for filename in files:
            if filename.endswith(".te"):
                te_file_path = os.path.join(root, filename)
                generate_if_file(te_file_path)
                generate_fc_file(te_file_path)

def append_policy_file(src_path, dst_path):
    # Append our policy fragment to the end of the upstream file
    with open(src_path, 'r') as src_file, open(dst_path, 'a') as dst_file:
        dst_file.write('\n')
        if src_path.endswith(".if") or src_path.endswith(".if.in"):
            marker = "##########"
            lines = src_file.readlines()
            start_index = next((i for i, line in enumerate(lines) if line.startswith(marker)), None)
            if start_index is not None:
                dst_file.writelines(lines[start_index:])
        else:
            for line in src_file.readlines():
                if not line.startswith('#'):
                    dst_file.write(line)

def copy_policy_file(src_path, dst_path):
    # Copy the new file into the sepolicy compilation directory
    os.renames(src_path, dst_path)

def apply_qcom_policy(d, src_root, sub_dir):
    src_dir = os.path.join(d.getVar("WORKDIR"), src_root, sub_dir)
    dst_dir = os.path.join(d.getVar("S"), "policy", "modules", "qti_sepolicy")
    file_extensions = ['.te', '.if', '.fc']
    copy_files_with_extensions(src_dir, dst_dir, file_extensions)
    qpolicy_metadata_path = os.path.join(dst_dir, "metadata.xml")
    with open(qpolicy_metadata_path, 'w') as metadata:
        metadata.write(f"<summary>\n     Policy modules for qcom sepolicy\n</summary>\n")
    normalize_module_format(dst_dir)

def apply_common_policy(d, src_root, sub_dir):
    src_dir = os.path.join(d.getVar("WORKDIR"), src_root, sub_dir)
    dst_dir = os.path.join(d.getVar("S"), "policy", "modules")
    file_extensions = ['.te', '.if', '.fc']
    for root, dirs, files in os.walk(src_dir):
        for file in files:
            policy_file = os.path.relpath(os.path.join(root, file), src_dir)
            dst_path = os.path.join(dst_dir, policy_file)
            if os.path.exists(dst_path):
                append_policy_file(os.path.join(src_dir, policy_file), dst_path)
            else:
                copy_policy_file(os.path.join(src_dir, policy_file), dst_path)

do_patch:append() {
    apply_qcom_policy(d, "lv-sepolicy", "qpolicy")
    apply_common_policy(d, "lv-sepolicy", "common")
}
