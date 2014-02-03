This is a simple template that provides a two column layout.

* Primary Content

content_primary.html will be the main body, by default this will be
rendered on the right of the page.

* Secondary Content

content_secondary.html will commonly be used for navigation or sidebar
content. By default it is rendered either to the left or right of the
screeen (see "Column Behaviour" below) when there is suffficient width
in the screen. Where the screen is too narrow the secondary content
will appear above the primary content. 

Any collapsible items in this area will be expanded or collapsed as
appropriate for the screen dimensions. That is, on a screen with
reduced height the secondary content area will be collapsed by
default.

* Layout Configuration

** Column Behaviour

The positions of the secondary and primary content on a wide screen
are controlled by the twoColumn.primary.position and
twoColumn.secondary.position. Set these to either "left" or "right" in
your widget.properties file to change the layout in widescreen view.
