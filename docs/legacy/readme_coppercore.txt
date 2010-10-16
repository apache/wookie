This README applies if you intend to build Wookie as part of a Learning Design runtime system using CopperCore; if you don't intend to use Wookie in this fashion, please refer to the README for Tomcat.

How to setup the source code for Eclipse
----------------------------------------
This codebase was compiled against Coppercore version v3.1 released 30/03/2007. (http://www.coppercore.org)
Also used was a snapshot of SLeD version 3 from the original UKOU sourceforge location (taken 13/12/2007)
The setup used for the codebase was Eclipse 3.3, using WTP for the widget project. (http://www.eclipse.org/)
Note: Do not start CopperCore until you have followed all of the instruction in this section, or you may experience problems.
Note: For some projects (org.tencompetence.widgetservice for example) you will need to define a Tomcat 5.5. server instance in eclipse WTP called "Apache Tomcat v5.5" (should be the default for tomcat 5.5.)
Note: For some projects (org.tencompetence.jboss.mbean.db for example) you must define a JBOSS_HOME variable within eclipse which points to the CCRT/jboss-4.0.4.GA folder.

(A) Create a (parent) folder on your hard drive for java projects (i.e. C:\java_projects)
	- You will have sub-folders under here which will house each eclipse projects.

(B) Download the following zip file from: http://coppercore.sourceforge.net/downloads.shtml

	(i) CopperCore Runtime Environment (coppercore_ccrt_3.1.zip)

(C) Unzip this downloaded zip file into the (parent) folder you previously created and you should end up with the following path...

	(i) a folder called "ccrt" (C:\java_projects\ccrt)

(D) The following modules are needed for a complete build of everything on CopperCore. Check out from the tencompetence cvs 
	repository INTO THE SAME PARENT FOLDER WHERE CCRT resides (i.e. into "C:\java_projects")...

	wp6/org.coppercore.ccsi
	wp6/uk.ac.open.sled
	
	wp6/org.tencompetence.widgetservice (optional)
	
	wp6/org.tencompetence.jboss.mbean.db (optional, but recommended, if using the Widget service.
	This module allows CCRT to be patched so that it can run the widget database 
	using a combination of HSQL and an mbean auto-loader, without having to set up an external database)
	
	IMPORTANT: check these modules out so that each project is in the same folder as CCRT. 
	This is because the ant build scripts expect CCRT to be in a certain location. 
	If you wish to check out the modules differently, it will be up to you to modify them accordingly.

	Your folder structure should now look like this...
	
	C:\java_projects\ccrt
	C:\java_projects\org.coppercore.ccsi
	C:\java_projects\uk.ac.open.sled
	C:\java_projects\org.tencompetence.widgetservice
	C:\java_projects\org.tencompetence.jboss.mbean.db
	

(E) What the modules/folders do

	ccrt
	----
	This is the CopperCore runtime environment, that you downloaded.  It offers a quick out-of-the-box solution for getting 
	up & running with CopperCore. (In production systems, certain attributes would need to be altered, but it is what 
	we will use for testing purposes).  Any changes made in the other modules will need to be copied across 
	to this CopperCore instance to take effect. However, this nightmare ;-) of updating the bits & pieces should be made 
	much simpler by using the using various ant build scripts included in each module. I recommend that if you are
	running one of the ant build scripts that you stop this server instance and restart once it has completed, as hot
	deploying on JBoss can sometimes result in strange things happening.
	
	
	org.coppercore.ccsi
	-------------------
	This module contains the CopperCore Service Integration framework. It is here that you would add any new services for
	CopperCore.  As a guide of how to write these, there are already - APIS, SCO12 and WIDGET services within the code.
	Study how these are written in order to write your own.  H+H supplied an ant build script which generates the build for
	CCSI, which makes this easier to accomplish.  Additionally, the script also copies across any other configuration data 
	that would be needed, i.e. any additional/updated properties files.  You may wish to study how these extra resources
	are copied across using the ant script & the files found under the "ccrt_patch" folder. If you create a new service you,
	will at the very least, need to modify the "ccsi.xml" to include any new service locations.
	
	
	uk.ac.open.sled
	---------------
	This module is the UK Open Universities web player for CopperCore. It is included here because you will probably also
	need to modify the player when you have added a new service via CCSI.  (The player will need to know how to handle the UI).
	TODO - describe this a bit more
	
	org.tencompetence.widgetservice
	-------------------------------
	You will need to define a Tomcat 5.5. server instance in eclipse WTP called "Apache Tomcat v5.5" (should be the default name for tomcat 5.5.), otherwise you may
	see that the project has missing dependencies.
	
	org.tencompetence.jboss.mbean.db
	--------------------------------
	If you have checked this project out, you must define a JBOSS_HOME variable within eclipse which points to the CCRT/jboss-4.0.4.GA folder.

	TODO - finish this off
	
(F) Setting up your WTP project to run the admin password facility from Eclipse

	For your tomcat enabled web project in Eclipse you will need to update the generated server.xml file for the widget 
	web app configuration.  If you look under the "Servers" project -> <YOUR SERVER TO RUN THE WEB APP FROM>, you will find the tomcat-users.xml file
	You will need to add two entries to enable the admin username/password.
	
	<role rolename="widgetadmin"/>
	<user password="java" roles="widgetadmin" username="java"/>
	
	..otherwise you will not be able to login to the admin section of the widget server unless you remove the authentication
	defined in the web.xml file.	
  
Problems - email P.Sharples@bolton.ac.uk
