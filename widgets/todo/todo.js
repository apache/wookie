/**
 * The Task class
 * This is the model used to work with tasks
 */ 
function Task(id,name,status,assigned){
    this.task_id = id;
    this.name = name;
    this.status = status;
    this.assigned_to = assigned;
}

//// Instance Task methods

// Save task
Task.prototype.save = function(){
	wave.getState().submitValue(this.task_id, JSON.stringify(this));
}
// Get the person the task is assigned to
Task.prototype.getUser = function(){
    var user = {};
    // Unassigned
    if (this.assigned_to == "unassigned"){
        user.username="unassigned";
        user.thumbnail="noone.png";
        return user;
    }
    // Known participant
    var participants = wave.getParticipants();
    var participant = null;
    for (key in participants){
        if (participants[key].getId() == this.assigned_to){
        	participant = participants[key];
        }
    }
    if (participant != null){
        user.id = participant.getId();
        user.username = participant.getDisplayName();
        user.thumbnail  = participant.getThumbnailUrl();
    }
    // Unknown participant
    if (user.username == null || user.username == ""){
        user.username = "anonymouse";        
        user.id = "anonymous";
    }
    // Default thumbnail image
    if (user.thumbnail == "" || user.thumbnail == null) user.thumbnail = "anon.png";
    return user;
}
    
//// Static Task methods

// The delimiter used for the data
Task.delimiter = "||||";


// Private method for creating new task from state info
Task.create = function(json){
		var obj = JSON.parse(json);
        var task = new Task(obj.task_id, obj.name,obj.status,obj.assigned_to);
        return task;
}

// Find a task
Task.find = function(task_id){
        var keys = wave.getState().getKeys();
        for (var i = 0; i < keys.length; i++) {
            var key = keys[i];
            if (key == task_id){
                return Task.create(wave.getState().get(key));
            }
        }
        return null;
}

// Find all tasks
Task.findAll = function(){
    var tasks = {};
        var keys = wave.getState().getKeys();
        for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        var task = Task.create(wave.getState().get(key));
        tasks[key] = task;
    }
    return tasks;
}

// Make a guid for a new task
Task.guid = function(){
    var uid = function() {return (((1+Math.random())*0x10000)|0).toString(16).substring(1);}
    return uid()+uid()+"-"+uid()+"-"+uid()+"-"+uid()+"-"+uid()+uid()+uid();
}

/**
 * The Controller object
 * This is used to wire up the view and model with actions
 */ 
var Controller = {

    // Action handlers

    // Toggle task state between completed and to-do
    toggleTask: function(task_id){
        var task = Task.find(task_id);
        if (task.status == "completed"){
            task.status = "todo";
        } else {
            task.status = "completed";
        }
        task.save();
    },
    
    // Create a new task
    newTask: function(){
        var task_title = document.getElementById('taskinput').value;
        var task_id = Task.guid();
        var task = new Task(task_id,task_title, "todo", "unassigned");
        task.save();          
    },
    
    // Abandon task for someone else to do
    abandonTask: function(task_id){
        var task = Task.find(task_id);
        task.assigned_to = "unassigned";
        task.save();        
    },
    
    // Claim a task (assign to self)
    claimTask: function(task_id){
        var task = Task.find(task_id);
        task.assigned_to = Controller.user.id;
        task.save();  
    },
    
    // Event handlers
    
    // Update the view when state has been updated
    stateUpdated: function(){
        var tasks = Task.findAll();
        if (tasks && tasks != null){
            var tasklist = "";
            for (key in tasks) {
                var task = tasks[key];
                // Which button to show depends on task status and user id
                if (task.assigned_to == Controller.user.id){
                    var task_button = "<img class=\"status\" src=\""+task.status+".png\" onclick=\"Controller.toggleTask('"+key+"')\"/>";
                } else {
                    var task_button = "<img class=\"status\" src=\""+task.status+".png\"/>";                
                }
                // Task title
                var name = "<div class=\"taskname\">"+task.name+"</div>";
                // Which action to show (claim, abandon, none) depends on who owns the task
                var task_action = "";
                if (task.assigned_to == "unassigned") 
                    task_action = "<img class=\"action\" src=\"plus.png\" onclick=\"Controller.claimTask('"+key+"')\"/>";
                if (task.assigned_to == Controller.user.id && task.status == "todo") 
                    task_action = "<img class=\"action\" src=\"minus.png\" onclick=\"Controller.abandonTask('"+key+"')\"/>";
                // Task owner
                var owner = task.getUser();
                var task_owner = "<div class=\"taskowner\">"+owner.username+"</div>";
                var task_icon = "<img class=\"user\" src=\""+owner.thumbnail+"\"/>";
                tasklist = "<div class=\"task\">"+task_button+name+task_action+task_owner+task_icon+ "</div>" + tasklist;
            }
            dwr.util.setValue("tasklist", tasklist, { escapeHtml:false });
        }
    },
    
    participantsUpdated: function(){
        Controller.updateUser();
    },
    
    init: function(){
    	Controller.participantsUpdated();
    	Controller.stateUpdated();
    	/**
    	 * Register the event handlers with the Wave feature
    	 */
    	wave.setStateCallback(Controller.stateUpdated);
    	wave.setParticipantCallback(Controller.participantsUpdated);
    },
    /**
     * Setup user information
     */
    user: {},
    updateUser:function(){
    	if (wave.getViewer() != null){
    		Controller.user.id = wave.getViewer().getId();
    		Controller.user.username = wave.getViewer().getDisplayName();
    		Controller.user.thumbnail  = wave.getViewer().getThumbnailUrl();
    	}
    	if (Controller.user.thumbnail == "" || Controller.user.thumbnail == null) Controller.user.thumbnail = "anon.png";
    	if (Controller.user.username == null || Controller.user.username == ""){
    		Controller.user.username = "anonymouse";        
    		Controller.user.id = "anonymous";
    	}
    }
}