
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
 
var widget;
 
/*
 * WidgetPreferences, singleton class
 * Calls to this object replace the legacy setPreferenceForKey/preferenceForKey methods
 * Implements HTML 5 Storage API
 */
 
var WidgetPreferences = new function WidgetPreferences() {

    /*
     * the internal preferences map
     */
    this.prefs = {};
    
    /*
     * the number of key/value pairs currently present in preferences
     */
    this.length = 0;
    
    /**
     * Resets the length attribute
     */
    this.calcLength = function () {
        var x = 0;
        for (var key in this.prefs) { 
            x++;
        }
        this.length = x;
        return x;
    };
    
    /**
     * Returns the tuple for the key
     * @param key the key to return the tuple for
     */
    this.key = function (n) {
        var x = 0;
        for (var key in this.prefs) {
            if (x == n) {
                return key;
            }
            x++;
        }
    };
    
    /**
     * Returns the value of a key
     * @param the key to return the value for
     */
    this.getItem = function (key) {
        if (!this.prefs[key]) {
            return null;
        }
        return this.prefs[key]["dvalue"];
    };
    
    /**
     * Create getters and setters for a preference
     * @param key the key to set
     * @param value the value to set (for IE)
     * @param pref the preference object to set (for IE)
     */
    this.createAccessorMethods = function (key, value, pref) {
        try {
            eval("Widget.preferences.__defineGetter__('" + key + "', function(){return Widget.preferences.getItem('" + key + "')})");
            eval("Widget.preferences.__defineSetter__('" + key + "', function(v){return Widget.preferences.setItem('" + key + "',v)})");
            eval("Widget.preferences.prefs[\"" + key + "\"]=pref;");
        } 
        catch (err) {

            //
            // cant use __defineGetter__ so try to setup for IE9
            //
            try {
                eval("Object.defineProperty(Widget.preferences,'" + key + "', {get: function get() { return Widget.preferences.getItem('" + key + "');},set: function set(value) {Widget.preferences.setItem('" + key + "',value)}});");
                eval("Widget.preferences.prefs[\"" + key + "\"]=pref;");
            } 
            catch (err2) {
                
                //
                // Catch IE 8 error. See WOOKIE-44
                //
                eval("Widget.preferences");
                    
                //
                // If eval went fine, populate with data, See WOOKIE-151
                //
                if (typeof Widget.preferences != "undefined") {
                    widget.preferences[key] = value;
                    widget.preferences.prefs[key] = pref;
                }
            }
        }
    };
    
    /**
     * Sets a key to a value
     * @param key the key to set
     * @param value the value to set
     */
    this.setItem = function (key, value) {
        //
        // Make a preference object and set its properties
        //
        var pref = {};
        var oldValue = null;
        pref.dvalue = value;
        pref.key = key;
        pref.readOnly = false;
   
        //
        // Check if the preference already exists
        //
        var existing = this.prefs[key];
        
        //
        // If it does exist, check it isn't read-only
        //
        if (existing) {
            oldValue = this.getItem(key);
            if (existing["readOnly"] === true) {
                window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR;
                throw (window.DOMException);
            }
        } else {
            //
            // Setup getters and setters for the new tuple
            //
            this.createAccessorMethods(key, value, pref);
        }
        
        //
        // Set the pref in the preferences collection
        // and persist it
        //
        this.prefs[key] = pref;
        widget.setPreferenceForKey(key, value);
        this.__raiseEvent(key, value, oldValue);
        //
        // Recalculate the length of the preferences collection
        //
        this.calcLength();
    };
    
    
    /**
     * Removes a tuple from the preferences collection
     * @param key the key of the tuple to remove
     */
    this.removeItem = function (key) {
        this.__removeItem(key, true);
    };
    
    /**
     * Internal delete function
     * @param key the key of the tuple to remove
     * @param fireEvent boolean; true to fire Storage Event on remove
     */
    this.__removeItem = function(key, fireEvent){
        var existing = (this.prefs[key]);
        if (existing) {
            if (existing["readOnly"] === true) {
                window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR;
                throw (window.DOMException);
            } else {
                var oldValue = this.getItem(key);
                delete this.prefs[key];
                widget.setPreferenceForKey(key, null);
                if (fireEvent) this.__raiseEvent(key, null, oldValue);
                this.calcLength();
            }
        }    
    };
    
    /**
     * Clears all tuples from the preferences collection
     */
    this.clear = function () {
        for (var key in this.prefs) {
            try {
                if(this.prefs[key]["readOnly"] === false)
                   this.__removeItem(key, false);
            } catch (e) {
                // swallow errors, as this method must never throw them according to spec.
            }
        }
        this.__raiseEvent(null,null,null);
    };
    
    this.__raiseEvent = function(key, value, oldValue){
        //
        // Raise custom storage event
        //  
        var evt;
        if(document.createEvent){
          evt = document.createEvent("Event");
        } else { // IE before v 9
          evt = document.createEventObject(window.event);
        }      
        var evt = document.createEvent("Event");
        evt.initEvent("storage", false,false);
        evt.key = key;
        evt.newValue = value;
        evt.oldValue = oldValue;
        evt.url      = window.location;
        evt.storageArea = widget.preferences;
        //
        // Dispatch to child frames
        //
        var frames = window.frames;
        for (var i=0;i<frames.length;i++){
          try{
            if(document.createEvent){
               frames[i].dispatchEvent(evt);  
            } else { // IE before v 9
               frames[i].fireEvent("storage", evt);
            }  
          } catch(err) {
            if (typeof console != "undefined") { 
                console.warn("error dispatching storage event to child frame; most likely this is due to same-origin restrictions");
            }
          }
        }   
    }
};

