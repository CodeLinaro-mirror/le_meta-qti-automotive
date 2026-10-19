# Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
# SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native"

# Build dtb.img with all GVM segments densely packed, plus a manifest file.
#
# Layout: [pvm dtbs][gvm dtbs][gvm dtbs]...
# Segment order follows "pvm ${VMS_GVM_TYPES}".
#
# No header and no padding: dtb.img is a plain concatenation of FDTs, so the
# ABL DTB scan loop walks every dtb in the image. Segment boundaries are not
# encoded in the image itself - the manifest is the only record of which
# segment each dtb came from.
#
# Usage: build_dense_dtb_img <dtbs_dir> <output_dtb_img> <manifest_file>
build_dense_dtb_img () {
    local dtbs_dir=$1
    local out_img=$2
    local manifest=$3

    local sep="--------------------------------------------------------------------------------"
    # Write manifest for debug
    printf "%-72s %-16s %-12s %-10s %s\n" \
        "NAME" "OFFSET(hex)" "SEGMENT" "INDEX" "SIZE(bytes)" > "$manifest"
    printf "%s\n" "$sep" >> "$manifest"

    # Initialize output image
    cat /dev/null > "$out_img"

    local cur=0 seg idx dtb sz
    for seg in pvm ${VMS_GVM_TYPES}; do
        idx=0
        for dtb in $(find "${dtbs_dir}/${seg}" -name "*.dtb" 2>/dev/null | sort); do
            sz=$(wc -c < "$dtb")
            cat "$dtb" >> "$out_img"
            printf "%-72s 0x%-14X %-12s %-10d %d\n" \
                "$(basename $dtb)" "$cur" "$seg" "$idx" "$sz" >> "$manifest"
            cur=$(expr $cur + $sz); idx=$(expr $idx + 1)
        done
    done

    printf "%s\n" "$sep"                                              >> "$manifest"
    printf "%-72s %-16s %-12s %-10s %d\n" "TOTAL" "" "" "" "$cur"    >> "$manifest"

    echo "dtb.img assembled: $(wc -c < $out_img) bytes, manifest: $manifest"
}

# Extract symbol names from a named section (__fixups__ or __symbols__) of a DTB/DTBO
_extract_dt_section () {
    local file="$1"
    local section="$2"
    fdtdump "$file" 2>/dev/null | awk -v sec="$section" '
        $0 ~ sec "[[:space:]]*{" { in_sec=1; depth=1; next }
        in_sec && /{/ { depth++ }
        in_sec && /}/ { depth--; if (depth <= 0) in_sec=0; next }
        in_sec && /=/ { sub(/^[[:space:]]+/, ""); print $1 }
    '
}

# Sort a space-separated list of DTBOs by dependency order.
# Usage: sort_dtbos_by_dep <base.dtb> <dtbo1> [dtbo2 ...]
# Prints sorted paths one per line.
sort_dtbos_by_dep () {
    local base_dtb="$1"
    shift
    local dtbos="$@"

    local tmpdir
    tmpdir=$(mktemp -d)

    local resolved="$tmpdir/resolved"
    _extract_dt_section "$base_dtb" '__symbols__' > "$resolved"

    for dtbo in $dtbos; do
        local name
        name=$(basename "$dtbo")
        _extract_dt_section "$dtbo" '__fixups__'  > "$tmpdir/${name}.needs"
        _extract_dt_section "$dtbo" '__symbols__' > "$tmpdir/${name}.exports"
    done

    local remaining="$dtbos"
    local sorted=""

    while [ -n "$remaining" ]; do
        local progress=0
        local next_remaining=""

        for dtbo in $remaining; do
            local name
            name=$(basename "$dtbo")
            local all_ok=1

            while IFS= read -r sym; do
                [ -z "$sym" ] && continue
                grep -qxF "$sym" "$resolved" 2>/dev/null || { all_ok=0; break; }
            done < "$tmpdir/${name}.needs"

            if [ "$all_ok" -eq 1 ]; then
                sorted="$sorted $dtbo"
                cat "$tmpdir/${name}.exports" >> "$resolved"
                progress=1
            else
                next_remaining="$next_remaining $dtbo"
            fi
        done

        remaining="$next_remaining"

        if [ "$progress" -eq 0 ]; then
            echo "WARN: dtbo dependency cycle or unresolvable symbols, using original order for: $remaining" >&2
            sorted="$sorted $remaining"
            break
        fi
    done

    rm -rf "$tmpdir"
    for dtbo in $sorted; do
        echo "$dtbo"
    done
}

