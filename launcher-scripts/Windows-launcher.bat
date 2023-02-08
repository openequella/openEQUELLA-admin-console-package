@echo off
set OLD_JAVA_HOME="%JAVA_HOME%"
set "BAT_DIRECTORY=%~dp0"
set "JAVA_HOME=%BAT_DIRECTORY%jdk-11.0.18+10-jre"
start "Admin console launcher" /D "%BAT_DIRECTORY%\libs" "%JAVA_HOME%\bin\javaw" -DLAUNCHER_JAVA_PATH="%JAVA_HOME%\bin\java" -jar admin.jar
set JAVA_HOME="%OLD_JAVA_HOME%"
set OLD_JAVA_HOME=""
