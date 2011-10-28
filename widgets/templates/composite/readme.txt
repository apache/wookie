This is a single widget used to display multiple widgets using a progressive enhancement technique to display 
the widgets in a sensible way regardless of the screen size.

* Configuration

** Widget Server

By default this widget tries to retrieve other widgets from "localhost:8080/wookie/", in a production environment 
you will need to load widgets from a live server. Change the "composite.wookie.domain", "composite.wookie.port" and "composite.wookie.path" 
accordingly.

You will also need to set the API key information accordingly using "composite.wookie.apikey", "composite.wookie.userid" and "composite.wookie.shareddatakey"
properties. By default these are set to the default test user credentials.

* Further REading

For more info on the progressive enhancement features 
see http://scottbw.wordpress.com/2011/03/16/adventures-in-progressive-enhancement/