match_dtb_to_dtbo () {
    dtb=$1
    dtbo=$2

    dtbo_compatible=$(fdtget -t s $dtbo / "compatible" | sed -e 's/\"//g' -e 's/[;\,]//g')

    dtb_compatible=$(fdtget -t s $dtb / "compatible" | sed -e 's/\"//g' -e 's/[;\,]//g')

    for dtb in $dtb_compatible; do
        for dtbo in $dtbo_compatible; do
            if [ "$dtb" = "$dtbo" ]; then
                return 0
            fi
        done
    done
    return 1
}

merge_dtbos () {
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3
    matched_dtbos=""

    dtb_files=$(find $dtb_dir -name "*.dtb*")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")

    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        matched_dtbos=""
        combined_suffix=""
        for dtbo_file in $dtbo_files; do
            if match_dtb_to_dtbo $dtb_file $dtbo_file; then
                matched_dtbos="${matched_dtbos} ${dtbo_file}"
                suffix=$(basename $dtbo_file .dtbo | sed -n 's/^[^-_]*[-_]\(.*\)/\1/p' | tr '_' '-')
                [ -n "$suffix" ] && combined_suffix="${combined_suffix}-${suffix}"
            fi
        done

        base_name=$(basename $dtb_file)
        base_dtb_name=$(echo "$base_name" | sed -e 's/\.[^.]*$//')
        combined_suffix="${combined_suffix#-}"

        # Extract a suffix from the input DTB filename by:
        # 1. Removing the base name up to the first '-' or '_' character.
        # 2. Removing the trailing '_overlay' token.
        # 3. Stripping the file extension.
        # Example:
        #   input_dtb="sa8797p-ddr-32gb_overlay.dtb"
        #   Resulting suffix="ddr-32gb"

        out_dtb=${base_dtb_name}-${combined_suffix}.dtb

        if [ "$matched_dtbos" != ""  ]; then
            matched_dtbos=$(sort_dtbos_by_dep $dtb_file $matched_dtbos | tr '\n' ' ')
            # execute the command in verbose mode(-v)
            fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $matched_dtbos
            #exit in case of failure
            if [ $? -ne 0 ]; then
                exit 1
            fi
        else
            cp $dtb_file ${out_dir}/${out_dtb}
        fi
    done
}

merge_dtbos_single () {
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3

    dtb_files=$(find $dtb_dir -name "*.dtb*")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")

    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        dtbo_matched=""
        for dtbo_file in $dtbo_files; do
            if match_dtb_to_dtbo $dtb_file $dtbo_file; then
                dtbo_matched="true"
                base_dtb_name=$(basename $dtb_file | sed -e 's/\.[^.]*$//' -e 's/-overlay$//')
                suffix=$(basename $dtbo_file .dtbo | sed -n 's/^[^-_]*[-_]\(.*\)/\1/p' | tr '_' '-' | sed 's/-overlay$//')
                if [ -n "$suffix" ]; then
                    out_dtb=${base_dtb_name}-${suffix}.dtb
                else
                    out_dtb=${base_dtb_name}-overlay.dtb
                fi
                # execute the command in verbose mode(-v)
                fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $dtbo_file
                #exit in case of failure
                if [ $? -ne 0 ]; then
                    exit 1
                fi
            fi
        done
        if [ -z "$dtbo_matched" ]; then
            base_name=$(basename $dtb_file)
            base_dtb_name=$(echo "$base_name" | sed -e 's/\.[^.]*$//')
            out_dtb=${base_dtb_name}-overlay.dtb
            cp $dtb_file ${out_dir}/${out_dtb}
        fi
    done
}

