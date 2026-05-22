#!/usr/bin/env bash

# requires curl & jq
# Credit: https://github.com/PurpurMC/Purpur

# Usage:
# upstreamCommit --paper HASH --purpur HASH --leaf HASH --keila HASH
# flag: --paper HASH - (Optional) the commit hash to use for comparing commits between paper (PaperMC/Paper/compare/HASH...HEAD)
# flag: --purpur HASH - the commit hash to use for comparing commits between purpur (PurpurMC/Purpur/compare/HASH...HEAD)
# flag: --leaf HASH - the commit hash to use for comparing commits between Leaf (Winds-Studio/Leaf/compare/HASH...HEAD)
# flag: --leaves HASH - legacy alias for --leaf
# flag: --keila HASH - the commit hash to use for comparing commits in tame-gg/keila

function getCommits() {
    curl -H "Accept: application/vnd.github.v3+json" https://api.github.com/repos/"$1"/compare/"$2"..."$3" | jq -r '.commits[] | "'"$1"'@\(.sha[:8]) \(.commit.message | split("\r\n")[0] | split("\n")[0])" | sub("\\[ci( |-)skip]"; "[ci/skip]")'
}

(
set -e
PS1="$"

paperHash=$(git diff gradle.properties | awk '/^-paperCommit =/{print $NF}')
purpurHash=""
leafHash=""
keilaHash=""

# Useless params standardize
# TEMP=$(getopt --long paper:,purpur:,leaf:,leaves:,keila: -o "" -- "$@")
# eval set -- "$TEMP"
while true; do
    case "$1" in
        --paper)
            paperHash="$2"
            shift 2
            ;;
        --purpur)
            purpurHash="$2"
            shift 2
            ;;
        --leaf|--leaves)
            leafHash="$2"
            shift 2
            ;;
        --keila)
            keilaHash="$2"
            shift 2
            ;;
        *)
            break
            ;;
    esac
done

paper=""
purpur=""
leaf=""
keila=""
updated=""
logsuffix=""

# Paper updates
if [ -n "$paperHash" ]; then
    newHash=$(git diff gradle.properties | awk '/^+paperCommit =/{print $NF}')
    paper=$(getCommits "PaperMC/Paper" "$paperHash" $(echo $newHash | grep . -q && echo $newHash || echo "ver/1.21.11")) # Update this on every version update

    # Updates found
    if [ -n "$paper" ]; then
        updated="Paper"
        logsuffix="$logsuffix\n\nPaper Changes:\n$paper"
    fi
fi

# Purpur updates
if [ -n "$purpurHash" ]; then
    purpur=$(getCommits "PurpurMC/Purpur" "$purpurHash" "ver/1.21.11") # Update this on every version update

    # Updates found
    if [ -n "$purpur" ]; then
        updated="${updated:+$updated/}Purpur"
        logsuffix="$logsuffix\n\nPurpur Changes:\n$purpur"
    fi
fi

# Leaf updates
if [ -n "$leafHash" ]; then
    leaf=$(getCommits "Winds-Studio/Leaf" "$leafHash" "HEAD")

    # Updates found
    if [ -n "$leaf" ]; then
        updated="${updated:+$updated/}Leaf"
        logsuffix="$logsuffix\n\nLeaf Changes:\n$leaf"
    fi
fi

# Keila updates
if [ -n "$keilaHash" ]; then
    keila=$(getCommits "tame-gg/keila" "$keilaHash" "HEAD")

    # Updates found
    if [ -n "$keila" ]; then
        updated="${updated:+$updated/}Keila"
        logsuffix="$logsuffix\n\nKeila Changes:\n$keila"
    fi
fi

disclaimer="Upstream has released updates that appear to apply and compile correctly"
log="Updated Upstream ($updated)\n\n${disclaimer}${logsuffix}"

git add gradle.properties

echo -e "$log" | git commit -F -

) || exit 1
