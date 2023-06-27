#!/usr/bin/env bash

# This script generates the hash files for specified files. These can be generated as part of the
# build process to provide later validation of artefact integrity.
#
# To validate with the output files from this you simply run:
#   sha256sum -c <hashfile>

# Enable globstar and nullglob options, so as to support filename globs - e.g. artefacts*.zip
shopt -s globstar nullglob

if [ $# -lt 1 ]; then
  echo "Usage: $0 <file...>"
  echo "Where:"
  echo "  <file> is the name of the file(s) to generate a hash for."
  exit 1
fi

# Loop over arguments, each one being the name of a file to hash
for file in "$@"; do
  # double loop to support globbing
  for filename in "$file"; do
    echo "Processing: $filename"
    hash_contents=$(sha256sum "$filename")
    hash_result=$?

    if [ $hash_result -ne 0 ]; then
        echo "Failed to hash [${filename}], exiting."
        exit $hash_result
    fi

    echo $hash_contents > "${filename}.sha256"
  done
done