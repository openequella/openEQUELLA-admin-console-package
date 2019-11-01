#!/bin/bash
OLD_JAVA_HOME=$JAVA_HOME
SCRIPT_DIRECTORY=`(cd $(dirname $0); pwd)`
export JAVA_HOME=$SCRIPT_DIRECTORY/jdk8u212-b03-jre
"$JAVA_HOME/bin/java" -DLAUNCHER_JAVA_PATH="$JAVA_HOME/bin/java" -jar "$SCRIPT_DIRECTORY/libs/admin.jar"
export JAVA_HOME=$OLD_JAVA_HOME