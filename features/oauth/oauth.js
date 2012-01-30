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

oAuth = new function OAuth() {
	this.access_token = null;
	this.client_id = null;
	this.status = null; // null: init, O: not being authenticated, F: authentication failed, A: authenticated
	
	this.init = function() {
	
		token_bunch = window.location.hash;
		if (token_bunch.length > 0) {
			OAuthConnector.updateToken(
					widget.instanceid_key + token_bunch, 
					{callback: function(result) {
						if (result != "invalid") {
							window.opener.location.reload();
						}
					}, async: false});
			window.close();
		}
		dwr.engine.beginBatch();
		OAuthConnector.getClientId(widget.instanceid_key, this.setClientId);
		OAuthConnector.queryToken(widget.instanceid_key, this.setAccessToken);
		dwr.engine.endBatch({async: false});
	}
	
	this.setClientId = function(returned_client_id) {
		oAuth.client_id = returned_client_id;
	}
	
	this.setAccessToken = function(token_info) {
		if (token_info != "invalid") {
			oAuth.access_token = token_info;
			oAuth.status = "A";
		} else { 
			oAuth.status = "O";			
		}		
	}
	
	this.proxify = function(url) {
		returnedUrl = widget.getProxyUrl() + "?instanceid_key=" + widget.instanceid_key + "&url=" + url;
		if (oAuth.client_id != null && oAuth.access_token != null) {
			returnedUrl = returnedUrl + "&client_id=" + oAuth.client_id + "&access_token=" + oAuth.access_token;
		}
		return returnedUrl;
	}
	
	this.authenticate = function() {
		OAuthConnector.authenticate(
				widget.instanceid_key + "#" + window.location,
				{callback: function(redirectUrl) {
					window.open(redirectUrl, "authentication_popup", "width=500, height=400");
				}, async: false});
	}
	
	this.invalidateToken = function() {
		oAuth.status = "O";
		oAuth.access_token = null;
		OAuthConnector.invalidateToken(widget.instanceid_key);
	}
	
	this.showStatus = function(container_id) {
		if (oAuth.status == null || oAuth.status == "O") {
			document.getElementById(container_id).innerHTML = "Not yet authenticated";
		} else if (oAuth.status == "F") {
			document.getElementById(container_id).innerHTML = "Authentication failed";
		} else if (oAuth.status == "A") {
			document.getElementById(container_id).innerHTML = "Authenticated";
		}
	}
	
}

oAuth.init();
window.oauth = oAuth;