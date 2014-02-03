This is a settings template. It will add a separate HTML (settings.html) page and 
associated scripts for managing settings. Your widget will need to provide a way to
navigate to this page. By default this will be an element with an id of "showSettings"
and will navigate to the settings page when clicked.

For example:

<a id="showSettings" href="#" data-role="button" data-mini="true">Settings</a>

How it works
============

A new page, defined in settings.html is injected into the existing
default page. This is used to display a dynamically generated settings
form.

The settings form is auto generated in the
${widget.shortname}_settings_controller.getsettingDefinitions(group)
method.  To create your own settings simply override this methodb.  If
your widget needs a large number of different settings you can group
settings together. Simply pass in the required group parameter to
retrieve the desired group of settings.

Groups can be used to describe related sets of settings. When the
settings page is requested the user will be presented with a list of
links to individual settings pages for each group. If no groups are
defined then all settings will be displayed on a single settings
page. Groups are defined in the
${widget.shortname}_settings_controller.getGroups() method.

Creating your settings
======================

Your widget should override the default settings definitions provided
in
${widget.shortname}_settings_controller.getSettingDefinitions(group)
and ${widget.shortname}_settings_controller.getGroups().

