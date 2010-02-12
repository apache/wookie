/**
 * The Line class
 */ 
function Line(id,path,user){
    this.id = id;
    this.path = path;
    this.user = user;
}

//// Instance methods

// Save
Line.prototype.save = function(){
	if (typeof(wave) != "undefined") wave.getState().submitValue(this.id, JSON.stringify(this));
}

//// Static methods

Line.create = function(json){
		var obj = JSON.parse(json);
        var line = new Line(obj.id, obj.path,obj.user);
        return line;
}

// Find
Line.find = function(id){
        var keys = wave.getState().getKeys();
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            if (key == task_id){
                return Line.create(wave.getState().get(key));
            }
        }
        return null;
}

// Find all
Line.findAll = function(){
    var lines = {};
	if (typeof(wave) != "undefined"){
        var keys = wave.getState().getKeys();
        for (var i = 0; i < keys.length; i++) {
        	var key = keys[i];
        	var line = Line.create(wave.getState().get(key));
        	lines[key] = line;
        }
    }
    return lines;
    
}

// Make a guid for a new object
Line.guid = function(){
    var uid = function() {return (((1+Math.random())*0x10000)|0).toString(16).substring(1);}
    return uid()+uid()+"-"+uid()+"-"+uid()+"-"+uid()+"-"+uid()+uid()+uid();
}

function rnd_no(max){
    return Math.floor(Math.random()*max);
};

/**
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {

    // Event handler
    // Update the view when state has been updated
    stateUpdated: function(){
        var lines = Line.findAll();
        if (lines && lines != null){
            for (key in lines) {
                var line = lines[key]; 
                var start = line.path[0].split(":");
                context.beginPath();
                context.moveTo(start[0],start[1]);
                for (point in line.path){
                    var coords = line.path[point].split(":");
                    context.lineTo(coords[0],coords[1]);
                    context.stroke();
                    //context.moveTo(coords[0],coords[1]);
                }
                context.closePath();
                
                   // context.fillRect  (start[0],   start[1], start[0]+150, 150);
             }
        }
    },
    
    participantsUpdated: function(){
    },
    
    init: function(){
        init_canvas();
    	Controller.participantsUpdated();
    	Controller.stateUpdated();
    	/**
    	 * Register the event handlers with the Wave feature
    	 */
    	if (typeof(wave) != "undefined"){
    		wave.setStateCallback(Controller.stateUpdated);
    		wave.setParticipantCallback(Controller.participantsUpdated);
    	}
    }
}