# Legacy DDR overlay merge for SINGLE_GVM_SUPPORT machines (sa8775-flex,
# sa8255-ivi, sa7255-ivi). DDR DTBOs use filename prefixes "0gvm-" (PVM-only),
# "1gvm-" (single LAGVM) and no prefix (multi-GVM) to encode the GVM role.
# Output filenames are derived from the DTBO name so that the "*0gvm*" /
# "*1gvm*" glob patterns in do_make_dtb can locate them for mv/cat.
merge_ddr_dtbos_single () {
    dtb_dir=$1
    dtbo_dir=$2
    out_dir=$3

    if [ "${SINGLE_GVM_SUPPORT}" = "1" ]; then
        # prefix 0gvm: for pvm only; prefix 1gvm: for single LAGVM; no prefix: for multigvm LAGVM + LVGVM
        ddr_sizes="0gvm-128gb:0xE00 0gvm-96gb:0xD00 0gvm-64gb:0xC00 0gvm-48gb:0xB00 0gvm-36gb:0xA00 0gvm-32gb:0x900 0gvm-24gb:0x800 0gvm-18gb:0x700 0gvm-16gb:0x600 0gvm-12gb:0x500 0gvm-8gb:0x400 0gvm-4gb:0x300 0gvm-2gb:0x200 0gvm-1gb:0x100"
        ddr_sizes="$ddr_sizes 1gvm-128gb:0xE00 1gvm-96gb:0xD00 1gvm-64gb:0xC00 1gvm-48gb:0xB00 1gvm-36gb:0xA00 1gvm-32gb:0x900 1gvm-24gb:0x800 1gvm-18gb:0x700 1gvm-16gb:0x600 1gvm-12gb:0x500 1gvm-8gb:0x400 1gvm-4gb:0x300 1gvm-2gb:0x200 1gvm-1gb:0x100"
        ddr_sizes="$ddr_sizes 128gb:0xE00 96gb:0xD00 64gb:0xC00 48gb:0xB00 36gb:0xA00 32gb:0x900 24gb:0x800 18gb:0x700 16gb:0x600 12gb:0x500 8gb:0x400 4gb:0x300 2gb:0x200 1gb:0x100"
    else
        ddr_sizes="128gb:0xE00 96gb:0xD00 64gb:0xC00 48gb:0xB00 36gb:0xA00 32gb:0x900 24gb:0x800 18gb:0x700 16gb:0x600 12gb:0x500 8gb:0x400 4gb:0x300 2gb:0x200 1gb:0x100"
    fi

    dtb_files=$(find $dtb_dir -name "*.dtb")
    dtbo_files=$(find $dtbo_dir -name "*.dtbo")
    if [ -z "$dtb_files" ]; then
        echo "ERR : Base DTB files NOT found"
        exit 1
    fi

    if [ -z "$dtbo_files" ]; then
        echo "WARN: Overlay DTB files not found"
        cp $dtb_dir/* $out_dir
        return 0
    fi

    for dtb_file in $dtb_files; do
        for dtbo_file in $dtbo_files; do
            dtbo_string=$(basename $dtbo_file)
            dtbo_string=$(echo "$dtbo_string" | sed -e 's/\.[^.]*$//')
            input_dtb=$(basename "$dtb_file")
            prefix1=$(echo "$input_dtb" | sed -e 's/-.*//')

            if [ "$prefix1" = "sa8397p" ] || [ "$prefix1" = "seca" ]; then
                cp $dtb_file $out_dir
                continue
            fi

            suffix=$(echo $input_dtb | sed -n 's/^[^\(-\|_\)]*[\(-\|_\)]\(.*\)/\1/p' \
                     | sed 's/\(-\|_\|\)overlay\(-\|_\|\)//g' | sed 's/\..*//')
            if [ -z "$suffix" ]; then
                out_dtb=${dtbo_string}.dtb
            else
                out_dtb=${dtbo_string}-${suffix}.dtb
            fi

            subtype=""
            for i in $ddr_sizes; do
               ddr_size=$(echo $i | sed 's,:.*,,g')
               ddr_type=$(echo $i | sed 's,.*:,,g')
               case "$dtbo_file" in
                  *"$ddr_size"*)
                      subtype="$ddr_type"
                      break
                      ;;
               esac
            done

            fdtoverlay -i $dtb_file -o ${out_dir}/${out_dtb} -v $dtbo_file
            if [ $? -ne 0 ]; then
                exit 1
            fi
            board_id=$(fdtget -t x ${out_dir}/${out_dtb} / qcom,board-id )
            updated_bid=$(echo "$board_id" | awk -v mask_hex="$subtype" '
            BEGIN {
                mask = strtonum(mask_hex)
            }
            {
                for (i = 1; i <= NF; i++) {
                    val = strtonum("0x" $i)
                    if (i % 2 == 0) {
                        val = or(val, mask)
                    }
                    printf "0x%X ", val
                }
            }')

            fdtput -t x  ${out_dir}/${out_dtb} / qcom,board-id $updated_bid
            if [ $? -ne 0 ]; then
                exit 1
            fi
        done
    done
}

