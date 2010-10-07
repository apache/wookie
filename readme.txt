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

> cd PROJECT_HOME_DIRECTORY
> ant

The first time you run this command all dependencies will be downloaded so you must be online and 
it will take some time. Subsequent builds will be much faster.

You can run Wookie in a number of different configurations, as described in the following sections.

Running Wookie in "standalone" mode
===================================
The quickest way to run Wookie is in "standalone" mode; in this mode Wookie uses a local database 
and embedded server. This is a good way to experiment with Wookie in development. Note that by
default the database is cleared and initialized on every launch.

To run Wookie in standalone mode:

> cd PROJECT_HOME_DIRECTORY
> ant run

The first command will download any required libraries for Wookie. The second will start a server 
running on port 8080. To access it, go to http://localhost:8080/wookie

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and
 password 'java'.

If you want to start with a completely clean build then use:

> cd PROJECT_HOME_DIRECTORY
> ant clean-build run

Running Wookie in debug mode
============================
You can start Wookie in debug mode using JDSPA with the following command:

> cd PROJECT_HOME_DIRECTORY
> ant -Djvmargs="-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n" run

Once the server is running connect your debugger to port 8000. If "suspend=y" is used, it will will block, waiting for a debug connection before starting the server.

Running Wookie with Tomcat and an external RDBMS
================================================
The following will compile and run the server in Tomcat with a specified RDBMS using JPA persistence. The database schema can be defined and populated on launch, however you must have previously done the following:

1. Obtained a JDBC driver jar file for the database,
2. created an empty database for Wookie's persistent storage, and
3. have valid database credentials to access it.

Make the following changes to configure build.properties, (substituting CATALINA_HOME with the absolute or relative path to Tomcat's installation directory and specifying the database driver, url, type and credentials connection information):

widget.deploy.dir=CATALINA_HOME/webapps/wookie/deploy
servletEngine.webapp.dir=CATALINA_HOME/webapps
servletEngine.container.lib.dir=CATALINA_HOME/lib
servletEngine.context.conf.dir=CATALINA_HOME/conf/Catalina/localhost
wookie.persistence.manager.type=jpa
wookie.db.user=java
wookie.db.password=java
wookie.db.driver=com.mysql.jdbc.Driver
wookie.db.uri=jdbc:mysql://localhost:3306/widgetdb
wookie.db.type=mysql
jdbc.driver.path=../mysql-connector-java-5.1.7.jar

The wookie.db.type setting can accept the following values: db2, derby, hsqldb, mssql, mysql, mysql5, oracle, oracle9, oracle10, postgresql, and sybase. The jdbc.driver.path setting can be absolute or relative file path to the driver jar archive.

To ensure the configuration changes are propagated into the Wookie build artifacts, perform a full clean build after changing build.properties:

> cd PROJECT_HOME_DIRECTORY
> ant clean-build deploy-webapp

To force Wookie to define or reset the database schema on the first start, use the following commands to start the Tomcat server:

> cd PROJECT_HOME_DIRECTORY
> export CATALINA_OPTS="-Dwidget.persistence.manager.initstore=true"
> CATALINA_HOME/bin/startup.sh

To shutdown the Wookie Tomcat server and clear the initialization flag:

> cd PROJECT_HOME_DIRECTORY
> CATALINA_HOME/bin/shutdown.sh
> export CATALINA_OPTS=

There are also SQL scripts for all of the valid database configurations built as part of Wookie that can be used to initialize the database schema manually if desired. These are located here:

build/classes/org/apache/wookie/beans/jpa/db2-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/derby-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/hsqldb-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/mssql-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/mysql5-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/mysql-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/oracle10-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/oracle9-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/oracle-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/postgresql-wookie-schema.sql
build/classes/org/apache/wookie/beans/jpa/sybase-wookie-schema.sql

The Wookie server will populate the newly initialized database with seed data to complete the process when started.

Running Wookie using embedded Jackrabbit JCR
============================================
The folloing property is used to switch the standalone or Tomcat deployed servers to utilize JCR for persistence via an embedded JCR instance:

wookie.persistence.manager.type=jcr

Additional properties must be configured to specify the desired JCR root node path and credentials for tomcat deployments:

wookie.repository.user=java
wookie.repository.password=java
wookie.repository.rootpath=/wookie
wookie.repository.workspace=default

These replace the wookie.db.* and jdbc.drive.path settings above, but otherwise the instructions apply for the JCR configuration.

Running Wookie with other configurations
========================================
Other configurations of Wookie can also be run; the main requirements are a servlet container and a database. The deployment and configuration operations can be determined by reviewing build.xml and build.properties. With some tweaking, the deploy-webapp task can be used to deploy Wookie to most any servlet container application.

Both the RDBMS and JCR versions of Wookie depend only on container defined JNDI resources. Alternate persistence configurations are made by selecting the persistence manager component and supplying the required resource.

Running Wookie with virtual host configuration
==============================================
See http://incubator.apache.org/wookie/downloading-and-installing-wookie.html

Building and deploying widgets
==============================
See http://incubator.apache.org/wookie/building-widgets.html
