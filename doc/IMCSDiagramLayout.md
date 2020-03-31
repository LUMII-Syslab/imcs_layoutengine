# IMCSDiagramLayout Doc

## IMCSDiagramLayout Introduction

You can initialize the corresponding instance using the JavaScript new operator:
var layout = new IMCSDiagramLayout();

If you do not pass any constructor parameters, or pass the string &quot;UNIVERSAL&quot;, then the universal layout algorithm will be used. Other layout algorithms are &quot;SYMMETRIC&quot;, &quot;VERTICAL&quot; (for an edge a→b the layout algorithm will return y\_max[a]≤y\_min[b]), &quot;INVERSE\_VERTICAL&quot;, &quot;HORIZONTAL&quot;, and &quot;INVERSE\_HORIZONTAL&quot;. Pass the corresponding string to the constructor:

var layout = new IMCSDiagramLayout(&quot;UNIVERSAL&quot;);

Then you can add/remove boxes and lines (using appropriate methods) as well as to call either the arrangeIncrementally(), or arrangeFromScratch() method. They both return the calculated layout information.

An important feature of the JavaScript implementation is that you specify integer IDs for boxes and lines. For instance, these IDs may be equal to database primary keys or references to model repository objects.

## IMCSDiagramLayout Methods

Please, notice that id&#39;s are of Java type long, but x, y, width and height are doubles.

**IMCSDiagramLayout.addBox(boxId, x, y, w, h)**
adds a box with the given id (some integer), x, y, width, and height to the layout;
during the initial layout, when no coordinates of boxes are known, you may specify x=y=0;
if a new box is added to the existing layout, use the current mouse position for x and y;
returns whether the operation succeeded;

**IMCSDiagramLayout.addLine(lineId, srcId, tgtId, options)**
adds an orthogonal line connecting the two boxes with the given id-s to the layout; optional options are in the following format (default values are specified):
```json
{
  lineType: "ORTHOGONAL", // or "POLYLINE", or "STRAIGHT"
  startSides: 15, // =parseInt("1111", 2)=0b1111, see bit mask values below
  endSides: 15, // =parseInt("1111", 2)=0b1111, see bit mask values below
}
```

Bit mask values:

* 0b0001: top (if the y axis is oriented downwards) = min\_y = 1
* 0b0010: right = 2
* 0b0100: bottom (if the y axis is oriented downwards) = max\_y = 4
* 0b1000: left = 8

Returns whether the operation succeeded.

**IMCSDiagramLayout.addLineLabel(labelId, lineId, w, h, placement)**
adds the label with the given labelId to the line with the given lineId; the size of the label is w\*h; the position is specified in the placement attribute and is one of the following values:

- &quot;start-left&quot;, i.e., left, if we follow the line direction (from start to end), near the start
- &quot;start-right&quot;,
- &quot;end-left&quot;, i.e., left, if we follow the line direction, near the end
- &quot;end-right&quot;,
- &quot;middle-left&quot;,
- &quot;middle-right&quot;.

returns whether the operation succeeded;

**IMCSDiagramLayout.removeLine(lineId)**
removes the given line from the layout;
the layout is not re-arranged.

Returns whether the operation succeeded.

**IMCSDiagramLayout.removeBox(boxId)**
removes the box as well as the incident lines from the layout;
the layout is not re-arranged.

Returns the array of id-s of the removed lines (perhaps, an empty array);
on error, returns false.

**IMCSDiagramLayout.moveBox(boxId, newX, newY)**
sets new desired coordinates for the box with the given id;
the layout is not re-arranged (call arrange() after the desired coordinates of all the desired boxes are set).

Returns whether the operation succeeded.

Warning: arrange() or arrangeFromScratch() must be called some time before, if you use a hierarchical layout other from &quot;VERTICAL&quot;!

**IMCSDiagramLayout.resizeBox(boxId, w, h)**
sets new desired dimensions for the box with the given id;
the layout is not re-arranged (call arrange() after the desired dimensions of all the desired boxes are set).

Returns whether the operation succeeded.

**IMCSDiagramLayout.moveLine(lineId, srcId, tgtId, points)**
sets new line start and end boxes and (optionally) line points;
the points are specified as an array of objects with the x and y attributes;
the layout is not re-arranged (call arrange() after all desired manipulations are called).

Returns whether the operation succeeded.

Warning: arrange() or arrangeFromScratch() must be called some time before, if you use a hierarchical layout other from &quot;VERTICAL&quot;!

**IMCSDiagramLayout.arrangeIncrementally()**
arranges the diagram taking into a consideration recently added elements and trying to preserve existing coordinates.

Returns an objects with the &quot;boxes&quot;, &quot;lines&quot;, and &quot;labels&quot; maps containing information about the layout

- the boxes map is in the form id->{x, y, width, height};
- the lines map is in the form id->[{x:x1,y:y1}, {x:x2,y:y2}, ...]
- the labels map is in the form id->{x, y, width, height};

**IMCSDiagramLayout.arrangeFromScratch()**
arranges the diagram from scratch not preserving existing coordinates.

Returns an objects with the &quot;boxes&quot;, &quot;lines&quot;, and &quot;labels&quot; maps containing information about the layout:

- the boxes map is in the form id->{x, y, width, height};
- the lines map is in the form id->[{x:x1,y:y1}, {x:x2,y:y2}, ...]
- the labels map is in the form id->{x, y, width, height};
