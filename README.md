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
