How to setup Wookie with Shindig
----------------------------------------

There are two ways to deploy a Wookie+Shindig server. The easiest is to use the pre-built single integrated service which can be found in the same folder as this README. If you want to, you can also deploy the two application separately (e.g. to use a custom Shindig or to make use of a newer Shindig build).

1. Deploying as a single application
====================================

To deploy the default single-app configuration, you need to build Wookie using the additional files located in our CVS repository under shindig/WEB-INF.

Note that the web.xml file located in shindig/WEB-INF replaces the default Wookie web.xml file.

Once installed, make sure you read the section on Proxy Configuration below.


2. Deploying Wookie and Shindig as separate, integrated, applications
=====================================================================

To deploy Wookie with Shindig separately, you need to build the Shindig.war and deploy on your Tomcat server alongside Wookie.

You can either place the Shindig webapp as ROOT or at another webapp location. If deploying elsewhere, note you must change
all the URLs in [SHINDIG]/WEB-INF/classes/containers/default.container.js, and also in shindig.properties, which can be found
inside the shindig-common-1.1. jar file.

2a Wookie setup
---------------
Once deployed, you need to setup Wookie as follows:

In [WOOKIE]/WEB-INF/classes/widgetserver.properties, set widget.metadata.url to point to the Shindig metadata service (if you've set up Shindig at Root, 
this is already set)

In [WOOKIE]/WEB-INF/classes/opensocial.properties, set opensocial.enable to true to start sending Shindig security tokens with rendered widgets/gadgets.

2b Shindig/Wookie features
--------------------------
You can use Wookie to manage the preferences of your Gadgets; to do this you must replace the standard
Shindig Features jar with one with some additional Wookie javascript code. You can download this jar
from the Wookie CVS repository.

3. Proxy configuration
======================
Note that both Wookie and Shindig come with proxy implementations. You can use either or both of these. The setting for W3C widgets can be found in [WOOKIE]/WEB-INF/classes/widgetserver.properties. The setting for Google Gadgets/OpenSocial Apps can be found in [SHINDIG]/WEB-INF/classes/containers/default.container.js. In each case you can set the proxy URL to either:

[WOOKIE]/proxy
[SHINDIG/gadgets/proxy

You can disable either of these services by commenting out the relevant sections of the web.xml files in Shindig and Wookie.

The main difference is that Wookie uses a whitelisting proxy, but currently this does not support proxied access to images, only to XML/JSON. (This is because we mostly assume that Widgets want to call AJAX methods rather than fetch images). In contrast the Shindig proxy service handles all media types and has no whitelist/blacklist at all - be very careful if you choose to expose it!

The most obvious way to safeguard your proxy is in your Apache server. Basically the only services that should make use of your proxy service are registered widget instances served from your Wookie/Shindig webapp host.

(One day maybe we'll be able to separate out the Proxy service from both Wookie and Shindig to make it standalone and easier to secure).

That's it folks
===============
Fingers crossed, once you've done these steps you can restart Tomcat and all will be well!
  
Problems - email scott.bradley.wilson@gmail.com
