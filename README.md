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
