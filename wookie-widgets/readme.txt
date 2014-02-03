This directory contains widget sourcecode and build files.

Create a widget
===============

There are three ways to create widgets, the first is to do it all by
hand. The second is to use a seed widget to provide the basic
structure of your widget and then edit that. The third is to use out
templating system that is designed to allow you to build widgets
without knowing much of how widgets are structured.

The easiest to get started with, if you know HTML and Javascript, is
probably to use the seed widgets. The templates system allows much
more reuse between widgets but brings with it a new set of rules and
configurations to learn.

This document describes using seeds to build widgets. To use the
templates see WOOKIE_HOME/widgets/template/readme.txt

Using Seeds to Create Widgets
=============================

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

cd $WOOKIE_HOME
ant build-widget

You will be asked for the name of the widget you wish to build. this name should be the
name of the folder containing the widget.

Your widget package will be created in the widgets/build directory.

Deploy a widget
===============

To deploy a widget to a running widget server use:

cd £WOOKIE_HOME
ant deploy-widget

You will be asked for the name of the widget you wish to build. this name should be the
name of the folder containing the widget.
 
Supplying the widget name
=========================

It can become tiresome having to enter the widget name repeatedly during the development cycle. 
For this reason you can supply it in the command, for example;

ant deploy-widget -Dwidget.shortname=FooWidget
