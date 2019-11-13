#!/bin/bash
OLD_JAVA_HOME=$JAVA_HOME
SCRIPT_DIRECTORY=`readlink -f "$(dirname "$0")"`
export JAVA_HOME=$SCRIPT_DIRECTORY/jdk8u212-b03-jre
"$JAVA_HOME/bin/java" -DLAUNCHER_JAVA_PATH="$JAVA_HOME/bin/java" -jar "$SCRIPT_DIRECTORY/libs/admin.jar"
export JAVA_HOME=$OLD_JAVA_HOME