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
 
var canvas, context;
var lastPoints = Array();
var mirrorPoints = Array();
  
var Controller = {

    red: 255,
    green: 255,
    blue: 64,
    
    setRGB: function(r,g,b){
        this.red=r;this.green=g;this.blue=b;
    },
    
    fill: function(){
        context.fillStyle = "rgb("+this.red+","+this.green+","+this.blue+")";
    	context.fillRect(0,0,400,300);
    },

    init: function(){
        this.init_canvas();
    }
}

Controller.init_canvas = function() {
    //
    // Find the canvas element.
    //
    canvas = document.getElementById('imageView');
    if (!canvas) {
      alert('Error: I cannot find the canvas element!');
      return;
    }

    if (!canvas.getContext) {
      alert('Error: no canvas.getContext!');
      return;
    }
    
    canvas.style.cursor="crosshair";

    //
    // Get the 2D canvas context.
    //
    context = canvas.getContext('2d');
    if (!context) {
      alert('Error: failed to getContext!');
      return;
    }
    
    // Mask
    mask();
    
    // If IE or OperaMobile
	if (typeof canvas.attachEvent !== 'undefined') {
    	canvas.attachEvent("onmousedown", Controller.startDraw, false);
	    canvas.attachEvent('onmouseup',   Controller.stopDraw, false);	
        canvas.attachEvent("ontouchstart", Controller.startDraw, false);
	    canvas.attachEvent('ontouchstop', Controller.stopDraw, false);
	    canvas.attachEvent('ontouchmove',   Controller.drawMouse, false);	
	}
	 else { 
    	// Attach the mouse and touch event listeners.
        canvas.onmousedown = Controller.startDraw;
		canvas.onmouseup = Controller.stopDraw;
		canvas.ontouchstart = Controller.startDraw;
		canvas.ontouchstop = Controller.stopDraw;
		canvas.ontouchmove = Controller.drawMouse;
    }
  }
  
  
//
// Start drawing - get the start position, and mirror position on the other side of the image.
//
Controller.startDraw = function(e) {
	if (e.touches) {
		// Touch event
		for (var i = 1; i <= e.touches.length; i++) {
			lastPoints[i] = getCoords(e.touches[i - 1]); // Get info for finger #1
            mirrorPoints[i] = getCoords(e.touches[i - 1]); // Get info for finger #1
            mirrorPoints[i].x =   400-mirrorPoints[i].x 
		}
	}
	else {
		// Mouse event
		lastPoints[0] = getCoords(e);
        mirrorPoints[0] = getCoords(e);
        mirrorPoints[0].x =   400-mirrorPoints[0].x 
		canvas.onmousemove = Controller.drawMouse;
	}
	
	return false;
}

//
// Stop drawing
//
Controller.stopDraw = function(e) {
    e.preventDefault();
    
    //
    // Add a "dab" in cases where there was no move action
    //
    Controller.drawMouse(e);
    
	canvas.onmousemove = null;
}

//
// Draw
//
Controller.drawMouse = function(e) {
	if (e.touches) {
		// Touch Enabled
		for (var i = 1; i <= e.touches.length; i++) {
			var p = getCoords(e.touches[i - 1]); // Get info for finger i
			lastPoints[i] = paint(lastPoints[i].x, lastPoints[i].y, p.x, p.y);
            mirrorPoints[i] = paint(mirrorPoints[i].x, mirrorPoints[i].y, 400-p.x, p.y);
		}
	}
	else {
		// Not touch enabled
		var p = getCoords(e);
		lastPoints[0] = paint(lastPoints[0].x, lastPoints[0].y, p.x, p.y);
        mirrorPoints[0] = paint(mirrorPoints[0].x, mirrorPoints[0].y, 400-p.x, p.y);
	}
	context.closePath();
	context.beginPath();

	return false;
}

//
// Paint on the canvas from the (s)tart to (e)nd
//
function paint(sX, sY, eX, eY) {
	context.moveTo(sX, sY);
    brush(eX,eY);
	return { x: eX, y: eY };
}

