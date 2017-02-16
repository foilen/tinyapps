#!/bin/bash

set -e

# Get the directory path of the current script
RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $RUN_PATH

rm -rf apps
mkdir apps

echo ---[ Compile and zip ]---
./gradlew clean distZip


echo ---[ Copy to apps/ ]---
find -type f | grep '.zip$' | xargs -I{} mv -v {} apps/

echo ---[ Applications compiled ]---
find apps
