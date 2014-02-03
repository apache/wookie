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

var ${widget.shortname}_Picker = {
	curr : 0,

	next : function() {
		${widget.shortname}_Picker.curr++;
		if (${widget.shortname}_Picker.curr >= ${widget.shortname}_${widget.shortname}_WidgetLoader.widgets.length)
			${widget.shortname}_Picker.curr = 0;
		${widget.shortname}_Picker.select(${widget.shortname}_Picker.curr);
	},

	prev : function() {
		${widget.shortname}_Picker.curr--;
		if (${widget.shortname}_Picker.curr < 0)
			${widget.shortname}_Picker.curr = ${widget.shortname}_WidgetLoader.widgets.length - 1;
		${widget.shortname}_Picker.select(${widget.shortname}_Picker.curr);
	},

	select : function(id) {
		/* $("#current-widget-title").text(${widget.shortname}_WidgetLoader.widgets[id].title); */
		$('.widget').hide();
		$("#" + id).show();
	},
};