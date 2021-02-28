#!/bin/bash

set -e

RUN_PATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $RUN_PATH

echo ----[ Copy to apps/ ]----
mkdir -p apps
find -type f | grep '.zip$' | xargs -I{} mv -v {} apps/
