<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
	function getHashParam(paramName) {
	    return (RegExp('[#&]' + paramName + '=' + '(.+?)(&|$)').exec(location.hash) || [,null])[1];
	}	
	
	function processToken() {
		if (typeof window.opener.oAuth == 'undefined') {
			alert('invalid function call')
			return;
		}
		
		// show waiting frame
		document.getElementById('block-busy').style.display = 'block';
		document.getElementById('block-info').style.display = 'none';
		
		// get access token + timeout in fragment
		var accessToken = getHashParam('access_token');
		var expires = getHashParam('expires_in');
				
		// send token to parent window
		window.opener.oAuth.initAccessToken(accessToken, expires);

		// hide waiting frame
		document.getElementById('block-busy').style.display = 'none';
		document.getElementById('block-info').style.display = 'block';
		
		// close this window
		window.close();
	}
</script>
</head>
<body onload="javascript:processToken();">    
    <div id="block-busy" style="visibility: hidden;">
    	<img alt="Processing ..." src="<%= request.getContextPath() %>/features/oauth/web/imgs/wait.gif"/>
    </div>

	<div id="block-info" style="visibility: hidden;">
		Please close this window.
	</div>
</body>
</html>