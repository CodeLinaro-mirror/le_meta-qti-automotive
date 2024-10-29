do_install:prepend() {
  sed -i 's/\[^\%](%%)/[^\%]?(%%)/g' ${WORKDIR}/systemctl
}
