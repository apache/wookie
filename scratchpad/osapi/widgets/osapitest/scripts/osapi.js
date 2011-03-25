osapi.people.getViewer().execute(
		function(viewer){
			alert(viewer.getDisplayName());
		}
);


function setMonkey(){
	osapi.appdata.update({userId: '@viewer', data: {gifts: 'a crazed monkey'}}).execute(
		function(data){
			alert("added");
			getMonkey();
			deleteMonkey();
		}
	);	
}

function getMonkey(){
osapi.appdata.get({userId: '@viewer', groupId: '@friends', keys: ['gifts']}).execute(
		function(data){
			alert(data['gifts']);
		}
);
}

function deleteMonkey(){
osapi.appdata.Delete({keys: ['gifts']}).execute(
		function(){
			alert("deleted");
			getMonkey();
		}
);
}
setMonkey();