//
// Get the coordinates for a mouse or touch event
//
function getCoords(e) {
	if (e.offsetX) {
		return { x: e.offsetX, y: e.offsetY };
	}
	else if (e.layerX) {
		return { x: e.layerX, y: e.layerY };
	}
	else {
		return { x: e.pageX - canvas.offsetLeft, y: e.pageY - canvas.offsetTop };
	}
}
  
  
//
// Add a brush splodge onto the canvas
//
brush = function (x,y) {
		var c = context,
			D = 15,
			D2 = D * 2;
            hardness = 30;
            opacity = 60;
          /*  
            // add this for IE
            if (canvas.attachEvent) {
            	D = 8;
            	D2 = D * 2;
            }
           */ 
		c.globalCompositeOperation = 'source-over';
		var r = c.createRadialGradient(x, y, hardness / 100 * D - 1, x, y, D);
		r.addColorStop(0, 'rgba('+Controller.red+','+Controller.green+','+Controller.blue+',' + opacity / 100 + ')');
		r.addColorStop(0.95, 'rgba('+Controller.red+','+Controller.green+','+Controller.blue+',0)'); // prevent aggregation of semi-opaque pixels
		r.addColorStop(1, 'rgba('+Controller.red+','+Controller.green+','+Controller.blue+',0)');
		c.fillStyle = r;
		c.fillRect(x-D, y-D, D2, D2);
		c.globalCompositeOperation = 'source-in';
		c.rect(x-D, y-D, D2, D2);
};

