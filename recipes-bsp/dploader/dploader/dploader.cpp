/*
* Copyright (c) 2020, The Linux Foundation. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are
* met:
*     * Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above
*       copyright notice, this list of conditions and the following
*       disclaimer in the documentation and/or other materials provided
*       with the distribution.
*     * Neither the name of The Linux Foundation nor the names of its
*       contributors may be used to endorse or promote products derived
*       from this software without specific prior written permission.

* THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
* ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
* BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
* BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
* OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
* IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
#include <unistd.h>
#include <getopt.h>
#include <stdio.h>
#include <sysexits.h>

#include <iostream>
#include <string>
#include <cstring>

#include <fs_mgr_dm_linear.h>
#include <liblp/liblp.h>

#define SUPER_DEVICE_PATH "/dev/disk/by-partlabel/super"

using namespace android;
using namespace android::fs_mgr;
using namespace std;

static void usage()
{
    fprintf(stderr,
            "Usage: dploader <command> [<option>]\n"
            "\n"
            "commands:\n"
            "    dump-metadata                  Show info about metadata in super partiiton.\n"
            "    create-logic-partitions        Create logic partitions.\n"
            "\n"
            "options:\n"
            "    -s <super device path>         Set the super device path\n"
            "                                   Default path is %s\n", SUPER_DEVICE_PATH);
}

static void do_dump_metadata(std::string& block_device)
{
    std::unique_ptr<LpMetadata> pt;
    pt = ReadCurrentMetadata(block_device);

     if (!pt) {
        cerr << "Failed to read metadata.\n";
        return;
    }

    cout << "Metadata Infomation:\n";
    cout << "    Version: " << pt->header.major_version << "." << pt->header.minor_version  << "\n";
    cout << "    Size: " << (pt->header.header_size + pt->header.tables_size) << " bytes\n";
    cout << "    Max size: " << pt->geometry.metadata_max_size << " bytes\n";
    cout << "    Slot count: " << pt->geometry.metadata_slot_count << "\n";

    cout << "Partition table:\n";
    for (const auto& partition : pt->partitions) {
        std::string name = GetPartitionName(partition);
        std::string group_name = GetPartitionGroupName(pt->groups[partition.group_index]);
        cout << "    Name: " << name << "\n";
        cout << "    Group: " << group_name << "\n";
    }

    cout << "Block device table:\n";
    for (const auto& block_device : pt->block_devices) {
        std::string partition_name = GetBlockDevicePartitionName(block_device);
        cout << "      Partition name: " << partition_name << "\n";
        cout << "      First sector: " << block_device.first_logical_sector << "\n";
        cout << "      Size: " << block_device.size << " bytes\n";
    }
}

static bool do_create_logic_partitions(std::string& block_device)
{
   return android::fs_mgr::CreateLogicalPartitions(block_device);
}

int main(int argc, char** argv)
{
    if (argc < 2) {
        usage();
        return 1;
    }

    int rv;
    std::string super_device = SUPER_DEVICE_PATH;
    while ((rv = getopt(argc, argv, "s:h")) != -1) {
        switch (rv) {
        case 's':
            super_device = optarg;
            cout << "super device path is " << super_device << "\n";
            break;
        case 'h':
            usage();
            return 1;
        default:
            break;
        }
    }

    argc -= optind;
    argv += optind;

    if (argc != 1) {
        usage();
        return 1;
    }

    if (strcmp(*argv, "dump-metadata") == 0) {
        do_dump_metadata(super_device);
    } else if (strcmp(*argv, "create-logic-partitions") == 0) {
        do_create_logic_partitions(super_device);
    } else {
        usage();
        return 1;
    }

    return 0;
}
