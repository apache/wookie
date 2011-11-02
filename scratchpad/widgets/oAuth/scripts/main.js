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

function showQuotes(quoteType) {
	jsonrpc_url = 'https://hogwarts.info.fundp.ac.be/google/quotes';
    if (window.oauth && typeof window.oauth.proxify == 'function'){
        jsonrpc_url = window.oauth.proxify(jsonrpc_url);
    }
	var request = {jsonrpc: '2.0',
		    method: 'getQuote',
			id: 'req-'+(++reqNr),
			params: {
				quotes: quoteType
			}
		};
	myXHR.open('POST', jsonrpc_url, true);
	myXHR.setRequestHeader('Content-Type', 'application/json');
	myXHR.onreadystatechange = handler;
	myXHR.send(JSON.stringify(request));
}

function handler(evtXHR) {
	if (myXHR.readyState == 4){
		if (myXHR.status == 200) {
			data = JSON.parse(myXHR.responseText);
			if(data.quote == null){
				notify('Widget instance is not authenticated, Please login');
			} else {
				notify('');
				document.getElementById('quoteBubble').innerHTML = '<a>' + data.quote + '</a>';
				document.getElementById('quotePortrait').innerHTML = '<img src="' + data.portrait + '">' + data.authorName + '</img>';
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
}