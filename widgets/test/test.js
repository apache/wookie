var attributeTest = [];
attributeTest['id'] = "http://www.getwookie.org/widgets/test";
attributeTest['author'] = "Scott Wilson";
attributeTest['authorEmail'] = "scott.bradley.wilson@gmail.com";
attributeTest['authorHref'] = "http://www.cetis.ac.uk/members/scott";
attributeTest['name'] = "API Test";
attributeTest['description'] = "A W3C API Testing Widget";
attributeTest['version'] = "1.0";
attributeTest['width'] = "320";
attributeTest['height'] = "520";

function test_cases(){
    testAttributes();
    testPreferences();
}

function testAttributes(){
    var idx = 1
    for (t in attributeTest){
        var testid = "TA"+idx;
        var testname = "access attribute:"+t;
        var expected = attributeTest[t];
        var test = new Function("return widget."+t);
        testFunction(testid,test, testname,expected);
        idx ++;
    }
}

function testPreferences(){
    // test a non-existing preference
    testPreferenceValue("TP1", "preferences.getitem(Non-existing preference)","non-existing","null");
    // set a value
    widget.preferences.setItem("newPref","new value");
    testPreferenceValue("TP2", "preferences: setItem/getItem","newPref","new value");
    // test preference defaults
    testPreferenceValue("TP3", "preferences: defaults","default_one","1");    
    testPreferenceValue("TP4", "preferences: defaults[2]","default_two","2");    
    // set and remove a value
    //widget.preferences.setItem("testremove","testremove");
    //widget.preferences.removeItem("testremove");
    //testPreferenceValue("TP5", "preferences: remove","testremove","null");
    // access using "nice" api
    widget.preferences.setItem("nice", "smashing");
    testNicePreferenceValue("TP6","preferences: set new pref, get with 'widget.preferences.nice'","nice", "smashing");
    testNicePreferenceValue("TP7", "preferences: get default pref with 'widget.preferences.default_one'","default_one","1");
    // read-only access
    addTestRow("TP8","preferences: set read only default pref with 'widget.preferences.default_one=7'");
    try{
        widget.preferences.default_one = 7; 
        fail("TP8");
    } catch(e){
        printValue("TP8", e);
        pass("TP8");
    }
    addTestRow("TP9","preferences: set read only default pref with setItem()");
    try{
        widget.preferences.setItem("default_one", 7); 
        fail("TP9");
    } catch(e){
        printValue("TP9", e);
        pass("TP9");
    }
}

function testPreferenceValue(testid, testname, pref, expected){
    testFunction(testid, new Function("return widget.preferences.getItem('"+pref+"')"), testname, expected);
}

function testNicePreferenceValue(testid, testname, pref, expected){
    testFunction(testid, new Function("return widget.preferences."+pref), testname, expected);
}

////////////

function testFunction(testid, test, testname, expected){
    addTestRow(testid,testname);
    var value = test();
    if (!value || value === null || value == '') value = 'null';
    printValue(testid, value);
    if (value == expected){
        pass(testid);
    } else {
        fail(testid);
    }
}


function printValue(testid, value){
    var elem = document.getElementById(testid+"Value");
    elem.innerHTML = value;
}

function addTestRow(testid, testname){
    var tableElem = document.getElementById("tests");
    var tableBody = tableElem.tBodies[0];
    var newRow = tableBody.insertRow(-1);
    var testCell = newRow.insertCell(0);
    testCell.innerHTML = testname;
    testCell.id = testid;
    var resultCell = newRow.insertCell(1);
    resultCell.id = testid+"Value";
}

function pass(pref){
        var elem = document.getElementById(pref+"Value"); 
        //elem.innerHTML = "PASSED";
        elem.className = "pass";
}

function fail(pref){
        var elem = document.getElementById(pref+"Value"); 
        //elem.innerHTML = "FAILED";
        elem.className = "fail";
}
