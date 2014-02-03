<%
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
%>

var ${widget.shortname}_images_controller = { 
    init:function() { 
        var albums = {
            "places": {
                "title": "Places",
                "preview": "images/places/032.jpg",
                "assets": [
                    {
	                "src":"images/places/032.jpg"
	            },
                    {
	                "src":"images/places/042.jpg"
	            },
                    {
	                "src":"images/places/eden project.jpg"
	            },
                    {
	                "src":"images/places/edenProject.jpg"
	            }
                ]
            },
            "beeKeeping": {
                "title": "Bees",
                "preview": "images/beeKeeping/3045715262_acbb1f9b60_z.jpg",
                "assets": [
                    {
	                "src":"images/beeKeeping/3045715262_acbb1f9b60_z.jpg"
	            },
                    {
	                "src":"images/beeKeeping/BeeFindsPollen.jpg"
	            }
                ]
            },
            "vehicles": {
                "title": "Vehicles",
                "preview": "images/vehicle/11919_wpm_hires.jpg",
                "assets": [
                    {
	                "src":"images/vehicle/11919_wpm_hires.jpg"
	            },
                    {
	                "src":"images/vehicle/14021_wpm_hires.jpg"
	            },
                    {
	                "src":"images/vehicle/7863_wpm_hires.jpg"
	            },
                ]
            }
        }
	${widget.shortname}_asset_controller.setAlbums(albums);
	${widget.shortname}_scanning_controller.scanElements = $('[data-scanOrder]');
    },

};

$('#home').live('pageinit',function(event) { 
  ${widget.shortname}_images_controller.init(); 
});
