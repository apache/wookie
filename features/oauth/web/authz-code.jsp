<?xml version="1.0" encoding="UTF-8" ?>
<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">

	function proxify(url) {
		if (typeof window.opener != 'undefined' && typeof window.opener.widget != 'undefined')
			return window.opener.widget.proxyUrl + "?instanceid_key=" + window.opener.widget.instanceid_key + "&url=" + url;
		else
			return url;
	}
		
	function processToken(code, error) {
		if (typeof window.opener.oAuth == 'undefined') {
			alert('invalid function call');
			return;
		}
		
		if (code == 'null') code = null;
		if (error == 'null') error = null;
		if (code == null && error == null) {
			alert('error occurred');
			return;			
		}
		var errorDetail;
		var accessToken, expires;			
		if (code != null) {
			// show waiting frame
			document.getElementById('block-busy').style.display = 'block';
			document.getElementById('block-info').style.display = 'none';

			// triggering the POST request from oauth object to continue the process
			var oauthParams = window.opener.oAuth.oauthParams;		
			var xhrRequest = new XMLHttpRequest();
			xhrRequest.open('POST', proxify(oauthParams['tokenEndpoint']), false);
			xhrRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
			xhrRequest.setRequestHeader('Authorization', 'Basic ' + oauthParams['clientSecret']);
			xhrRequest.setRequestHeader('Accept', 'application/json');
			xhrRequest.onreadystatechange = function() {
				if (xhrRequest.readyState == 4) {
					if (xhrRequest.status == 200) {
						var result = JSON.parse(xhrRequest.responseText);
						if (typeof result.access_token != 'undefined') accessToken = result.access_token;
						if (typeof result.expires_in != 'undefined') expires = result.expires_in;
						if (expires == null) expires = 3600;	//defaut to 1h
					} else {
						error = 'error_unknown';
					}
				}
			}
			var payload = 'grant_type=authorization_code&client_id=' + oauthParams['clientId'] +
				'&client_secret=' + oauthParams['clientSecret'] +
				'&redirect_uri=' + oauthParams['redirectUri'] +
				'&code=' + code;
			xhrRequest.send(payload);			
		}
		
		// hide waiting frame
		document.getElementById('block-busy').style.display = 'none';
		
		// send token to parent window
		if (error == null) {
			document.getElementById('block-info').style.display = 'block';
			// close this window
			window.opener.oAuth.finishAuthProcess('success', accessToken, expires);
		} else {
			if (error == 'access_denied')
				errorDetail = 'The resource owner or authorization server denied the request.';
			else if (error == 'invalid_scope')
				errorDetail = 'The requested scope is invalid, unknown, or malformed.';
			else if (error == 'invalid_request')
				errorDetail = 'The request is missing a required parameter, includes an invalid parameter value, includes a parameter more than once, or is otherwise malformed.';
			else if (error == 'unauthorized_client')
				errorDetail = 'The client is not authorized to request an authorization code using this method.';
			else if (error == 'unsupported_response_type')
				errorDetail = 'The authorization server does not support obtaining an authorization code using this method.';
			else
				errorDetail = 'No detail information';
			document.getElementById('b-err-code').innerHTML = error;
			document.getElementById('b-err-msg').innerHTML = errorDetail;
			document.getElementById('block-error').style.display = 'block';			
			window.opener.oAuth.finishAuthProcess(error, errorDetail, null);
		}
	}
	
</script>
</head>
<body onload="javascript:processToken('<%= request.getParameter("code") %>' , '<%= request.getParameter("error") %>');">
    <div id="block-busy" style="display: none;">
    	<img alt="Processing ..." src="<%= request.getContextPath() %>/features/oauth/web/imgs/wait.gif"/>
    </div>

	<div id="block-info" style="display: none;">
		Please close this window.<br/>
		<button type="button" onclick="javascript:window.close();">Close</button>
	</div>

	<div id="block-error" style="display: none;">
		Error code: <span id="b-err-code"></span><br/>
		Error description: <br/>
		<span id="b-err-msg"></span><br/>
		<button type="button" onclick="javascript:window.close();">Close</button>
	</div>
</body>
</html>
