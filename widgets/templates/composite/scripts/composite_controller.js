/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The ${widget.shortname}_composite_controller object
 * This is used to wire up the view and model with actions
 */ 
var ${widget.shortname}_Controller = {
    init:function() {
    	var widgets = ${composite.widgets}; 
        var properties = [{widget:"http://wookie.apache.org/widgets/freeder", key:"feedURL", value:"http://apache.markmail.org/atom/wookie-dev"}];
        ${widget.shortname}_WidgetLoader.init(widgets,properties);
    }
}

/**
 * Initialise page layout
 */
$(window).load(function(event) {
   ${widget.shortname}_Controller.init(); 
});