mask = function(){
	// the butterfly shape
    context.save();
    context.beginPath();
    context.translate(400,300); 
    context.rotate(Math.PI);
    context.moveTo(0,300);
    // if IE - TODO butterfly image is not quite drawn correctly
    if (canvas.attachEvent) {
    	context.quadraticCurveTo(80.1783,301.0774,134.5506,273.2636,168.0000,228.0000);
    	context.quadraticCurveTo(174.6660,215.3346,181.3340,202.6654,188.0000,190.0000);
    	context.quadraticCurveTo(189.3332,194.9995,190.6668,200.0005,192.0000,205.0000);    
    	context.quadraticCurveTo(171.6442,233.3266,148.4269,261.6911,134.0000,295.0000);
    	context.quadraticCurveTo(134.3333,295.6666,134.6667,296.3334,135.0000,297.0000);
    	context.quadraticCurveTo(154.8897,289.3127,174.1114,208.2397,205.0000,207.0000);
    	context.quadraticCurveTo(206.9998,208.6665,209.0002,210.3335,211.0000,212.0000);
    	context.quadraticCurveTo(213.8325,219.3365,262.2242,296.0280,265.0000,297.0000);
    	context.quadraticCurveTo(265.3333,296.3334,265.6667,295.6666,266.0000,295.0000);
    	context.quadraticCurveTo(253.2034,263.5240,229.7223,229.2060,208.0000,205.0000);
    	context.quadraticCurveTo(209.3332,199.6672,210.6668,194.3328,212.0000,189.0000);
    	context.quadraticCurveTo(212.3333,189.0000,212.6667,189.0000,213.0000,189.0000);
    	context.quadraticCurveTo(213.3333,189.0000,213.6667,189.0000,214.0000,189.0000);
    	context.quadraticCurveTo(235.4263,260.3326,305.6072,300.4202,400.0000,300.0000);
    	context.quadraticCurveTo(400.7564,260.7574,381.7836,251.4776,376.0000,221.0000);
    	context.quadraticCurveTo(372.5464,202.8009,374.9546,182.1234,363.0000,173.0000);
    	context.quadraticCurveTo(352.5150,164.9982,339.5687,168.3028,325.0000,163.0000);
    	context.quadraticCurveTo(325.0000,162.6667,325.0000,162.3333,325.0000,162.0000);
    	context.quadraticCurveTo(349.5033,158.3783,352.2077,130.7042,354.0000,109.0000);
    	context.quadraticCurveTo(345.9678,103.6550,343.8264,100.1513,338.0000,94.0000);
    	context.quadraticCurveTo(339.8800,86.6748,343.8295,83.7354,341.0000,77.0000);
    	context.quadraticCurveTo(336.3338,73.6670,331.6662,70.3330,327.0000,67.0000);
    	context.quadraticCurveTo(311.5394,39.7644,355.2383,26.7068,341.0000,2.0000);
    	context.quadraticCurveTo(337.2354,0.8094,333.2949,-0.8581,329.0000,1.0000);
    	context.quadraticCurveTo(327.6668,1.6666,326.3332,2.3334,325.0000,3.0000);
    	context.quadraticCurveTo(321.9745,19.4608,323.1224,37.2860,309.0000,43.0000);
    	context.quadraticCurveTo(294.2036,46.5382,273.9450,34.5515,258.0000,41.0000);
    	context.quadraticCurveTo(230.1233,52.2740,223.0465,111.1756,212.0000,141.0000);
    	context.quadraticCurveTo(211.3334,141.0000,210.6666,141.0000,210.0000,141.0000);
    	context.quadraticCurveTo(208.6668,130.3344,207.3332,119.6656,206.0000,109.0000);
    	context.quadraticCurveTo(204.0002,107.6668,201.9998,106.3332,200.0000,105.0000);
    	context.quadraticCurveTo(198.0002,105.9999,195.9998,107.0001,194.0000,108.0000);
    	context.quadraticCurveTo(192.0002,118.6656,189.9998,129.3344,188.0000,140.0000);
    	context.quadraticCurveTo(188.0000,139.3334,188.0000,138.6666,188.0000,138.0000);
    	context.quadraticCurveTo(181.3340,119.3352,174.6660,100.6648,168.0000,82.0000);
    	context.quadraticCurveTo(166.3335,73.6675,164.6665,65.3325,163.0000,57.0000);
    	context.quadraticCurveTo(159.6670,55.0002,156.3330,52.9998,153.0000,51.0000);
    	context.quadraticCurveTo(124.5349,29.4373,115.4563,53.0338,91.0000,43.0000);
    	context.quadraticCurveTo(75.6567,36.7051,77.4912,12.9952,69.0000,0.0000);
    	context.quadraticCurveTo(65.3654,0.6436,65.9730,0.4032,64.0000,2.0000);
    	context.quadraticCurveTo(59.0478,4.2801,58.0846,6.9206,56.0000,12.0000);
    	context.quadraticCurveTo(58.5748,31.4614,74.0400,36.2547,75.0000,60.0000);
    	context.quadraticCurveTo(70.0342,69.9436,61.4112,73.0454,56.0000,82.0000);
    	context.quadraticCurveTo(58.6524,86.5103,60.9126,87.5516,62.0000,94.0000);
    	context.quadraticCurveTo(58.7903,95.8190,48.1997,106.9635,45.0000,111.0000);
    	context.quadraticCurveTo(51.2644,140.5583,50.9489,147.1374,74.0000,163.0000);
    	context.quadraticCurveTo(62.8643,167.3318,49.5220,163.8747,41.0000,170.0000);
    	context.quadraticCurveTo(25.0144,181.4898,28.4368,204.3249,23.0000,226.0000);
    	context.quadraticCurveTo(16.0797,253.5891,-0.2461,262.3976,0.0000,300.0000);
   	}
   	else{
   		// IE8 took too long to process this...
   		context.bezierCurveTo(80.1783,301.0774,134.5506,273.2636,168.0000,228.0000);
   		context.bezierCurveTo(174.6660,215.3346,181.3340,202.6654,188.0000,190.0000);
   		context.bezierCurveTo(189.3332,194.9995,190.6668,200.0005,192.0000,205.0000);    
   		context.bezierCurveTo(171.6442,233.3266,148.4269,261.6911,134.0000,295.0000);
   		context.bezierCurveTo(134.3333,295.6666,134.6667,296.3334,135.0000,297.0000);
   		context.bezierCurveTo(154.8897,289.3127,174.1114,208.2397,205.0000,207.0000);
   		context.bezierCurveTo(206.9998,208.6665,209.0002,210.3335,211.0000,212.0000);
   		context.bezierCurveTo(213.8325,219.3365,262.2242,296.0280,265.0000,297.0000);
   		context.bezierCurveTo(265.3333,296.3334,265.6667,295.6666,266.0000,295.0000);
   		context.bezierCurveTo(253.2034,263.5240,229.7223,229.2060,208.0000,205.0000);
   		context.bezierCurveTo(209.3332,199.6672,210.6668,194.3328,212.0000,189.0000);
   		context.bezierCurveTo(212.3333,189.0000,212.6667,189.0000,213.0000,189.0000);
   		context.bezierCurveTo(213.3333,189.0000,213.6667,189.0000,214.0000,189.0000);
   		context.bezierCurveTo(235.4263,260.3326,305.6072,300.4202,400.0000,300.0000);
   		context.bezierCurveTo(400.7564,260.7574,381.7836,251.4776,376.0000,221.0000);
   		context.bezierCurveTo(372.5464,202.8009,374.9546,182.1234,363.0000,173.0000);
   		context.bezierCurveTo(352.5150,164.9982,339.5687,168.3028,325.0000,163.0000);
   		context.bezierCurveTo(325.0000,162.6667,325.0000,162.3333,325.0000,162.0000);
   		context.bezierCurveTo(349.5033,158.3783,352.2077,130.7042,354.0000,109.0000);
   		context.bezierCurveTo(345.9678,103.6550,343.8264,100.1513,338.0000,94.0000);
   		context.bezierCurveTo(339.8800,86.6748,343.8295,83.7354,341.0000,77.0000);
   		context.bezierCurveTo(336.3338,73.6670,331.6662,70.3330,327.0000,67.0000);
   		context.bezierCurveTo(311.5394,39.7644,355.2383,26.7068,341.0000,2.0000);
   		context.bezierCurveTo(337.2354,0.8094,333.2949,-0.8581,329.0000,1.0000);
   		context.bezierCurveTo(327.6668,1.6666,326.3332,2.3334,325.0000,3.0000);
   		context.bezierCurveTo(321.9745,19.4608,323.1224,37.2860,309.0000,43.0000);
   		context.bezierCurveTo(294.2036,46.5382,273.9450,34.5515,258.0000,41.0000);
   		context.bezierCurveTo(230.1233,52.2740,223.0465,111.1756,212.0000,141.0000);
   		context.bezierCurveTo(211.3334,141.0000,210.6666,141.0000,210.0000,141.0000);
   		context.bezierCurveTo(208.6668,130.3344,207.3332,119.6656,206.0000,109.0000);
   		context.bezierCurveTo(204.0002,107.6668,201.9998,106.3332,200.0000,105.0000);
   		context.bezierCurveTo(198.0002,105.9999,195.9998,107.0001,194.0000,108.0000);
   		context.bezierCurveTo(192.0002,118.6656,189.9998,129.3344,188.0000,140.0000);
   		context.bezierCurveTo(188.0000,139.3334,188.0000,138.6666,188.0000,138.0000);
   		context.bezierCurveTo(181.3340,119.3352,174.6660,100.6648,168.0000,82.0000);
   		context.bezierCurveTo(166.3335,73.6675,164.6665,65.3325,163.0000,57.0000);
   		context.bezierCurveTo(159.6670,55.0002,156.3330,52.9998,153.0000,51.0000);
   		context.bezierCurveTo(124.5349,29.4373,115.4563,53.0338,91.0000,43.0000);
   		context.bezierCurveTo(75.6567,36.7051,77.4912,12.9952,69.0000,0.0000);
   		context.bezierCurveTo(65.3654,0.6436,65.9730,0.4032,64.0000,2.0000);
   		context.bezierCurveTo(59.0478,4.2801,58.0846,6.9206,56.0000,12.0000);
   		context.bezierCurveTo(58.5748,31.4614,74.0400,36.2547,75.0000,60.0000);
   		context.bezierCurveTo(70.0342,69.9436,61.4112,73.0454,56.0000,82.0000);
   		context.bezierCurveTo(58.6524,86.5103,60.9126,87.5516,62.0000,94.0000);
   		context.bezierCurveTo(58.7903,95.8190,48.1997,106.9635,45.0000,111.0000);
   		context.bezierCurveTo(51.2644,140.5583,50.9489,147.1374,74.0000,163.0000);
   		context.bezierCurveTo(62.8643,167.3318,49.5220,163.8747,41.0000,170.0000);
   		context.bezierCurveTo(25.0144,181.4898,28.4368,204.3249,23.0000,226.0000);
   		context.bezierCurveTo(16.0797,253.5891,-0.2461,262.3976,0.0000,300.0000);
   	}           
    context.closePath();
    context.stroke();
    context.restore();
    context.clip();
    context.fillStyle = "rgb(255,255,255)";
    context.fill();        
}



