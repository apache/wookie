
function twidget()
{
   // this.myData = 5;
   // this.myString = "Hello World";
    //this.ShowData = DisplayData;
    this.system = dosystem;
    this.preferenceForKey = getPrefernceForKey;
      this.setPreferenceForKey = setPrefernceForKey;
}

function getPrefernceForKey(p1){
	return "pref";
}

function setPrefernceForKey(p1, p2){
	//return "pref";
}

function dosystem()
{
    return "not supported";
}

function dosystem(p1)
{
//alert("1"+p1);
    return "not supported";
}


function dosystem(p1,p2)
{
//alert("2:"+ p1+p2);
    return "not supported";
}


function DisplayString()
{
//alert("3");
    alert( this.myString ); 
}

var widget = new twidget();


/*
myClassObj1.myData = 10;
myClassObj1.myString = "Obj1:  Hello World";
myClassObj2.myData = 20;
myClassObj2.myString = "Obj2:  Hello World";
myClassObj1.ShowData();     // displays: 10
myClassObj1.ShowString();   // displays: "Obj1:  Hello World"
myClassObj2.ShowData();     // displays: 20
myClassObj2.ShowString();   // displays: "Obj2:  Hello World"
*/