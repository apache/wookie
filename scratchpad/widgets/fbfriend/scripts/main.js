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
var reqNr = 0;
var myXHR = new XMLHttpRequest();

function getFirstFriendList(jsonrpc_url) {
    if (window.oauth && typeof window.oauth.proxify == 'function'){
        jsonrpc_url = window.oauth.proxify(jsonrpc_url);
    }
    jsonrpc_url = jsonrpc_url + '&limit=5';
        
	myXHR.open('GET', jsonrpc_url, true);
	myXHR.setRequestHeader('Content-Type', 'application/json');
	myXHR.onreadystatechange = handleFindFriend;
	myXHR.send();
}

function getNextFriendList(jsonrpc_url) {
    if (typeof window.widget.proxify == 'function'){
        jsonrpc_url = window.widget.proxify(jsonrpc_url);
    }
	myXHR.open('GET', jsonrpc_url, true);
	myXHR.setRequestHeader('Content-Type', 'application/json');
	myXHR.onreadystatechange = handleFindFriend;
	myXHR.send();
}

function logInOut() {
	if (window.oauth){
		if (window.oauth.status == "A") {
			if (typeof window.oauth.invalidateToken == 'function') {
				window.oauth.invalidateToken();
				document.getElementById("button-login").value = "Connect";
				document.getElementById('wookie-content').innerHTML = "";
			}
		} else {
			if (typeof window.oauth.authenticate == 'function') {
				window.oauth.authenticate();
				if (window.oauth.status == "A") {
					document.getElementById('button-login').value = 'Disconnect';
					document.getElementById('wookie-content').innerHTML = "";
				}
			}
		}
		window.oauth.showStatus("wookie-footer");
	}
}

function writeToWall(user_id) {
	var jsonrpc_url = 'https://graph.facebook.com/' + user_id + '/feed';
    if (window.oauth && typeof window.oauth.proxify == 'function'){
        jsonrpc_url = window.oauth.proxify(jsonrpc_url);
    }
    jsonrpc_url = jsonrpc_url + '&message=' + escape(document.getElementById('msg_' + user_id).value);
    var user_message = document.getElementById('msg_' + user_id).value;
    if (user_message.length == 0) {
    	notify('Please write something to send');
    	document.getElementById('msg_' + user_id).focus();
    	return;
    }
	myXHR.open('POST', jsonrpc_url, true);
	myXHR.setRequestHeader('Content-Type', 'application/json');
	myXHR.onreadystatechange = handleWriteToWall;
	myXHR.send();
}

function handleWriteToWall(evtXHR) {
	if (myXHR.readyState == 4){
		if (myXHR.status == 200) {
			result = JSON.parse(myXHR.responseText);
			if (typeof result.error  != 'undefined') {
				notify('Widget instance has not enough authorization to write on wall');
			} else {
				notify('Wall message is succesfully posted');
			}
		}
	}
}

function handleFindFriend(evtXHR) {
	if (myXHR.readyState == 4){
		if (myXHR.status == 200) {
			result = JSON.parse(myXHR.responseText);
			friendList = '<p>';
			if (typeof result.data  == 'undefined') {
				notify('Something goes wrong');
			} else {
				if (typeof result.paging.previous != 'undefined') {
					friendList = '<input type="submit" class="wookie-form-button" value="Prev" onclick="getNextFriendList(\'' + result.paging.previous + '\')"/>';
				}
				if (typeof result.paging.next != 'undefined') {
					friendList = friendList + '<input type="submit" class="wookie-form-button" value="Next" onclick="getNextFriendList(\'' + result.paging.next + '\')"/>';
				}
				friendList = friendList + '</p> Write a message to wall of your friends <table>';
				for (i=0; i<result.data.length; i++) {
					friendPhoto = '<img src="http://graph.facebook.com/' + result.data[i].id + '/picture" alt="friend avatar" height="42" width="42" />';
					friendName  = result.data[i].name 
								+ '<br/> <input type="text" id="msg_' + result.data[i].id + '" value=""/>'
								+ '<input type="submit" class="wookie-form-button" value="OK" onclick="writeToWall(\'' + result.data[i].id + '\')"/>' ;
					friendList = friendList + '<tr><td>' + friendPhoto + '</td><td>' + friendName + '</td></tr>';
				}
				friendList = friendList + '</table>';
				document.getElementById('wookie-content').innerHTML = '<p>' + friendList + '</p>';
			}

		} else {
			notify("Invocation Errors Occured. Status: " + myXHR.status);
		}
	}
}

function notify(message){
	if (window.widget && widget.showNotification){
		widget.showNotification(message, function(){widget.show()});
	} else {
		document.getElementById('notices').innerHTML = "<p>"+message+"</p>";
	}
	if (message.length > 0) setTimeout ("notify('')", 3000);
}