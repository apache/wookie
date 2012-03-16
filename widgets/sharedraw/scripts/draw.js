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
var canvas, context, tool;
  
function init_canvas () {
    // Find the canvas element.
    canvas = document.getElementById('imageView');
    if (!canvas) {
      alert('Error: I cannot find the canvas element!');
      return;
    }

    if (!canvas.getContext) {
      alert('Error: no canvas.getContext!');
      return;
    }

    // Get the 2D canvas context.
    context = canvas.getContext('2d');
    if (!context) {
      alert('Error: failed to getContext!');
      return;
    }
    
    // Fit canvas to window
    var viewport = getViewport();
    context.canvas.width  = viewport.width;
    context.canvas.height = viewport.height;

    // Pencil tool instance.
    tool = new tool_pencil();

    // Attach the mousedown, mousemove and mouseup event listeners.
	if (canvas.attachEvent) {
    	canvas.attachEvent("onmousedown", ev_canvas, false);
    	canvas.attachEvent('onmousemove', ev_canvas, false);
    	canvas.attachEvent('onmouseup',   ev_canvas, false);	
	}
	else if (canvas.addEventListener) { 
		// Attach the mousedown, mousemove and mouseup event listeners.
		canvas.addEventListener('mousedown', ev_canvas, false);
		canvas.addEventListener('mousemove', ev_canvas, false);
		canvas.addEventListener('mouseup',   ev_canvas, false);
	}    
  }

  function tool_pencil () {
    var tool = this;
    this.started = false;
    this.mousedown = function (ev) {
        tool.line = new Array();
        context.beginPath();
        context.moveTo(ev._x, ev._y);
        tool.line[0] = ev._x + ":" + ev._y;
        tool.started = true;
    };
    this.mousemove = function (ev) {
      if (tool.started) {
        tool.line.push(ev._x + ":" + ev._y);
        context.lineTo(ev._x, ev._y);
        context.stroke();
      }
    };
    this.mouseup = function (ev) {
      if (tool.started) {
        tool.mousemove(ev);
        tool.started = false;
        // Save the action
        line = new Line(Line.guid(),tool.line,null);
        line.save();
      }
    };
  }
  function ev_canvas (ev) {
    if (ev.layerX || ev.layerX == 0) { // Firefox
      ev._x = ev.layerX;
      ev._y = ev.layerY;
    } else if (ev.offsetX || ev.offsetX == 0) { // Opera
      ev._x = ev.offsetX;
      ev._y = ev.offsetY;
    }
    var func = tool[ev.type];
    if (func) {
      func(ev);
    }
  }
  
  //
  // Get the viewport height and width
  //
  function getViewport() {
    var viewport = {};
    viewport.height = 0;
    viewport.width = 0;
    if( typeof( window.innerHeight ) == 'number' ) {
      //Non-IE
      viewport.height = window.innerHeight;
      viewport.width = window.innerWidth;
    } else if( document.documentElement &&( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
      //IE 6+ in 'standards compliant mode'
      viewport.height = document.documentElement.clientHeight;
      viewport.width = document.documentElement.clientWidth; 
    } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
      //IE 4 compatible
      viewport.height = document.body.clientHeight;
      viewport.width = document.body.clientWidth;
    }
    return viewport;
  }
