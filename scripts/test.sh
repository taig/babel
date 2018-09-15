#!/bin/bash

# Docker entrypoint for test suite execution

set -e

sbt coverage +test documentation/makeMicrosite coverageReport

bash <(curl -s https://codecov.io/bash)

if [ "$CIRCLE_BRANCH" == "master" ]; then ./publish-microsite.sh; fi