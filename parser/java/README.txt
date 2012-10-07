Wookie W3C Widget Parser
========================

This is the standalone parser package for W3C Widgets developed as part of Apache Wookie (incubating).

Usage
=====

To process a W3C Widget (.wgt) file, instantiate a W3CWidgetFactory as follows:

W3CWidgetFactory fac = new W3CWidgetFactory();
fac.setOutputFolder = my_output_dir; // folder on the local file system widgets will be saved into 
fac.setStartPageProcessor = new MyStartPageProcessorImpl(); // class that injects scripts or does any other start file post-processing 
fac.setLocales = new String[]{"en","fr","nl"}; // supported locales 
fac.setLocalPath = "/deploy"; // local base path for widget URL 
fac.setFeatures = new String[]{"http://wave.google.com"}; // supported features
W3CWidget widget = fac.parse(new File(my_widget_package_file));

