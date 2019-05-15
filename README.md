# admin-console-package
Project to build a stand-alone admin console (launcher) package

## Building the configurator
```
~$ ./gradlew :configurator:jar --console=verbose
```
The jar is placed in .../build/libs/

For development, you can run the app via:

```
~$ ./gradlew :configurator:run --console=verbose
```

##Apereo Header validation
The gradle build includes a task which will check that each source file in the common and configurator projects has an Apereo 
license notice at the top of the file. This license is stored in `LICENSE` in the root of this repository.
In order to apply the headers, run:

```
-$ ./gradlew licenseFormat
```
 In order to check that the headers have been applied, run:
 
 ```
 -$ ./gradlew license
 ```
NOTE: The license check is executed as part of the standard `build` target.

## Creating packages for Linux, Windows and Mac
The gradle build includes a task which creates three packages by bundling OpenJDK, a fat Jar of this project and a system-specific launcher script.
For Linux user, extract the package and run : ./Linux-launcher.sh
For Windows user, extract the package and double click Windows-launcher.bat
For Mac user, extract the package and (to be continued...)

To run this task, run:
 ```
 -$ ./gradlew createPackages
 ```
This task is also automatically executed after running
 ```
 -$ ./gradlew build
 ```