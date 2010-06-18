/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*
 * Replace text function - overloaded on strings
 */
 String.prototype.replaceAll = function(strTarget, strSubString){
	var strText = this;
	var intIndexOfMatch = strText.indexOf( strTarget );
  
	// Keep looping while an instance of the target string
	// still exists in the string.
	while (intIndexOfMatch != -1){
		// Relace out the current instance.
		strText = strText.replace( strTarget, strSubString )
		// Get the index of any next matching substring.
		intIndexOfMatch = strText.indexOf( strTarget );
	}
  
	// Return the updated string with ALL the target strings
	// replaced out with the new substring.
	return( strText );
 }

 
 /*
  * Utility class
  */
 var WidgetUtil = {
		 
	 generate3DigitRandomNumber : function(){
	 	return Math.floor((Math.random() * 900) + 100);
 	 },
		 
	 generateRandomHexColor : function(){
	 			colors = new Array(14)
				colors[0]="0"
				colors[1]="1"
				colors[2]="2"
				colors[3]="3"
				colors[4]="4"
				colors[5]="5"
				colors[5]="6"
				colors[6]="7"
				colors[7]="8"
				colors[8]="9"
				colors[9]="a"
				colors[10]="b"
				colors[11]="c"
				colors[12]="d"
				colors[13]="e"
				colors[14]="f"

				digit = new Array(5)
				color=""
				for (i=0;i<6;i++){
					digit[i]=colors[Math.round(Math.random()*14)]
					color = color+digit[i]
				}
				return color;
			},
			
			findObj : function (n, d) {
				var p,i,x; if(!d) d=document;
				if((p=n.indexOf("?"))>0&&parent.frames.length) {
					d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);
				}
				if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++)
					x=d.forms[i][n];
					for(i=0;!x&&d.layers&&i<d.layers.length;i++)
						x=findObj(n,d.layers[i].document);
					if(!x && document.getElementById) x=document.getElementById(n); return x;
			},
			
			roundNumber : function (num, dec) {
				var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
				return result;
			},
			
			onReturn : function(event, sendMessage){
				dwr.util.onReturn(event, sendMessage)
			},
			
			getValue : function(fName){				
				return dwr.util.getValue(fName);
			},
		
			setValue : function(fName, fValue, options){				
				dwr.util.setValue(fName, fValue, options);
			},
		
			escapeHtml : function(text){
				return dwr.util.escapeHtml(text);
			},
			
			byId : function(id){
				return dwr.util.byId(id);
			}
 }
 
/*
 * simple debug window
 */
var DebugHelper = {
		
	winLength : 800,
	winHeight : 200,
	debugLog : "",
	
	debug : function(p){
		result = this.cleanInput(p);
		this.debugLog+="<tr><td>";
		this.debugLog+=result;
		this.debugLog+="<hr/></td></tr>";
		this.writeConsole(this.debugLog);	
	},

	cleanInput : function(p){
		removedOpenBracket = p.replaceAll( "<", "&lt;" )
		removedClosedBracket = removedOpenBracket.replaceAll( ">", "&gt;" )
		result = removedClosedBracket;
		return result;
	},

	writeConsole : function(content) {
		 consoleRef=window.open('','myconsole',
		  'width=' + this.winLength + ',height=' + this.winHeight
		   +',menubar=0'
		   +',toolbar=1'
		   +',status=0'
		   +',scrollbars=1'
		   +',resizable=1')	
		   
		 consoleRef.document.writeln(
		  '<html><head>'		  		  
		 +'<title>Debug</title></head>'
		 +'<body bgcolor=white><table width=' + this.winLength + '>'
		 +content+'<table><a name="bottom"></a></body></html>'
		 )
		 consoleRef.onLoad = consoleRef.scrollTo(0,999999);
		 consoleRef.document.close()
	}

}
