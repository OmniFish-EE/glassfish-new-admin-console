# New GlassFish Admin Console

A new GlassFish Administration console based on standard modern technologies available in GlassFish Web Profile.

You can run the application by executing the following command from the directory where this file resides. Please ensure you have installed a Java SE 11+ implementation. Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed. You may first need to execute `chmod +x mvnw`.

```
./mvnw clean package && ./mvnw -pl war cargo:run
```

Once the runtime starts, you can access the project at http://localhost:8080/admingui-ng