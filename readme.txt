Wookie
======

Wookie is an open-source Widget Engine based on the W3C Widgets Specification.

For more information see http://incubator.apache.org/projects/wookie.html

======================
IMPORTANT LEGAL NOTICE
======================

Apache Wookie (Incubating) depends on several libraries that are licenced under open source licences that are incompatible with
the Apache License. When you first build the project these will be downloaded automatically by the build system.

Configuration
=============

There are some configuration options in build.properties, these are documented within the file itself.

Building
========

You will need to download and install Apache Ant in order to build from source. See http://ant.apace.org

Once installed run the following commands:

cd PROJECT_HOME_DIRECTORY
ant

The first time you run this command all dependencies will be downloaded so you must be online and it will take some time. 
Subsequent builds will be much faster.

Running
=======

The following will compile any changed java files and run the webserver in demo mode, complete with an embedded database. 
The database will be automatically updated at construction - do not use this approach in production.

cd PROJECT_HOME_DIRECTORY
ant run
http://localhost:8080/wookie

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and password 'java'.

If you want to start with a completely clean build then use:

ant clean-build run

If you want to clean the database use:

ant clean-db

Deploying using Tomcat and MySQL
================================

The following will compile and run the server in Tomcat with a MySQL database. The database tables will be set up automatically, however you
must have previously created a database called "widgetdb" with username "java" and password "java".

cd PROJECT_HOME_DIRECTORY
edit the "build.properties" file, uncommenting "project.using.mysql=true" and setting "servletEngine.webapp.dir=" to the location of your
tomcat webapps directory.
ant clean-build deploy-webapp

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and password 'java'.

If you want to start with a completely clean build then use:

ant clean-build deploy-webapp

If you want to clean the database use:

ant clean-db

Debugging
=========

To run the server in debug mode on a specific port use the wookie.jvmargs property, as follows:

ant -Dwookie.jvmargs="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n" run

