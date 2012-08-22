===========================
Client Widget Signer Guide
===========================

This is the client digital signature widget signing package developed as part
of Apache Wookie (incubating).
See http://dev.w3.org/2006/waf/widgets-digsig/ 
and https://issues.apache.org/jira/browse/WOOKIE-139
for more info. (Thanks to Pushpalanka Jayawardhana)

Guide to the Swing based client application
===========================================
Run "SignCoordinator" (as a standalone java app) or you can also run the top 
level ant task 'build-signer' to generate an executable jar package. 
A swing based application should appear. 
What follows is a brief explanation of the fields in the application.

Author/Distributor
------------------	
Your role should be selected. Depending on your role, files will be selected for signing 
according to the W3C widget digsig specification. An Author will sign all the content of the 
widget except distributor signatures. A distributor will sign all the content of a widget 
except other distributor signatures.

Keystore File
-------------
The recommended key length is 4096 bits. Only RSA keystores are accepted here according to W3C 
widget digsig specification. You can use the given sample keystore file for testing purposes 
which is generated using Java keytool. (Found in digsig-client/java/resources/wookie_test_store.jks)
Alternatively see below on how to generate a new one.

Keystore Password
-----------------
Password given for Keystore file. For the sample keystore file this is 'wookie'.

Private Key Alias
-----------------
The key alias given in key generation. For the sample this is 'wookietest'.

Private Key Password
--------------------
You can keep this blank to use the same password as of the keystore, which is the default setting. 
If it differs you can enter it here. 

Certificate Alias
-----------------
The alias for the X509 certificate. You can keep this blank to use the same alias as of the private 
key, which is the default setting.

Path to Widget
--------------
Point to the folder which carries widget content. Once you select the path, the content to be signed 
will be shown in the below text area. According to your role files will be selected and any hidden 
files(name starting with '.') will be skipped. Before signing you can check whether the signing content 
is correct. Any modifications needed should be done in the widget folder and reselect the folder in GUI.

Widget Name
-----------
Any preferred name for the widget.

Once you press 'Sign' the signer will generate a signature file for the selected content, using the given 
key details. The generated signature file will be stored in the same folder. Also the signed content and the 
signature will be packed into 'widget_name.wgt' and stored in the same folder, that you can directly send 
to deployment.


How to generate a new keystore file
===================================

Replace <your *something*> with your own values below

C:\temp>keytool  -genkeypair -alias <your alias> -storepass <your password> -validity 365 -keyalg RSA -keysize 4096 -keystore <filename>.jks
What is your first and last name?
  [Unknown]:  <enter your name> 
What is the name of your organizational unit?
  [Unknown]:  <enter your ou>
What is the name of your organization?
  [Unknown]:  <enter your org>
What is the name of your City or Locality?
  [Unknown]:  <enter your city>
What is the name of your State or Province?
  [Unknown]:  <enter your state>
What is the two-letter country code for this unit?
  [Unknown]:  <enter your county, i,e GB>
Is CN=Fred Bloggs, OU=myDept, O=Acme99 Inc, L=someTown, ST=someState, C=gb correct?
  [no]:  yes

Enter key password for <your alias>
        (RETURN if same as keystore password):

C:\temp>