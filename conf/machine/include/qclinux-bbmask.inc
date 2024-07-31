# mask QCLINUX origin kernel recipes, use automotive's in qclinux1.0
BBMASK += "meta-qti-bsp/recipes-kernel/linux/linux-qcom-custom_6.6.bb"
BBMASK += "meta-qti-bsp/recipes-kernel/linux/linux-kernel-qcom-headers_6.6.bb"
BBMASK += "meta-qti-bsp/recipes-kernel/linux/linux-qcom-base_6.6.bb"
BBMASK += "meta-qti-core-prop/recipes-bsp/qmi-framework/qmi-framework_15.0.bb"
BBMASK += "meta-qti-core-internal/recipes-bsp/diag-noship/diag-noship_15.0.bb"
BBMASK += "meta-qti-core-prop/recipes-bsp/diag/diag-router_15.0.bb"
BBMASK += "meta-qti-core-prop/recipes-bsp/diag/diag_15.0.qcom.bb"
BBMASK += "meta-qcom/recipes-test/diag/diag_git.bb"
