@echo off
set OLD_JAVA_HOME=%JAVA_HOME%
set JAVA_HOME=%cd%\jdk8u212-b03-jre
start "Admin console launcher" /D "%cd%\libs" "%JAVA_HOME%\bin\javaw" -DLAUNCHER_JAVA_PATH="%JAVA_HOME%\bin\java" -jar admin.jar
set JAVA_HOME=%OLD_JAVA_HOME%
set OLD_JAVA_HOME=