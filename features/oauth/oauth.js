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
	this.status = null; // null: init, O: not being authenticated, F: authentication failed, A: authenticated
	this.oauthParams = new Object();
	this.authCallback = null;
	this.popup = null;
	
	this.init = function() {
		var info = new Object();
		info['id_key'] = widget.instanceid_key;
		info['url'] = window.location.href;
		OAuthConnector.queryOAuthParams(info, 
				{callback: function(map) {
			        oAuth.oauthParams = map;
				}, async: false});
		// if persist enalbed, try to get acess token
		if (oAuth.oauthParams['persist'] != 'false') {
			OAuthConnector.queryToken(widget.instanceid_key, 
					{callback: function(accessToken) {
						oAuth.setAccessToken(accessToken);
					}, async: false});
		}
	}

	// errorCode: 
	//	* success
	//		p1: access_token, p2: expires time
	//	* other
	//		p1: error description	
	this.finishAuthProcess = function(error_code, p1, p2) {
		var jResult = new Object();
		if (error_code == 'success') {
			// set access token to member variables
			oAuth.setAccessToken(p1);
			// update to db if persist type
			if (oAuth.oauthParams['persist'] != 'false') {
				OAuthConnector.updateToken(
						'id_key=' + widget.instanceid_key + '&access_token=' + p1 + '&expires_in=' + p2, 
						{callback: function(result) {
							return;
						}, async: false});
			}
			jResult['error'] = 'success';
			if (oAuth.popup != null) oAuth.popup.close();
		} else {
			jResult['error'] = error_code;
			jResult['desc'] = p1;
			if (oAuth.authCallback != null && oAuth.popup != null)
				oAuth.popup.close();
		}
		
		if (oAuth.authCallback != null) {
			window.setTimeout(function() { 
				oAuth.authCallback(jResult);
			}, 5);
		}
	}
	
	this.setAccessToken = function(token) {
		if (token != 'invalid') {
			oAuth.access_token = token;
			oAuth.status = 'A';
		} else { 
			oAuth.status = 'O';			
		}		
	}
	
	this.proxify = function(url) {
		returnedUrl = widget.getProxyUrl() + '?instanceid_key=' + widget.instanceid_key + '&url=' + url;
		if (oAuth.access_token != null) {
			returnedUrl = returnedUrl + '&access_token=' + oAuth.access_token;
		}
		return returnedUrl;
	}
	
	this.authenticate = function(fCallback) {
		// check if persist
		if (oAuth.oauthParams['persist'] != 'false') {
			OAuthConnector.queryToken(widget.instanceid_key, 
					{callback: function(accessToken) {
						oAuth.setAccessToken(accessToken);
					}, async: false});
			
			if (oAuth.status == 'A') return;
		}
		
		// check oauth profile
		if (typeof oAuth.oauthParams['profile'] != 'undefined') {
			if (oAuth.oauthParams['profile'] != 'implicit' && oAuth.oauthParams['profile'] != 'authorization code') {
				alert(oAuth.oauthParams['profile'] + ' is not supported in this version');
				return;
			}
		}
		// show popup window
		var url = oAuth.oauthParams['authzServer'];
		if (oAuth.oauthParams['profile'] == 'implicit') {
			url += '?response_type=token&client_id=' + oAuth.oauthParams['clientId'] + 
			'&redirect_uri=' + oAuth.oauthParams['redirectUri'];
		} else if (oAuth.oauthParams['profile'] == 'authorization code') {
			url += '?response_type=code&client_id=' + oAuth.oauthParams['clientId'] + 
			'&redirect_uri=' + oAuth.oauthParams['redirectUri'];			
		}

		if (typeof oAuth.oauthParams['scope'] != 'undefined') {
			url += '&scope=' + oAuth.oauthParams['scope']; 
		}
		
		this.authCallback = fCallback;
		
		oAuth.popup = window.open(url, 'Authorization request', 
				'width=' + oAuth.oauthParams['popupWidth'] + ', height=' + oAuth.oauthParams['popupHeight']);
	}
	
	this.invalidateToken = function() {
		oAuth.status = 'O';
		oAuth.access_token = null;
		OAuthConnector.invalidateToken(widget.instanceid_key);
	}
	
	this.showStatus = function(container_id) {
		if (oAuth.status == null || oAuth.status == 'O') {
			document.getElementById(container_id).innerHTML = 'Not yet authenticated';
		} else if (oAuth.status == 'F') {
			document.getElementById(container_id).innerHTML = 'Authentication failed';
		} else if (oAuth.status == 'A') {
			document.getElementById(container_id).innerHTML = 'Authenticated';
		}
	}	
}

oAuth.init();
window.oauth = oAuth;

