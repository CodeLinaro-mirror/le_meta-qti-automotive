# Both hwrng and jitter entropy source are supported. Jitter entropy init
# is slow and need consume four cpu by default, and it works much slower
# than hwrng. Hwrng is enough for entropy functionality, so disable jitter.
PACKAGECONFIG:remove = "libjitterentropy"