/**
 * Widget object
 */
var Widget = {

    /**
     * The ID key issued by wookie
     */
    instanceid_key : null,
    
    /**
     * The URL to the server-side proxy
     */
    proxyUrl : null,    
    
    /**
     * Method called whenever widget shared data is updated. Used by Wave feature to register its state change callback.
     */
    onSharedUpdate : null,
    
    /**
     * The preferences object; instantiated below
     */
    preferences: null,

    /**
     * Set up the widget object
     */
    init: function () {    
    
        //
        // This page url will be called with e.g. idkey=4j45j345jl353n5lfg09cw03f05
        // so grab that key to use as authentication against the server.
        // Also get the proxy address and store it.
        //
        var query = window.location.search.substring(1);
        var pairs = query.split("&");
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos >= 0) {                
                var argname = pairs[i].substring(0, pos);
                
                //
                // get the id_key and assign it to instanceid_key.
                //
                if (argname == "idkey") {
                    this.instanceid_key = pairs[i].substring(pos + 1);
                }
                
                //
                // TODO - remove this & use a callback instead of having it in the URL
                //
                if (argname == "proxy") {
                    this.proxyUrl = pairs[i].substring(pos + 1);
                }
            }
        }
        
        //
        // First, we need to refresh the security token. The initial token in the URL
        // is only usable for a short while.
        //
        this.refreshToken(false);
        
        //
        // Instantiate a Widget Preferences object, and load all values
        // Note we do this synchronously, as widgets are likely
        // to ask for a handle on this as an onLoad() event
        //
        this.preferences = WidgetPreferences;
        
        //
        // Load preferences using AJAX
        //
        this.loadPreferences();
        
        //
        // Load metadata using AJAX
        //
        this.loadMetadata();
 
    },
    
    /**
     * Load widget metadata from Wookie API
     */
    loadMetadata: function(){
        var xml_request = new XMLHttpRequest();
        xml_request.open("GET", "/wookie/metadata", false);
        xml_request.setRequestHeader("Authorization",this.instanceid_key);
        xml_request.onreadystatechange = function()
        {
            if(xml_request.readyState == 4 && xml_request.status == 200){
              var json = (JSON.parse(xml_request.responseText));
              Widget.setMetadata(json);
            }
        }
        xml_request.setRequestHeader("Cache-Control", "no-cache");
        xml_request.send(null);  
    },
    
    /**
     * Load preferences from Wookie API
     */
    loadPreferences: function(){
        var xml_request = new XMLHttpRequest();
        xml_request.open("GET", "/wookie/preferences", false);
        xml_request.setRequestHeader("Authorization",this.instanceid_key);
        xml_request.onreadystatechange = function()
        {
            if(xml_request.readyState == 4 && xml_request.status == 200){
              var json = (JSON.parse(xml_request.responseText));
              Widget.setPrefs(json.Preferences);
            }
        }
        xml_request.setRequestHeader("Cache-Control", "no-cache");
        xml_request.send(null);   
    },
    
    /**
     * Refreshes the widget security token from Wookie API
     * @param async whether to refresh the token asynchronously
     */
    refreshToken: function(async){
        var xml_request = new XMLHttpRequest();
        xml_request.open("POST", "/wookie/token", async);
        xml_request.setRequestHeader("Authorization",this.instanceid_key);
        xml_request.onreadystatechange = function()
        {
            if(xml_request.readyState == 4 && xml_request.status == 201){
              var json = (JSON.parse(xml_request.responseText));
              Widget.instanceid_key = json.token;
            }
        }
        xml_request.setRequestHeader("Cache-Control", "no-cache");
        xml_request.send(null);  
    },

    /**
     * Set the metadata collection for this widget instance
     * @param map the map of metadata items, consisting of a key and value
     */
    setMetadata: function (map) {
        for (var key in map) {
            Widget.setMetadataProperty(key, map[key]);
        }
    },

    /**
     * Set an individual metadata property.
     * @param key the metadata key
     * @param value the metadata value
     */
    setMetadataProperty: function (key, value) {
    
        //
        // The height and widget metadata items use Number values
        //
        if (key == "width" || key == "height") {
            value = Number(value);
        }
        
        //
        // Set up getters and setters; note that setters must return a 
        // NO_MODIFICATION_ALLOWED_ERR as metadata properties are read-only
        //
        try {
            Widget.__defineSetter__(key, function () {
                window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR; 
                throw (window.DOMException);
            });
            Widget.__defineGetter__(key, function () {
                return value;
            });
        } catch (err) {
            try {
                //
                // cant use __defineGetter__ so try to setup for IE9
                //
                Object.defineProperty(Widget, key, {
                    set: function set() {
                        window.DOMException.code = DOMException.NO_MODIFICATION_ALLOWED_ERR; 
                        throw (window.DOMException); 
                    },
                    get: function get() {
                        return value;
                    }
                });
            } catch (err2) {
                //
                // catch IE8
                //
                eval("Widget." + key + "='" + value + "';");
            }
        }
    },
    
    /**
     * Set the preferences collection for this widget instance
     * @param map the map of preference items, consisting of key, value and readonly flag
     */
    setPrefs: function (map) {
        this.preferences = WidgetPreferences;
        this.preferences.prefs = {};
        for (var i in map) {
            var obj = map[i];
            var key = obj["dkey"];
            
            //
            // Define getters and setters for each preference item
            //
            this.preferences.createAccessorMethods(key, obj["dvalue"], obj);
            
            //
            // Add the item to preferences collection
            //
            eval("this.preferences.prefs[\"" + key + "\"]=obj;");
        }
        this.preferences.calcLength();
        Widget.setMetadataProperty("preferences",this.preferences);
    },
    
    /**
     * Persist a preference object in the server backend
     */
    setPreferenceForKey : function (wName, wValue) {
        var xml_request = new XMLHttpRequest();
        xml_request.open("POST", "/wookie/preferences?name="+wName+"&value="+wValue, true);
        xml_request.setRequestHeader("Authorization",this.instanceid_key);
        xml_request.setRequestHeader("Cache-Control", "no-cache");
        xml_request.send(null);   
    },
    
    /**
     * Get the instance id key for this instance
     */
    getInstanceKey : function () {
        return this.instanceid_key;
    },
    
    /**
     * Get the URL for the server-side proxy
     */ 
    getProxyUrl : function () {
        return this.proxyUrl;
    },
        
    /**
     * Convert a given URL into a proxified version
     * @param url the URL to convert
     */
    proxify : function (url) {
        return this.proxyUrl + "?instanceid_key=" + this.instanceid_key + "&url=" + url;
    },
    
    /**
     * Return Widget as the object type
     */
    toString: function () {
        return "[object Widget]";
    }    
};

//
// Utility for getting an XMLHTTP object across different browsers
//
Widget.createXMLHTTPObject = function(){
    try{return new XMLHttpRequest();}catch(e){}try{return new ActiveXObject("Msxml3.XMLHTTP");}catch(e){}try{return new ActiveXObject("Msxml2.XMLHTTP.6.0");}catch(e){}try{return new ActiveXObject("Msxml2.XMLHTTP.3.0");}catch(e){}try{return new ActiveXObject("Msxml2.XMLHTTP");}catch(e){}try{return new ActiveXObject("Microsoft.XMLHTTP");}catch(e){}return null;
}

//
// Initialize the widget object
//
Widget.init();

//
// Avoid lower/uppercase confusion for the widget object
//
widget = Widget;

//
// Add widget object to the window object
//
window.widget = Widget;