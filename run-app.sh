#!/usr/bin/env bash
set -a
source .env
set +a
mvn clean compile
mvn exec:java "-Dexec.mainClass=com.placement.App"
