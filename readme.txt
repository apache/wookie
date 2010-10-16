Wookie
======
Welcome to Wookie!

Apache Wookie is a Java server application that allows you to upload and deploy widgets for your applications; widgets can not only include all the usual kinds of mini-applications, badges, and gadgets, but also fully-collaborative applications such as chats, quizzes, and games.

Wookie is based on the W3C Widgets specification, but widgets can also be included that use extended APIs such as Google Wave Gadgets and OpenSocial.

Apache Wookie is an effort undergoing incubation at The Apache Software Foundation (ASF). Incubation is required of all newly accepted projects until a further review indicates that the infrastructure, communications, and decision making process have stabilized in a manner consistent with other successful ASF projects. While incubation status is not necessarily a reflection of the completeness or stability of the code, it does indicate that the project has yet to be fully endorsed by the ASF.

Administrators can upload Widgets packaged according to the W3C Widgets specification, and makes these available to applications using a REST API. Applications typically have a plugin that implements this REST API, and enables users to pick widgets from a gallery of those available to add to their pages. There is a Connector Framework included in Wookie for many different programming languages that simplifies the process of writing plugins.

For more information see http://incubator.apache.org/projects/wookie.html

Questions? Need Help?
=====================
If you have questions about Wookie, please check the FAQ (see http://incubator.apache.org/wookie/faq.html) to see if someone has already posted an answer to your question. Failing that, you can post a message to either the wookie-dev or wookie-user mailing list; to subscribe to a mailing list, send an email with the subject "subscribe" to "wookie-dev-subscribe@incubator.apache.org" or "wookie-user-subscribe@incubator.apache.org"

Downloading & Building Wookie
=============================

Running Wookie in "standalone" mode
===================================
The quickest way to run Wookie is in "standalone" mode; in this mode Wookie uses a local database and embedded server. This is a good way to experiment with Wookie in development.

To run Wookie in standalone mode:

> cd PROJECT_HOME_DIRECTORY
> ant run

The first time you run wookie (or compile it) any required libraries will be downloaded, this can take some time and requires a network connection.

Note that by default, wookie will clean the database each time it is run, this ensures that your development environment starts in a consistent state. However, any modifications you have made to the database during previous runs will be lost.

To run the development server without cleaning the database you need to set initDB=false. See the next section for more details.

Once the server is running go to http://localhost:8080/wookie

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and password 'java'.

If you want to start with a completely clean build then use:

> cd PROJECT_HOME_DIRECTORY
> ant clean-build run

Running options
===============
To set run mode options you can either add properties to the local.build.properties file or you can pass properties in via the command line using -Drun.args="<property_name>=<property_value> <property_name>=<property_value>". For example:

-Drun.args="port=8080 initDB=false"

Available properties are:

Property | Values	         | Description	                               | Default
------------------------------------------------------------------------------------
initDB	 | True or False	 | Initialise the DB to a default configuration| True
port	 | Integer	         | Set the port on which the server is to run  | 8888

Running Wookie in debug mode
=============================
You can start Wookie in debug mode using JDSPA with the following command:

> ant -Djvmargs="-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n" clean-db run

Once Wookie is running in debug mode you need to connect to it using your debugger.

Debugging with Eclipse
======================
To connect Eclipse to Wookie running in debug mode on your local machine you would:

> Run -> Debug Configurations... 

> Select "Remote Java Application"

> Click "New Launch Configuration"

Set as follows:

Name: Wookie Debug (Localhost)
Project: Click "Browse" and select your Wookie project
PORT: 8001

Now click the "Debug" button (note you must be running Wookie in debug mode, see above, first).

You can now set breakpoints etc. as if you were running from within Eclipse.

Once you have set up this "debug configuration" you can quickly access it from your debug menu.

For more on remote debugging with Eclipse see http://www.eclipsezone.com/eclipse/forums/t53459.html

Note, you can set up a run configuration to be able to run Wookie from the UI of Eclipse, but I'll leave that for you to work out as I use the command line.

Running Wookie with Tomcat and MySQL
====================================
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

You will need to create a user with the role "widgetadmin" in your tomcat installation. For example, add the following to tomcat-users.xml:

<role rolename="widgetadmin"/><user username="java" password="java" roles="widgetadmin"/>

If you have previously used Wookie in standalone deployment, to ensure the right scripts are included in the build you need to run:

> ant clean-build

Then to build and deploy the webapp, run:

> ant deploy-webapp

You can then start Tomcat as usual.

To access the administration menu (http://localhost:8080/wookie/admin) use the username 'java' and password 'java'.

If you want to start with a completely clean build then use:

> ant clean-build deploy-webapp

Running Wookie with a security manager
======================================
NOTE: This section was written before moving from Hibernate to OpenJPA/JCR and so some of these instructions may no longer be correct.

If you are running Wookie with a security manager you will have to grant some permissions to get Wookie running properly. Otherwise you are likely to run into this of a similar error:

SEVERE: Exception sending context initialized event to listener instance of
 class org.apache.wookie.server.ContextListener
java.security.AccessControlException: access denied
(java.util.PropertyPermission user.dir read) at ...
If you can't turn off the security manager but don't really care about it, you can consider to replace to contents of the policy file (in Tomcat this is typically $TOMCAT_HOME/conf/catalina.policy, Ubuntu users will find the policy files in /etc/tomcat6/policy.d/) with the grant all statement:

grant {
  permission java.security.AllPermission;
};

This is not a recommended way of working for a production environment, because it effectively means turning off security alltogether.

Another option is to properly configure the security policy file. Below is a list of permissions that Wookie needs. Most of these are for Hibernate that needs to generate 'enhanced' classes at runtime.

// fixes access denied (java.util.PropertyPermission user.dir read)
grant {
  permission java.util.PropertyPermission "*", "read,write";
};

// fixes access denied (java.io.FilePermission TOMCAT_HOME\bin\local.widgetserver.properties read)
grant {
  permission java.io.FilePermission
	"<<ALL FILES>>", "read, write";
};

// fixes access denied (java.net.SocketPermission localhost resolve)
grant  {
   permission java.net.SocketPermission "*", "resolve,connect";
};

// fixes access denied (java.lang.RuntimePermission accessClassInPackage.org.apache.catalina)
// fixes access denied (java.lang.RuntimePermission accessDeclaredMembers)
// fixes access denied (java.lang.RuntimePermission getProtectionDomain)
grant {
    permission java.lang.RuntimePermission "accessClassInPackage.org.apache.catalina";
	permission java.lang.RuntimePermission "accessDeclaredMembers";
	permission java.lang.RuntimePermission "getProtectionDomain";
};

// fixes access denied (java.lang.reflect.ReflectPermission suppressAccessChecks)
grant  {
   permission java.lang.reflect.ReflectPermission "suppressAccessChecks";
};

Note that some of these fixes are still not very strict, which means that if you
are really concerned to limit permissions as much as possible, you need to define
stricter rules, but it's a start and should get your Wookie instance up-and-running.

For more information how to do this in Tomcat, see the Security Manager HOW-TO

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
Other configurations of Wookie can also be run; the main requirements are a servlet container and a database.

Database connections can be set up using either JPA (Java Persistence API) or JCR; the configuration details are set in build.properties. Additional configuration for advanced use can be found in org.apache.wookie.beans.jpa/peristence.properties. The database schema and mappings is located in etc/ddl-schema.

The deploy-webapp task can be used to deploy Wookie to any servlet container application.

Running Wookie in a virtual host configuration
==============================================
This has been noted as an issue with the current release (see WOOKIE-111).

If anyone has any advice on resolving this issue, then please do comment on the issue at https://issues.apache.org/jira/browse/WOOKIE-111

Building and deploying widgets
==============================
See the instructions in widgets/readme.txt



