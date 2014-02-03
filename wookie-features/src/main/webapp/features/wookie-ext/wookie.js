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


window.widget.appendSharedDataForKey = function(wName, wValue){
    WookieImpl.appendSharedDataForKey(widget.instanceid_key, wName, wValue);	
};	

window.widget.preferenceForKey = function(wName, callBackFunction){
    WookieImpl.preferenceForKey(widget.instanceid_key, wName, callBackFunction);
};

window.widget.setSharedDataForKey = function(wName, wValue){
    WookieImpl.setSharedDataForKey(widget.instanceid_key, wName, wValue);
};

window.widget.sharedDataForKey = function(wName, callBackFunction){
    WookieImpl.sharedDataForKey(widget.instanceid_key, wName, callBackFunction)
};

window.widget.lock = function(){
    WookieImpl.lock(widget.instanceid_key);
};
	
window.widget.unlock = function(){
    WookieImpl.unlock(widget.instanceid_key);
};

window.widget.hide = function(){
    WookieImpl.hide(widget.instanceid_key);
};

window.widget.show = function(){
    WookieImpl.show(this.instanceid_key);
};

window.widget.openURL = function(url){
    window.open(url);
};

