# Copyright (c) 2024 Qualcomm Innovation Center, Inc. All rights reserved.
# SPDX-License-Identifier: BSD-3-Clause-Clear

def get_tag(path,d):
    try:
        if os.path.exists(path + "/.repo/manifests") and os.path.exists(path + "/.repo/manifest.xml"):
            manifest_link = get_link_filename(os.readlink(path + "/.repo/manifest.xml"))
            if manifest_link != "":
                return manifest_link
            else:
                f = os.popen("cd %s/.repo/manifests; git describe --always 2>&1" % path)
                data = f.read()
                if f.close() is None:
                    rev = data.split(" ")[0]
                    if len(rev) != 0:
                        return rev.rstrip("\n")
    except:
        pass
    return time.strftime('%Y%m%d%H%M')


def get_link_filename(link_fn):
    try:
        import re
        return re.compile("manifests/(.+?-[0-9]+?-.+?\.0)\.xml",re.I).match(link_fn).group(1)
    except:
        return ""

