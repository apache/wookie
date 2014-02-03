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

/**
 * Provides a basic shim for opening the widget without a widget
 * object (e.g. directly in browsers)
 */
if (!window.widget){
    window.widget = {
        /**
         * Adds in the "proxify" method if it isn't in the widget
         * object, e.g. as we're opening the widget directly in a
         * browser, or using a widget runtime other than Wookie
         * e.g. Opera, PhoneGap etc
         */
        proxify:function(url) {
            return url;
        },

        openURL:function(url){
            window.widget.views.openUrl(url);
        }

    };
}

if (!window.widget.views){
    window.widget.views = {
        openUrl: function (url, navigateCallback, opt_viewTarget) {
            window.open(url, opt_viewTarget);      
            if (navigateCallback) {
                navigateCallback(window);
            };
        }    
    }
}

