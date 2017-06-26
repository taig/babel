#!/bin/bash

set -e

git config --global user.name "Niklas Klein"
git config --global user.email mail@taig.io
git config --global push.default simple

sbt "project documentation" publishMicrosite