This is the Wookie templating system that can be used to create
consistent user interface widgets. Using this system you can
create a widget with just a few lines of code and then, in order
make a change to the UI of all widgets, simply change the template.

By using these templates you will ensure that your users have a 
consistent experience but also have an accessible experience since 
these templates are designed with accessibility in mind.

* Installation

To use these templates you need to install Apache Ant. Note, that if
you install Ant on Ubuntu using apt-get you will also need to install
ant-options (this is included on other platforms).

* About Templates

The templates directory contains all the wookie templates. There are,
at the time of writing, two types of template, those that use JQuery
Mobile and those that use Enyo. Enyo templates have names in the form
of "enyoTemplteName" and JQueryMobile have no precursor to their name
(at some point in the future we will probably make the JQuery Mobile
templates conform to the same nameing standard,
e.g. "jqmTemplateName".

In all cases the appropriate "base" template of is the root of all
templates using that framework. Any edit to the template will be
reflected in all widgets that are generated after the edit (excpet
where the edited behaviour has been overridden by a child template).

** Status

The Enyo templates currently use Enyo 2.0 which does not yet have any
UI components. Therefore the Enyo templates are pretty think on the
ground with respect to styling. You can, of course, style everything
using CSS but since the Enyo project predicts a release of the UI
components in "about a month" (from launch in mid Jan 2012) it's
probably best to wait.

The JQuery Mobile templates have been used to build a number of
widgets. They are fairly flexible but the current Ant based build
system presents some limitations and unneccessary
complexity. Furthermore, JQuery Mobile is designed for use only on
mobile platforms. This can present some issues when being displayed on
a desktop machine.

Moving forwards we intend to continue experimenting with both the Enyo
and JQuery Mobile templates. At this stage both implementations should
be considered Alpha quality.

* Creating Widgets from Templates
Regardless of which framework you use the basic techniques for
building widgets is the same. Of course, when you start building real
widgets you will be using techniques specific to the chosen framework,
but for the purposes of this introduction we will not be addressing
these details.

** Building your first template based widget

This section describes how to build your widgets. We start off with a
simple hello world widget and then work through a number of
customisations.

*** Hello World Widget

In this example we will create a simple widget that says hello to the
world (what else would the first widget be?)

  * cd WOOKIE_HOME/widgets/templates/widgets
    * NB if you want to build your widgets elsewhere then see the advanced topic section below.
  * mkdir helloWorld
  * create a file called "./widget.properties
  * open widget.properties in your favourite editor and add the
    following lines (not including bullets):
    * template.name=base
    * widget.shortname=HelloWorld
    * widget.name=Hello World
    * widget.description=A very friendly widget to demonstrate how easy it is to build a widget from templates.
    * widget.id=http://www.apache.org/widgets/HelloWorld
  * Create a file called "content_primary.html" and the following content
    * <p>Hello World!</p>

That's it, you have built your first template based widget. Now you
need to generate and deploy it:

  * Ensure that Wookie is running locally
  * cd [WOOKIE_HOME]/widgets/templates/widgets
  * ant generate-all-widgets -Dwidget.include=helloWorld
  * take a look at your new widget in your local instance of Wookie

***Note:*** while this example creates the widget in the source tree
of Wookie you should not develop your widgets here under normal
circumstances. Instead you should define your own directory for
widgets within your own project directory, see Advanced Topics, below.

**** What did we just do?

This section describes in some detail what you did above.

***** Widget properties

Widgets are defined by a set of properties. The first set of
properties are used by all widgets and are defined in
"widget.properties" the next are defined in 
"../common/widget.properties" the last are the "default.widget.properties" 
provided by the template. You created a widget directory and defined 
the "widget.properties" file by copying the default properties 
provided by the template system.

Most of these properties should be self explanatory, with the
exception of the "template.name". This is the name of the template
your widget will be based on, for this demo we left that unchanged and
defined a few of the basic properties that are used to ensure the user
knows what a widget is when looking at the widget store.

***** Widget content

The default widget properties configures the widget, but do not tell
the template what content to display. Widget templates are intended to
be self-documenting. You can find out what content a template expects
to be provided by reading the appropriate files in the template. At
some point in the future we will generate real documentation from
these template files, but for now you need to do the leg work.

To discover what properties are available to the template we look in
"/templates/base/template_build.xml" and examine the "_init_template"
target. You may see entries such as:

    <loadfile property="content.primary"
              srcFile="content_primary.html"
              failonerror="false">

This entry is saying that a property called "content.primary" is being
defined by the contents of a file called "content_primary.html". In other 
words, you should provide a "content_primary.html" file in your widget 
definition. This property will be used wherever "${content.primary}" 
appears in the template definition. Take a look in the templates 
"index.html" to see this in action.

***** Building the widget

The command "ant generate-all-widgets" builds and deploys all your widgets
to a locally running Wookie server. Once this command has successfully
executed you can examine your widget in action.

The property "widget.include" defines a matcher for the widgets which
are to be generated.

** Adding some style

This section builds on the previous tutorial. We will add some simple
styling to the Hello World widget.

Starting in your widget definition directory:

  * mkdir style
  * create file "style/helloworld.add.css"
  * add the following to your css file
    * .content-primary { background: yellow; }  
  * cd ..
  * ant generate-all-widgets

Now take a look at your styled hello world widget.

**** What did we just do?

Any css file in the "style" directory with a ".add" name will be added
to template provided stylesheets. This allows you to define new styles
and also to override other styles defined by the templates you are using.

You can also add css files without the ".add". These will be copied
into your widget as separate files. However, the template will be
unaware of these stylesheets so this is only useful if you have some
mechansims by which your widget will be aware of them such as a
javascript theme switcher.

Note that you can use tokens defined in the widget.properties file in
your CSS. Simply use "${property.name}" in your file.

** Being dynamic

This section continues to build on the previous tutorial. In this
section we will add a tiny bit of Javascript that will ask the users
name and say hello to them personally.

Starting in the root directory of your widget do the following:

  * mkdir scripts
  * create a file called "scripts/user_controller.js"
  * Add the content in the Javascript section below to the "user_controller.js" file
  * Change the contents of "content_primary.html" to:
    * <p>Hello <span id="name">World</span></p>
  * cd ..
  * ant generate-all-widgets
  * Now take a look at your new widget in Wookie

*** Javascript

This is what you need in your "scripts/helloworld_controller.js" file:

var ${widget.shortname}_user_controller = { 
  init:function() { 
    name = prompt("Please enter your name","Your Name"); 
    $('#name').html(name);
  }
};

$('#home').live('pageshow',function(event) { 
  ${widget.shortname}_user_controller.init(); 
});

*** What did we just do?

Files in the script folder that end with a "_controller.js" are added
to the "controller.js" files provided by the templates being used. In
this file you can define the controller for your widget.

Note that you must use the ${widget.shortname} token and add a widget
unique string to the object name in order to ensure the controller
object is given a unique name.

We also register an action to take when the page is loaded. In this
case we define function in the controller that prompts for the users
name and replaces the word "world" in the content with that name.

Note that you can add additional libraries to your widget by placing
them in a "lib" directory in the root of the widget directory. Be sure
to add any copyright notices and licenses to a "NOTICE" file and
"legal" directory respectfully.

*** Overriding Template functionality

The scripts provided by your widget implementation can override the
functionality provided by the template. For example, the message
template provides a simple interface for creating and sending
messages. However, there is no way the template can implement the
message send functionality since each widget will use a different
delivery mechanism.

For this reason the ${widget.shortname}_message_controller provides
the following function:

send:function(subject, message) {
    alert("Send message:\n\n" + subject + "\n\n" + message);
}

If you want to implement a true message widget you need to override
this function in your widget javascript.

To do this you simply add code such as the following to your widgets
WIDGET_controller.js file:

${widget.shortname}_message_controller.send = function(subject, message) {
    // message send code here
}

** Tiled Templates
The Base Template provides functionality that allows it to provide a
separate view that is used when the viewport is below a certain
size. This view can provide reduced information and is designed for
use in views that have multiple widgets on display at the same time.

By default the maximum height and widget for a tiled viewport is 310
pixels. That is, if the viewport is 311 or more pixels in both width
and height it will display the content defined in the #home div of the
page definition. If the viewport is 310 or less pixels in either
height or width then the content in the #tile div will be displayed.

To customise the tile content your widget should provide a
content_tile.html file.

To customise the maximum viewport size for the tile view set the
properties "widget.tile.max.height" and "widget.tile.max.width" (both
default to 310).
** Common Files
When building a family of related widgets you are likely to provide a
set of common files that will be reused by multiple widgets. These can
be placed in a directory called "common" in the same directory as your
widget definitions. All files in this folder will be copied, with
token replacement, into each widget.

Because we use token replacement in these files too, it is possible
for each widget to customise the common files appropriately.

** Advanced Topics
*** Your own widgets directories
You can create a widgets directory anywhere you want it. To create
your own directory for widget definitions simply create the directory
and copy [WOOKIE_HOME]/widgets/templates/widgets_build.xml into your
new directory with the name build.xml. 

Now edit your build.xml file so that the value of the
"wookie.root.dir" property so that it points to the root of your
Wookie folder. You should also edit the name property of the root
project element.

Each of your widget definitions will then be contained in a sub-folder
of the directory you created above. That is, you will have a directory
structure like this:

+- widgets
     |
     +- build.xml
     +- widgetDefinition1
     |     |
     |     +- widget.properties
     |     +- ...
     |
     +- widgetDefinition2
           |
           +- widget.properties
           +- ...

*** Multiple widget directories

You can, optionally, have multiple widget sub-directories in your main
widget directory, each of which can be deployed individually or as a
whole. In order to do this create your additional widget directories
and then edit your build.xml file in your main widgets directory to
ensure that they all get built when calling the target
"generate-all-widgets". There is an exmple in the build.xml file you
created above.

*** Deploying to alternative locations
You can change the location widgets are deployed to by setting the
property "widget.deploy.dir" in the command line or in the widgets
build.xml file.
*** Deploying in compressed or expanded form
By default widgets are deployed in compressed ".wgt" files
only. However, if you want to deploy in uncompressed (expanded) format
you can do so by setting the propery "widget.deploy.expanded" in the
command line or in the widgets build.xml file.

To supress deployment in compressed form it is not quite so easy. You
must first override the widgets_build.xml file (see "Your own widgets
directories" above). In this file you will need to comment out the following line:

    <property name="widget.deploy.compressed" value="true"/>

* Building templatised widgets

To build and deploy the widgets run "ant generate-all-widgets" from
within your widgets directory which will generate all known widgets.

Alternatively you can use "ant generate-widgets" which will
ask you which widget definitions directory you wish to generate from,

*** Building selected widgets

When you have lots of widgets it can be useful to build just one, or a
set of related widgets. There are two ways to do this, you can provide
a pattern to filter widgets to be built or you can keep your widgets
in directories and build one directory at a time.

**** Filtering widgets to be built

Adding the following to your ant command will filter all widgets:

-Dwidget.include=WIDGET_PATTERN

where "WIDGET_PATTERN" is an include pattern that describes which
widgets should be generated and deployed. For example,

"ant generate-widgets -Dwidget.include=*browse"

will generate all widgets in a specified directory that have a name ending in "browse".

*** Building single directories

"ant generate-widgets" will ask you to specify which widget directory
is to be used. To avoid having to answer this question you can specify
it in the command line as follows:

"-Dwidget.definitions.dir=DIRECTORY"

* Define new templates

To create a new template simply create a directory in the templates
folder with an appropriate template name and copy
"base/template_build.xml" (for JQuery Mobile template) or
"enyo/base/template_build.xml" (for an Enyo based template) and edit
as follows:

  * name attribute of the <project ...> element
  * "_generate_from_parent_templates" target (see below)

Next you will need to add any resources the template needs.

*** Template Dependencies

Template can have dependencies on other templates. This allows us to
build up complex templates whilst still ensuring they are easy to maintain.

To define a templates parents simply edit the
"_generate_from_parent_templates" targtet in the
"template_build.xml". The "templates/base/template_build.xml" file has
a commented out example of how to do this.

When generating a widget from a template the parent templates are
processed first. Any resources that a subsequent template provides
will override resources provided by the parent templates. That is, the
most recently produced resource wins.

In general all templates will have the "base" template as a parent
(see below). The "base" template provides the bare minimum we need for
a W3C widget.

*** Configuring templates

By using properties we can create templates that can be customised. A 
template will define any number of properties in "template.properties".
These properties can then be reused in any of the files by inserting 
"${property.name}". When the template is used to create a widget these 
properties will be expanded.

Alternatively properties values can be provided in files. This is 
useful for properties that are intended to contain lots of information 
or formatting, such as HTML snippets.

Default values for properties are provided in the templates 
"deafault.widget.properties" file. This file also serves as documentation.


**** Template content

Each template will define some content that needs to be provided by
the widget definition. This content will be defined in files provided
by the widget definition (see below). The template must define what
these files are and must load them in the "_init_template" target of
the "template_build.xml" file. See the base template for an example of
how to do this.

Note that when a template has parent templates it will inherit all of
the customisation points of each parent. Therefore each widget
definition must provide all the necessary files before it can be
generated.

You can use tokens in the content that will be replaced when a widget
is generated. For example, you can write:

<p>Welcome to the ${widget.name}</p>

You can also call methods in javascript provided by any of the
templates used to create the widget, for example:

<form method="post" 
      onsubmit="javascript:${widget.shortname}_auth_controller.authenticate(username, password)">

FIXME - bad practice to have JS in HTML file - shoudl be added with load event or JQ/JQM mechanisms. As you point out above
can use tokens in JS

What tokens are available depends on the templates used, but in
generally anything that appears in the widget.properties file can be
used as a token.

*** CSS

**** Default CSS

Each template provides a minimum of CSS styles to allow widgets to be
consistent in their styling. Your own templates and widgets based on
these can override these styles as necessary, see below.

**** Media Queries

The root "default.widget.properties" file provides a number of useful
properties for performing media queries in your CSS and
Javascript. These properties define many common form factors in
various orientations. Please check the file for the full list.

To use these in your CSS simply include them as properties. for example:

#+BEGIN_SRC CSS
@media ${widget.media.screen.wide} {
/* Styles */
}
#+END_SRC

To use them in your javascript you can use the JQuery Mobile media function, for example:

#+BEGINE_SRC Javascript
if ($.mobile.media("${widget.media.screen.wide}")) {
  $('#searchPanel').trigger('expand');
}
#+END_SRC

**** Custom CSS

Each template can provide complete CSS files which will be copied into
widgets just like any other resources. In addition templates can add
to or modify CSS rules defined in parent templates.

To create additive CSS simply add files with names in the form of
"foo.add.css". The contents of these files will be added to the start
of a final CSS file called "all.css". 

The base template provides a link to the "all.css" in its "index.html" file. Therefore, if your template
should extends the base template you do not need to explicitly include "all.css". However, any additional
CSS files you provide will not be included automatically so you will need to provide a new "index.html" with
appropriate style references.

Note, there is a feature request to improve the handling of CSS provided by sub-templates or widgets see: 
https://issues.apache.org/jira/browse/WOOKIE-276

*** Scripts

Each template can provide one or more controller Javascript
definitions. The controller code is where the UI and the data 
model are wired together. 

Templates should provide any controller code in files named 
"PROJECTNAME_controller.js". When a widget is
generated from a template all its controller javascript files, along
with those defined in any parents, are concatenated into a single
"controller.js" file. This allows each template to define Javascript
controller objects.

It is important that
each controller object has a unique name. Therefore, we recommend
that you define your controllers using tokens that will be replaced
during widget generation. For example, the login template defines an 
"auth_controller.js" which provides a controller for authorisation 
activities. This file defines an object as follows:

var ${widget.shortname}_auth_controller = {
  basic_auth:function(username, password) {
    ...
  }
}

We then need to use the same token replacement approach when this
object is used. For example, calling the basid_auth function defined 
above is done with "${widget.shortname}_auth_controller.basic_auth(...)"

You can also provide other JS files by adding them to the scripts 
directory.

As with CSS the default index.html provided by the base template loads
the controller.js file. It does not load other JS you might provide in 
other templates. In order to to ensure these are loaded you need to 
provide a new index.html with the appropriate <script...> tags.

A feature request has been made to enhance this handling of Javascript,
see https://issues.apache.org/jira/browse/WOOKIE-277

*** Test widgets

When creating a new template you should also create a new test
widget. Test widgets are used to demonstrate and test the
functionality of widget templates.

To create a new test widget simply create a new directory in the
"testWidgets" directory, using a name of the form "fooTestWidget"
where "foo" is replaced by the name of the template under test. Then
copy the "widget.properties" file from the most appropriate existing
test widget (usually this will be one of the new templates parents). 

You'll need to edit, at least:

  * "template.name"
  * "widget.shortname"
  * "widget.name" properties. 

You will also need to add any new new properties and content
descriptions that are necessary for the widget to work.

**** Building test widgets

To test changes to the templates run "ant generate-test-widgets". This
will generate and deploy one or more test widgets using the latest
versions of the templates.

In order to make the build faster in a development process you can
define which templates are to be built using the "widget.include"
property discussed in the next section.

* Widget API
The base template provides a shim implementation of the widget API
used in Wookie (window.widget.*). This shim is only loaded if an api
object has not been injected by the host enviroment, a happens when
the widget is deployed by Wookie.

If you want to build a custom version of the widget for a given
platform you can provide your own implementation of the widget API by
injecting your own window.widget object at runtime or by providing
your own implementation in widget_api.js at build time.
