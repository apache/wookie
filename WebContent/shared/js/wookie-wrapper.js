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
 
 /*
 * WidgetPreferences, singleton class
 * Calls to this object replace the legacy setPreferenceForKey/preferenceForKey methods
 * Implements HTML 5 Storage API
 */
 
WidgetPreferences = new function WidgetPreferences(){
	// the internal preferences map
	this.prefs = {};
	// SPEC: The length attribute must return the number of key/value pairs currently present in the list associated with the object.
	this.length = 0;
	// Resets the length attribute
	this.calcLength = function(){
		var x = 0;
		for (key in this.prefs) x++;
		this.length = x;
		return x;
	}
	this.key = function(n){
		var x=0;
	    for (key in this.prefs){
			if (x == n) return key;
			x++;
		};
	}
	this.getItem = function(key){
		if (!this.prefs[key]) return undefined;
		return this.prefs[key]["dvalue"];
	}
	this.setItem = function(key,value){
        // Make a preference object
        var pref = {};
        pref.dvalue = value;
        pref.key = key;
        pref.readOnly = false;
        
	    var existing = this.prefs[key];
        if (existing){
            if (existing["readOnly"] == true){
            	window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR;
            	throw (window.DOMException);
            }
        } else {
        	// Setup prototype methods
        	try{
        		eval("Widget.preferences.__defineGetter__('"+key+"', function(){return Widget.preferences.getItem('"+key+"')})");
        		eval("Widget.preferences.__defineSetter__('"+key+"', function(v){return Widget.preferences.setItem('"+key+"',v)})");
        		eval("Widget.preferences.prefs."+key+"=pref");
            }
            // Catch IE 8 error. See WOOKIE-44 
            catch(err){
            	eval("Widget.preferences." + key + "='" + value + "'");
				eval("Widget.preferences.prefs." + key + "=pref");
            }
        }
		this.prefs[key] = pref;
		Widget.setPreferenceForKey(key, value);
		this.calcLength();
	}
	this.removeItem = function(key){
		var existing = (this.prefs[key]);
        if (existing){
            if (existing["readOnly"] == true){
            	window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR;
            	throw (window.DOMException);
            } else {
            	delete this.prefs[key];
            	Widget.setPreferenceForKey(key,null);
            	this.calcLength();
            }
        }
	}
	this.clear = function(){
		for (key in this.prefs){
			try{
				this.removeItem(key);
			} catch (e){
				// swallow errors, as this method must never throw them according to spec.
			}
		}
	}
}

/*
 * Widget object
 */
var Widget = {	
	instanceid_key : null,	
	proxyUrl : null,	
	// this should be assigned by the calling JS app
	onSharedUpdate : null,
	// this should be assigned by the calling JS app
	onLocked : null,
	// this should be assigned by the calling JS app
	onUnlocked : null,
	// initialised below as a singleton
	preferences: null,

	init : function(){	
		/*
		 * This page url will be called with e.g. idkey=4j45j345jl353n5lfg09cw03f05
		 * so grab that key to use as authentication against the server.
		 * Also get the proxy address and store it.
		 */
		var query = window.location.search.substring(1);
		var pairs = query.split("&");
		for (var i=0;i<pairs.length;i++){
			var pos = pairs[i].indexOf('=');
			if (pos >= 0){				
				var argname = pairs[i].substring(0,pos);
				if(argname=="idkey"){
					// This gets the id_key and assigns it to instanceid_key.
					this.instanceid_key = pairs[i].substring(pos+1);
				}
				//TODO - remove this & use a callback instead of having it in the URL
				if(argname=="proxy"){
					this.proxyUrl = pairs[i].substring(pos+1);
				}
			}
		}	
		
		// Instantiate a Widget Preferences object, and load all values
		// Note we do this synchronously, as widgets are likely
		// to ask for a handle on this as an onLoad() event
		this.preferences = WidgetPreferences;
		dwr.engine.beginBatch();
		WidgetImpl.preferences(this.instanceid_key, this.setPrefs);
		WidgetImpl.metadata(this.instanceid_key, this.setMetadata);
		dwr.engine.endBatch({async:false});
	},
	
	setMetadata: function(map){
		Widget.id = map["id"];
		Widget.author = map["author"];
		Widget.authorEmail = map["authorEmail"];
		Widget.authorHref = map["authorHref"];
		Widget.name = map["name"];
		Widget.shortName = map["shortName"];
		Widget.description = map["description"];
		Widget.version = map["version"];
		Widget.height = parseInt(map["height"]);
		Widget.width = parseInt(map["width"]);
	},
	
	setPrefs: function(map){
		this.preferences = WidgetPreferences;
		this.preferences.prefs = {};
		for (i in map){
            obj = map[i];
            key = obj["dkey"];
            try{
				eval("Widget.preferences.__defineGetter__('"+key+"', function(){return Widget.preferences.getItem('"+key+"')})");
            	eval("Widget.preferences.__defineSetter__('"+key+"', function(v){return Widget.preferences.setItem('"+key+"',v)})");
            	eval("this.preferences.prefs."+key+"=obj");
            }
            // Catch IE 8 error. See WOOKIE-44
            catch(err){
            	eval("Widget.preferences.setItem('" + key + "','" + obj["dvalue"] + "')");
            	eval("Widget.preferences.getItem('" + key + "') == '" + obj["dvalue"] + "'");
            }
		}
		this.preferences.calcLength();
	},
	
	setPreferenceForKey : function(wName, wValue){
		WidgetImpl.setPreferenceForKey(this.instanceid_key, wName, wValue);	
	},

	preferenceForKey : function(wName, callBackFunction){
		WidgetImpl.preferenceForKey(this.instanceid_key, wName, callBackFunction);
	},
	
	setSharedDataForKey : function(wName, wValue){
		WidgetImpl.setSharedDataForKey(this.instanceid_key, wName, wValue);
	},
	
	sharedDataForKey : function(wName, callBackFunction){
		WidgetImpl.sharedDataForKey(this.instanceid_key, wName, callBackFunction)
	},
	
	appendSharedDataForKey : function(wName, wValue){
		WidgetImpl.appendSharedDataForKey(this.instanceid_key, wName, wValue)		
	},	
	
	lock : function(){
		WidgetImpl.lock(this.instanceid_key);
	},
	
	unlock : function(){
		WidgetImpl.unlock(this.instanceid_key);
	},
	
	hide : function(){
		WidgetImpl.hide(this.instanceid_key);
	},
	
	show : function(){
		WidgetImpl.show(this.instanceid_key);
	},
	
	openURL : function(url){
		window.open(url);
	},
	
	getInstanceKey : function(){
		return this.instanceid_key;
	},
	
	getProxyUrl : function(){
		return this.proxyUrl;
	},
		
	proxify : function(url){
			return this.proxyUrl + "?instanceid_key=" + this.instanceid_key + "&url=" + url;
	},
	
	toString: function(){
		return "[object Widget]";
	}
	
}
// very important !
Widget.init();
widget = Widget;
window.widget = Widget;





