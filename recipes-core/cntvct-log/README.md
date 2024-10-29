# cntvct-log

## Overview

The cntvct-log tool provides a mechanism to accurately calculate the time of
boot events relative to power on.

The cntvct time is the time since power-on. The monotonic time printed by journalctl
is the time since a fixed point in boot, some time after power-on.

journald can not print timestamps using the cntvct time, but it can print the
monotonic timestamps.

This tool adds a systemd service that prints the cntvct timestamp, and when using
journalctl, we can view the monotonic timestamp of journald entries.

## Usage

On the target, follow the steps below to get the time of boot events relative to device power-on.

Check for the cntvct timestamp printed by the cntvct-log tool.

```
journalctl -b -o short-monotonic | grep cntvct
...
[    7.028879] localhost cntvct@local-fs[810]: 12.190890s.
```

In this example, the cntvct timestamp is 12.190890 and the monotonic timestamp is 7.028879.

So, the cntvct offset == ```12.190890 - 7.028879 = 5.162011```

Now, when printing out important boot events in journalctl, you can add the cntvct
offset to the monotonic timestamp to get the boot event's timestamp relative to
power-on.

For example, to see the timestamp relative to power-on of when systemd finishes loading the SELinux policy:

```
journalctl -b -o short-monotonic | grep SELinux
...
[    3.349643] localhost systemd[1]: Successfully loaded SELinux policy in 411.462ms.
```

Add the cntvct offset to the monotonic timestamp to get the timestamp relative to power-on.

SELinux loaded timestamp relative to power-on == ```3.349643 + 5.162011 = 8.511654```
