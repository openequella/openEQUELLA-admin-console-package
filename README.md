# admin-console-package
Project to build a stand-alone admin console (launcher) package

## Building the configurator
```
~$ ./gradlew jar --console=verbose
```
The jar is placed in .../build/libs/

For development, you can run the app via:

```
~$ ./gradlew run --console=verbose
```

## Apereo Header validation
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
The gradle build includes a task which creates three packages by bundling OpenJDK, all dependencies of this project and a system-specific launcher script.

* For Linux user, extract the package and run: ./Linux-launcher.sh
* For Windows user, extract the package and double click Windows-launcher.bat
* For Mac user, extract the package and run: ./Mac-launcher.sh

These packages will be produced as part of the standard build task.
 
 ```
  -$ ./gradlew build
 ```

## Adding Root CA Certificates
If openEQUELLA is configured to use SSL with a certificate that is not recognized by Java, (such as with a self-signed certificate, an internal CA, smaller/lesser known third party CAs)
then the admin-console-package will fail to log in to the institution because of this error displayed in the terminal window: 

```
Caused by: javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: 
PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: 
unable to find valid certification path to requested target
```

This is because the Java Keystore used by the admin console package does not recognize the Root CA certificate used to sign openEQUELLA's SSL certificate.

The admin-console-package uses its own copy of the JRE, in the `jdk8u242-b08-jre` folder. 
The keystore we need to update is within this folder, at the path `jdk8u242-b08-jre/lib/security/cacerts`.

The bundled JRE comes with a command line tool which you can use for updating these keystores, called `keytool`. 
This should work in Mac, Linux and Windows. It is stored in `jdk8u242-b08-jre/bin`.

**NOTE:**

If Java is installed on your system it will have its own version of `keystore`. 
You should use the one within the admin-console-package's bundled JRE rather than your system Java version, to ensure compatibility.

You will need a copy of the Root CA certificate used to sign your SSL certificate saved as a .pem file for the following command's `-file` argument.

`-alias` can be whatever you wish to call this key store entry.

`-storepass` must be `changeit` - unless you have specifically changed this password first, 
in which case you should use whatever it was set to.

```
keytool -import -trustcacerts -keystore path/to/adminconsolepackage/jdk8u242-b08-jre/lib/security/cacerts -storepass changeit -alias giveYourCertANameHere -file path/to/rootCA.pem
```

The command will display the certificate and prompt the user to `Trust this certificate? [no]:`. Type `yes` and hit Enter.
If successful, the response will be:

    Certificate was added to keystore.

Now close and reopen the admin-console-package and attempt to log into your openEQUELLA's admin console. The error should be gone and login should be successful.
