//############simple debug window########################

var winLength=800;
var winHeight=200;

var debugLog="";

function debug(p){
	result = cleanInput(p);
	debugLog+="<tr><td>";
	debugLog+=result;
	debugLog+="<hr/></td></tr>";
	writeConsole(debugLog);	
}

function cleanInput(p){
	removedOpenBracket = p.replaceAll( "<", "&lt;" )
	removedClosedBracket = removedOpenBracket.replaceAll( ">", "&gt;" )
	result = removedClosedBracket;
	return result;
}

function writeConsole(content) {
 consoleRef=window.open('','myconsole',
  'width=' + winLength + ',height=' + winHeight
   +',menubar=0'
   +',toolbar=1'
   +',status=0'
   +',scrollbars=1'
   +',resizable=1')
// top.consoleRef.document.open("text/html","replace");
 consoleRef.document.writeln(
  '<html><head>'
 // +'<script>function gotoAnchor(){var orig = this.location.href;location.replace(orig+"#bottom");}</script>'
  
  
 +'<title>Debug</title></head>'
 //+'<body bgcolor=white onLoad="gotoAnchor()"><table width=' + winLength + '>'
 +'<body bgcolor=white><table width=' + winLength + '>'
 +content
 +'<table><a name="bottom"></a></body></html>'
 )
 consoleRef.onLoad = consoleRef.scrollTo(0,999999);

 consoleRef.document.close()
}




 String.prototype.replaceAll = function(
 strTarget, // The substring you want to replace
 strSubString // The string you want to replace in.
 ){
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
//########################################