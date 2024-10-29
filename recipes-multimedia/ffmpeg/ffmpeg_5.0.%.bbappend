FILESEXTRAPATHS:append := ":${THISDIR}/ffmpeg_5.0/"
SRC_URI:append = " file://0001-avcodec-amrwbdec-fix-multichannel-support-bug-in-wbd.patch \
                 "