update_dtb_ddr_id () {
    dtb_dir=$1
    ddr_sizes="128gb:0xE00 96gb:0xD00 64gb:0xC00 48gb:0xB00 36gb:0xA00 32gb:0x900 24gb:0x800 18gb:0x700 16gb:0x600 12gb:0x500 8gb:0x400 4gb:0x300 2gb:0x200 1gb:0x100"

    dtb_files=$(find $dtb_dir -name "*.dtb")

    for dtb_file in $dtb_files; do
        subtype=""
        for i in $ddr_sizes; do
            ddr_size=$(echo $i | sed 's,:.*,,g')
            ddr_type=$(echo $i | sed 's,.*:,,g')
            case "$dtb_file" in
                *"$ddr_size"*)
                    subtype="$ddr_type"
                    break
                    ;;
            esac
        done

        echo dtb: $dtb_file, ddr_size: $ddr_size ddr_type: $ddr_type subtype: $subtype
        if [ -z "$subtype" ]; then
            continue;
        fi

        #get board-id from dtb files and replace with updated value
        #OR operation of board id subtype and ddr type is performed.
        board_id=$(fdtget -t x ${dtb_file} / qcom,board-id )
        updated_bid=$(echo "$board_id" | awk -v mask_hex="$subtype" '
        BEGIN {
            mask = strtonum(mask_hex)
        }
        {
            for (i = 1; i <= NF; i++) {
                val = strtonum("0x" $i)
                if (i % 2 == 0) {
                    val = or(val, mask)
                }
                printf "0x%X ", val
            }
        }')

        # execute the command in verbose mode(-v)
        fdtput -t x  ${dtb_file} / qcom,board-id $updated_bid
        #exit in case of failure
        if [ $? -ne 0 ]; then
            exit 1
        fi

        # update compatible
        # read existing compatible stringlist (space separated)
        compatible=$(fdtget -t s ${dtb_file} / compatible )
        # base SoC compatible is the last entry, e.g. "qcom,sa8797p"
        base_compatible=$(echo "$compatible" | awk '{print $NF}')
        # build ddr specific compatible, e.g. "qcom,sa8797p-64gb"
        ddr_compatible="${base_compatible}-${ddr_size}"
        # prepend ddr_compatible to the existing compatible list
        update_compatible="$ddr_compatible $compatible"

        fdtput -t s ${dtb_file} / compatible $update_compatible
        #exit in case of failure
        if [ $? -ne 0 ]; then
            exit 1
        fi
    done
}

# Match a pvm dtb to a vms DDR overlay dtbo.
# Requires both compatible (board type) and <N>gb token (DDR size) to match.
# Returns 0 on match, 1 otherwise.
match_dtb_to_vms_dtbo () {
    local dtb=$1
    local ddr_dtbo=$2

    local dtb_compat
    dtb_compat=$(fdtget -t s "$dtb" / compatible 2>/dev/null | sed -e 's/\"//g' -e 's/[;,]//g')
    [ -z "$dtb_compat" ] && return 1

    local compat found
    found=0
    for compat in $(fdtget -t s "$ddr_dtbo" / compatible 2>/dev/null | sed -e 's/\"//g' -e 's/[;,]//g'); do
        if echo "$dtb_compat" | grep -qw "$compat"; then
            found=1
            break
        fi
    done
    [ "$found" -eq 0 ] && return 1

    local dtbo_ddr_size dtb_ddr_size
    dtbo_ddr_size=$(basename "$ddr_dtbo" | grep -oE '[0-9]+gb' | head -1)
    [ -z "$dtbo_ddr_size" ] && return 1

    dtb_ddr_size=$(basename "$dtb" | grep -oE '[0-9]+gb' | head -1)
    [ -z "$dtb_ddr_size" ] && return 1

    [ "$dtb_ddr_size" = "$dtbo_ddr_size" ] && return 0 || return 1
}

# Merge pvm dtbs with vms DDR overlays into the output directory.
# Usage: merge_vms_dtbos <pvm_dir> <vms_ddr_dtbo_dir> <out_dir>
merge_vms_dtbos () {
    local pvm_dir=$1
    local ddr_dtbo_dir=$2
    local out_dir=$3

    local dtb_files=$(find "$pvm_dir" -name "*.dtb")
    local ddr_dtbo_files=$(find "$ddr_dtbo_dir" -name "*.dtbo")

    if [ -z "$dtb_files" ]; then
        echo "ERR : No pvm DTB files found in $pvm_dir"
        exit 1
    fi

    if [ -z "$ddr_dtbo_files" ]; then
        echo "WARN: No vms DDR DTBO files found in $ddr_dtbo_dir, skipping vms merge"
        return 0
    fi

    install -d "$out_dir"

    for dtb_file in $dtb_files; do
        for ddr_dtbo_file in $ddr_dtbo_files; do
            if match_dtb_to_vms_dtbo "$dtb_file" "$ddr_dtbo_file"; then
                local gvm_type
                gvm_type=$(basename "$ddr_dtbo_file" .dtbo | grep -oE "$(echo ${VMS_GVM_TYPES} | tr ' ' '|')")
                local base_name
                base_name=$(basename "$dtb_file" .dtb)
                local out_dtb="${out_dir}/${base_name}-${gvm_type}.dtb"

                fdtoverlay -i "$dtb_file" -o "$out_dtb" -v "$ddr_dtbo_file"
                if [ $? -ne 0 ]; then
                    echo "ERR: failed to apply vms DDR overlay to $(basename $dtb_file)"
                    exit 1
                fi
            fi
        done
    done
}
