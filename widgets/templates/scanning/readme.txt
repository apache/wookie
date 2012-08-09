The Scanning Template adds basic switch control functionality to a
widget. It extends the base template.

Using
=====

To use this template you must provide the elements over which we should scan by setting
${widget.shortname}_scanning_controller.scanElements, e.g.

HTML5 data attributes are a good way to identify elements that chould
be included in the scanning.

<div data-scanOrder="3">...</div>
<div data-scanOrder="1">...</div>
<div data-scanOrder="2">...</div>

We can then use a selector like the following to tell the scanning
code which elements to iterate over:

${widget.shortname}_scanning_controller.scanElements = $('[data-scanOrder]');

Sorting the elements based on the value of this attribute will allow
us to change the scanning order. This might be performed using a
technique such as
http://james.padolsey.com/javascript/sorting-elements-with-jquery/

Configuration
=============

You can also change various aspects of the scanning functionality by
setting the following variables in ${widget.shortname}_scanning_controller:

    delay: 1000, // time in milliseconds between focus change

These can also be set in the widget.properties file using:

