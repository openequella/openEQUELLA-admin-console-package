#!/bin/bash
OLD_JAVA_HOME=$JAVA_HOME
SCRIPT_DIRECTORY=`readlink -f "$(dirname "$0")"`
export JAVA_HOME=$SCRIPT_DIRECTORY/jdk-21.0.1+12-jre
"$JAVA_HOME/bin/java" -DLAUNCHER_JAVA_PATH="$JAVA_HOME/bin/java" -jar "$SCRIPT_DIRECTORY/libs/admin.jar"
export JAVA_HOME=$OLD_JAVA_HOME
