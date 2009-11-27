Wookie
======

Wookie is an open-source Widget Engine based on the W3C Widgets Specification.

For more information see http://incubator.apache.org/projects/wookie.html

======================
IMPORTANT LEGAL NOTICE
======================

Apache Wookie (Incubating) depends on several libraries that are licensed under open source licences that are incompatible with
the Apache License. When you first build the project these will be downloaded automatically by the build system.

Configuration
=============

There are some configuration options in build.properties, these are documented within the file itself. There is also additional
documentation at http://incubator.apache.org/wookie/wookie-server-administrators-guide.html

Downloading & Building Wookie
=============================

You will need to download and install Apache Ant in order to build from source. See http://ant.apace.org

Wookie is designed to be downloaded using subversion; to checkout the latest code:

svn co https://svn.apache.org/repos/asf/incubator/wookie/trunk

This will downloaded the latest source code to your current folder. 

You then need to enter:

cd PROJECT_HOME_DIRECTORY
ant

The first time you run this command all dependencies will be downloaded so you must be online and 
it will take some time. Subsequent builds will be much faster.

You can run Wookie in a number of different configurations, as described in the following sections.

Running Wookie in "standalone" mode
===================================
The quickest way to run Wookie is in "standalone" mode; in this mode Wookie uses a local database 
and embedded server. This is a good way to experiment with Wookie in development.

To run Wookie in standalone mode:

ant run

The first command will download any required libraries for Wookie. The second will start a server 
running on port 8080. To access it, go to http://localhost:8080/wookie

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and
 password 'java'.

If you want to start with a completely clean build then use:

ant clean-build run

If you want to clean the database use:

ant clean-db

Running Wookie in debug mode
============================
You can start Wookie in debug mode using JDSPA with the following command:

ant -Djvmargs="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n" run

Running Wookie with Tomcat and MySQL
====================================
The following will compile and run the server in Tomcat with a MySQL database. The database tables will be set up automatically, however you must have previously created a database called "widgetdb" with username "java" and password "java". (If you want to change the database connection settings, then you need to alter the scripts/mysql/hibernate.cfg.xml.)

First, you need to edit the "build.properties" file as follows:

uncomment "project.using.mysql=true" and comment out "project.using.derby=true"
set "servletEngine.webapp.dir=" to the location of your tomcat webapps directory.

Next you need to setup the MySQL tables by running:

ant clean-db

If you have previously used Wookie in standalone deployment, to ensure the right scripts are included in the build you need to run:

ant clean-build

Then to build and deploy the webapp, run:

ant deploy-webapp

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and password 'java'.

If you want to start with a completely clean build then use:

ant clean-build deploy-webapp

If you want to clean the database use:

ant clean-db

By default Wookie connects to MySQL using Hibernate and the C3P0 connection pooler.

Once the server is running connect your debugger to port 8000. If "suspend=y" is used, it will will block, waiting for a debug connection before starting the server.

Running Wookie with other configurations
========================================
Other configurations of Wookie can also be run; the main requirements are a servlet container and a database. The setup information for databases can be found in the /scripts directory. How these are loaded into configurations is defined in build.xml; the main requirement is to define connection details in scripts/database/hibernate.cfg.xml and to define the database setup script in scripts/database/widgetdb.sql.

The deploy-webapp task can be used to deploy Wookie to any servlet container application.

Building and deploying widgets
==============================
See http://incubator.apache.org/wookie/building-widgets.html