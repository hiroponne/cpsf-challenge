var drawFlag = false;
var previousX = 0;
var previousY = 0;


window.addEventListener("load", function() {
  var bb = document.getElementById("blackboard");
  bb.addEventListener("mousemove", draw, true);
  bb.addEventListener("mousedown", function(e) {
    drawFlag = true;
    previousX = e.clientX;
    previousY = e.clientY;
  }, false);
  bb.addEventListener("mouseup", function() {
    drawFlag = false;
  }, false);
}, true);

function draw(e) {
  if(!drawFlag) {
    return;
  }
  var x = e.clientX;
  var y = e.clientY;
  var bb = document.getElementById("blackboard");
  var context = bb.getContext("2d");
  context.strokeStyle = "white";
  context.lineWidth = 3;
  context.beginPath();
  context.moveTo(previousX, previousY);
  context.lineTo(x, y);
  context.stroke();
  context.closePath();
  previousX = x;
  previousY = y;
}
