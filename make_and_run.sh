#!/bin/sh

mvn clean package
cd target
unzip boundless-0.0.1-SNAPSHOT-release.zip
cd boundless-0.0.1-SNAPSHOT
./run.sh
