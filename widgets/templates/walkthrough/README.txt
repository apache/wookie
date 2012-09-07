# Walkthrough

Walk through is a HTML app / widget that presents a series of linked questions.
Adiitional links in the question text are used to add items to a list of 
actions to be carried out. Some of the actions contain links to external web 
pages and some to internal notes (both open in a new tab). The Actions list
can be download as text and internal notes are included.

The questions are provided in a XML document and so can be easily changed. 
Questions are in HTML (using suitable escaping in the XML doc). Notes are
formated using the Creole wiki markup.

Walkthrough is a W3C widget and designed to be used with the Wookie widget 
server. It is completely client side and so requires a modern browser with
Javascript enabled. Becuase of current browser restricted access to local 
files the Save feature simply displays text to be copied by the operator.

# Notes:

* Questions can be referenced by id in a URI such as index.html#q6
* Notes can be referenced by id in a URI such as noteviewer.html#n1
* WAI ARIA roles have been used but not HTML5 landmark elements

# Creating a custom widget from the walkthrough template

## Building a widget to be built by the template system

See the wookie widget template system readme 
({wookie.root}/widgets/templates/readme.txt) for general notes. You'll need a 
minimum of the following (assuming we're creating a widget called 'walkabout')

+ workspace
  + widgets
    build.xml
    + walkabout
      widget.properties

widget.properties contains something like
    * template.name=walkthrough
    * widget.shortname=Walkabout
    * widget.name=Walkabout 
    * widget.description=Provides a way to explore information information 
      and track actions to be performed.

To build the walkabout widget run "ant generate-all-widgets" from within your 
widgets directory 

# Defining and accessing question sets

## Providing a set of questions to be baked into the widget

Place your question set in 'widgets/walkabout/xml/questions.xml and build the
widget. The DTD (format) and an example can be found in walkthrough template's
xml folder

+ workspace
  + widgets
    build.xml
    + walkabout
      widget.properties
      + xml
	    questions.xml

You may like to add a comment header section to the questions that contains 
your copyright notice.

## Accessing other questions

It is possible to access other question sets by URL. This is done by specifying
questions=URL in the querystring part of the URL used to invoke the widget. The
URL can be relative for questions included in the widget or absolute for 
questions located anywhere.

e.g. http://wookie.server/wookie/wservices/wookie.apache.org/widgets/walkabout/
index.html?questions=xml/more_questions.xml

# Using the widget

You can use the widget .wgt file by deploying it to a Apache Wookie server or 
another environment like Opera that can use them. Alternatively you can unzip
the archive (eg by renaming as *.zip or using 7Zip) and deploy the individual
files to a web server.
 
