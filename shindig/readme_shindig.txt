How to setup Wookie with Shindig
----------------------------------------

To deploy Wookie with Shindig, you need to deploy the Shindig.war on your server alongside Wookie.

You can either place the Shindig webapp as ROOT or at another webapp location. If deploying elsewhere, note you must change
all the URLs in [SHINDIG]/WEB-INF/classes/containers/default.container.js, and also in shindig.properties, which can be found
inside the shindig-common-1.1. jar file.

1 Wookie setup
---------------
Once deployed, you need to setup Wookie as follows:

In [WOOKIE]/WEB-INF/classes/widgetserver.properties, set widget.metadata.url to point to the Shindig metadata service (if you've set up Shindig at Root, 
this is already set)

In [WOOKIE]/WEB-INF/classes/opensocial.properties, set opensocial.enable to true to start sending Shindig security tokens with rendered widgets/gadgets.

2 Shindig/Wookie features
--------------------------
You can use Wookie to manage the preferences of your Gadgets; to do this you must replace the standard
Shindig Features jar with one with some additional Wookie javascript code and make an adjustment to the 
web.xml for Wookie to allow calls between the two applications. More information on this can be found
in features/README

3. Proxy configuration
======================
Note that both Wookie and Shindig come with proxy implementations. You can use either or both of these. The setting for W3C widgets can be found in [WOOKIE]/WEB-INF/classes/widgetserver.properties. The setting for Google Gadgets/OpenSocial Apps can be found in [SHINDIG]/WEB-INF/classes/containers/default.container.js. In each case you can set the proxy URL to either:

[WOOKIE]/proxy
[SHINDIG/gadgets/proxy

You can disable either of these services by commenting out the relevant sections of the web.xml files in Shindig and Wookie.

The main difference is that Wookie uses a whitelisting proxy, but currently this does not support proxied access to images, only to XML/JSON. (This is because we mostly assume that Widgets want to call AJAX methods rather than fetch images). In contrast the Shindig proxy service handles all media types and has no whitelist/blacklist at all - be very careful if you choose to expose it!

The most obvious way to safeguard your proxy is in your Apache server. Basically the only services that should make use of your proxy service are registered widget instances served from your Wookie/Shindig webapp host.

(One day maybe we'll be able to separate out the Proxy service from both Wookie and Shindig to make it standalone and easier to secure).

4. Try it out
=============
In Wookie, go to the Admin page and click "Add new Google Gadget/OpenSocial app". 

Enter "http://www.google.com/ig/modules/hello.xml" and press return.

If all goes well, you should see the message "Successfully created a new entry for this gadget". 

Go to the Wookie widget gallery, and you should see a new widget called "Hello World Example". 

Click "demo" and you should see "Hello, world!" for the gadget.

That's all folks
===============
  
Problems - email wookie-dev@incubator.apache.org
