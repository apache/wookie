/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
 WidgetPreferences = new function WidgetPreferences(){
	this.prefs = {};
	this.length = 0;
	this.calcLength = function(){
		var x = 0;
		for (key in this.prefs) x++;
		this.length = x;
		return x;
	};
	this.key = function(n){
		var x=0;
	    for (key in this.prefs){
			if (x == n) return key;
			x++;
		};
	};
	this.getItem = function(key){
		if (!this.prefs[key]) return null;
		return this.prefs[key];
	};
	this.setItem = function(key,value){
		this.prefs[key] = value;
		Widget.setPreferenceForKey(key, value);
		this.calcLength();
	};
	this.removeItem = function(key){
		delete this.prefs[key];
		Widget.setPreferenceForKey(key,null);
		this.calcLength();
	};
	this.clear = function(){
		for (key in this.prefs){
			this.removeItem(key);
		}
	};
};

var Widget = {	
	instanceid_key : null,	
	preferences: null,
	init : function(){	
		var query = window.location.search.substring(1);
		var pairs = query.split("&");
		for (var i=0;i<pairs.length;i++){
			var pos = pairs[i].indexOf('=');
			if (pos >= 0){				
				var argname = pairs[i].substring(0,pos);
				if(argname=="idkey") this.instanceid_key = pairs[i].substring(pos+1);
			}
		}
		this.preferences = WidgetPreferences;
		dwr.engine.beginBatch();
		WidgetImpl.preferences(this.instanceid_key, this.setPrefs);
		dwr.engine.endBatch({async:false});
		dwr.engine.setActiveReverseAjax(false);	
		
	},
	setPrefs: function(map){
		this.preferences = WidgetPreferences;
		this.preferences.prefs = map;
		this.preferences.calcLength();
	},
	setPreferenceForKey : function(wName, wValue){
		WidgetImpl.setPreferenceForKey(this.instanceid_key, wName, wValue);	
	},
	preferenceForKey : function(wName, callBackFunction){
		WidgetImpl.preferenceForKey(this.instanceid_key, wName, callBackFunction);
	}
	
};