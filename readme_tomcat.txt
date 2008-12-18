Prerequisites
(a) java v1.5.* or later installed
(b) tomcat v5.5.* installed
(c) Mysql v5.0.* installed
 
Setup 
(1) Place the "wookie" folder found in this archive, under your "webapps" folder in tomcat
	- usually found under tomcat/webapps	
(2) You will need to alter the "tomcat-users.xml" file under your tomcat instance
	- usually found under tomcat/conf

	You will need to add two entries to enable the admin username/password...
	
	<role rolename="widgetadmin"/>
	<user password="java" roles="widgetadmin" username="java"/>
	
	...otherwise you will not be able to login to the admin section of the widget server 
	(unless you remove the authentication defined in the web.xml file)
(3) Login to mysql and create a new user - -u java -p java
(4) Run the "widgetdb_mysql.sql" script found in this archive
(5) Grant the user "java" to have access rights to the "widgetdb" schema in mysqladmin
(6) Start tomcat and navigate to http://localhost:8080/wookie in your browser