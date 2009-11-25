This directory contains widget sourcecode and build files.

Create a widget
===============

To create a widget:

ant seed-widget

This is an interactive target that will ask you a number of questions 
about the widget you wish to create. The first question asks for a 
short name for your widget. This short name is used as the name of the 
widget directory and other key widget. For the rest of this document 
WIDGET_SHORTNAME will refer to your response to this question.

Once the target has completed your widget skeleton will be available in
a directory called WIDGET_SHORTNAME.

The default widget will provide a very basic widget with default style
resource and javascript files.

Build a Widget
==============

cd WIDGET_SHORTNAME
ant

Your widget package will be created in the build directory.

Note, you can also run ant from the widget directory, in which case you will
be asked for the shortname of the widget you wish to work with.

Deploy a widget
===============

At present it is not possible to automatically deploy to a running widget server (we welcome
your help on this).

To deploy to a running server you will need to use the admin interface of the server.



 