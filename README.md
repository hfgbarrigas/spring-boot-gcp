# README #

Simple starter to expose beans to fetch GCP metadata and Credentials needed to authenticate/authorize interaction with GCP
services.

### How do I get set up? ###

add the following dependency to your pom.xml.

    <dependency>
        <groupId>co.hold</groupId>
        <artifactId>google-cloud-client-spring-boot</artifactId>
        <version>X</version>
    </dependency>
    
### How do I use it? ###
You'll need a bootstrap.yml (or .properties) file located in your resources folder and set up a few properties.

***Metadata***

The [metadata](https://cloud.google.com/compute/docs/storing-retrieving-metadata) auto configuration is disabled by default.
Configure the following properties to use it:

    gcp:
      metadata:
        enabled: true/false
        baseUrl: 'the base url for google cloud metadata'

At the moment there are two types of metadata exposed - *region and zone* plus the *project id*. 

***Google credentials***

The [credentials](https://cloud.google.com/docs/authentication/production) auto configuration is disabled by default.
Configure the following properties to use it:

    gcp:
      credentials:
        enabled: true/false
        type: default

Type property accepts two values: file or default. Default will use gcp application default credentials, recommended usage when 
application is running inside GCP. File credentials should be used when it is intended to use specific credentials, as when running
the application locally or inside gcp but with scoped credentials. The following example shows its usage:

    gcp:
      credentials:
        enabled: true
        type: file
        file:
         name: 'absolute/path/to/file'

Note: Beans are configured with an *order of 1*.
