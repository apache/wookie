The Scanning Template adds basic switch control functionality to a
widget. It extends the base template. By default it will scan through
all elements that have a "scan" CSS class assigned to them.

Using
=====

To use this template you must provide the elements over which we should scan by setting
${widget.shortname}_scanning_controller.scanElements, e.g.

${widget.shortname}_scanning_controller.scanElements = $('.scan');

You can also change various aspects of the scanning functionality by
setting the following variables in ${widget.shortname}_scanning_controller:

    delay: 1000, // time in milliseconds between focus change

These can also be set in the widget.properties file using:


