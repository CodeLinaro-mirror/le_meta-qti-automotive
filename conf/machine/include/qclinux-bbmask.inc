# mask QCLINUX origin kernel recipes, use automotive's in qclinux1.0
BBMASK:sa8797 += "meta-qti-bsp/recipes-connectivity/bluez5/"
BBMASK:sa8797 += "meta-qti-bsp/recipes-kernel"
BBMASK:sa8797 += "meta-qti-bsp/recipes-multimedia/fastrpc/fastrpc_git.bbappend"
BBMASK:sa8797 += "meta-qti-bsp/recipes-bsp/ss-restart"
BBMASK:sa8797 += "meta-qcom/recipes-support/fastrpc/fastrpc_git.bb"
BBMASK:sa8797 += "meta-qcom"
BBMASK:sa8797 += "meta-qti-core"
