How to setup Wookie with Shindig
----------------------------------------

To deploy Wookie with Shindig, you need to build the Shindig.war and deploy on your Tomcat server alongside Wookie.

You can either place the Shindig webapp as ROOT or at another webapp location. If deploying elsewhere, note you must change
all the URLs in [SHINDIG]/WEB-INF/classes/containers/default.container.js, and also in shindig.properties, which can be found
inside the shindig-common-1.1. jar file.

(We'll eventually provide a sample shindig.war on our website you can use to get started - copy it into you Tomcat webapps folder and you're good to go)

Once deployed, you need to setup Wookie as follows:

In [WOOKIE]/WEB-INF/classes/widgetserver.properties, set widget.metadata.url to point to the Shindig metadata service (if you've set up Shindig at Root, 
this is already set)

In [WOOKIE]/WEB-INF/classes/opensocial.properties, set opensocial.enable to true to start sending Shindig security tokens with rendered widgets/gadgets.

Note that both Wookie and Shindig come with proxy implementations. You can use either or both of these. The setting for
W3C widgets can be found in [WOOKIE]/WEB-INF/classes/widgetserver.properties. The setting for Google Gadgets/OpenSocial Apps
can be found in [SHINDIG]/WEB-INF/classes/containers/default.container.js. In each case you can set the proxy URL to either:

[WOOKIE]/proxy
[SHINDIG/gadgets/proxy

You can disable either of these services by commenting out the relevant sections of the web.xml files in Shindig and Wookie.

The main difference is that Wookie uses a whitelisting proxy, but currently this does not support proxied access to images, only
to XML/JSON. (This is because we mostly assume that Widgets want to call AJAX methods rather than fetch images). In contrast
the Shindig proxy service handles all media types and has no whitelist/blacklist at all - be very careful if you choose to
expose it!

Fingers crossed, once you've done these steps you can restart Tomcat and all will be well!
  
Problems - email scott.bradley.wilson@gmail.com
