This is a PHP implementation of the Wookie Connector Framework. It is intended to map the java
implementation which should be considered the reference implementation of the connector 
framework.

INSTALLATION
============

Simply copy these files into a convenient place in your plugin and use the WookieConnectorService
to communicate with the Wookie Server. 

At the time of writing this code relies on software that may not be compatible with 
the Apache Software License. Until we resolve this concern you will need to download the
XML serialising code from http://keithdevens.com/software/phpxml#source and save it as 
"xmlFunctions.php" in the same directory as this code. 