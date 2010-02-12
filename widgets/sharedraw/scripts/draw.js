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

    // Pencil tool instance.
    tool = new tool_pencil();

    // Attach the mousedown, mousemove and mouseup event listeners.
    canvas.addEventListener('mousedown', ev_canvas, false);
    canvas.addEventListener('mousemove', ev_canvas, false);
    canvas.addEventListener('mouseup',   ev_canvas, false);
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