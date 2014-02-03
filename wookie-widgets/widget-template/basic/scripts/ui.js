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
 * Helper methods for manipulating the widget UI.
 */


function wookieShowSettings(event) {
	var elemContent = document.getElementById("wookie-content");
	var elemSettings = document.getElementById("wookie-settings");
	
	var elemContentProperty = document.getElementById("wookie-template-property-content");
	elemContentProperty.value = elemContent.innerHTML;
	
	elemContent.style.display="none";
	elemSettings.style.display="block";
}

function wookieShowMain(event) {
	var content = Widget.preferences.getItem("content");
	var elemContent = document.getElementById("wookie-content");
	elemContent.innerHTML = content;
	
	var elemSettings = document.getElementById("wookie-settings");
	elemContent.style.display="block";
	elemSettings.style.display="none";	
}
