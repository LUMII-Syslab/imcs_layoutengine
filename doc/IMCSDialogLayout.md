# IMCSDialogLayout Doc
## IMCSDialogLayout Introduction

IMCSDialogLayout is a pseudo class that implements IMCS layout algorithms for laying out dialog windows in JavaScript.

Additional information on how to use the layout or on the meaning of certain parameters can be found in the papers:

1. Kozlovics, S.: A Dialog Engine Metamodel for the Transformation-Driven Architecture. In: Scientific Papers, University of Latvia. vol. 756, pp. 151-170 (2010)
2. Kozlovics, S.: Calculating The Layout For Dialog Windows Specified As Models. In: Scientific Papers, University of Latvia. vol. 787, pp. 106-124 (2012)

You can initialize the corresponding instance using the JavaScript new operator:
```javascript
var dialogLayout = new IMCSDialogLayout(callback);
```

The callback parameter is a JavaScript object, which defines certain functions for getting information about dialog components to be layed out. Each dialog component is referenced by an id (the rComponent parameter, which is a natural number). The callback functions are as follows (all number values, including rComponent, should be integers):

**getAnchor(rComponent)**

Specifies what coordinates are passed back to the layout callback of the given component. Possible return values are:

- &quot;parent&quot; (parent&#39;s x and y are considered zeroes, when laying out child elements)
- &quot;sibling&quot; (component&#39;s x and y are relative to the previous sibling)
- &quot;zero&quot; (component&#39;s x and y are relative to the form&#39;s left-top corner, which has coordinates (0;0) ).
- ? &quot;grandparent&quot; (grandparent&#39;s x and y are considered zeroes, when laying out grandchild elements)

Default is &quot;parent&quot;.

**load(rComponent)**

Creates a HTML (dojo, jQuery, or other) element for the given rComponent (but not for its children). The load function will be called for all child components recursively by IMCSDialogLayout.

If the component is being loaded asynchronously, call IMCSDialogLayout.loadStarted(rComponent) during load() and call IMCSDialogLayout.loadFinished(rComponent) when the component is fully loaded, e.g.,

```javascript
callback.load = function load(rComponent) {
  dialogLayout.loadStarted(rComponent);
  asyncFunctionToLoadComponent(rComponent, function(callback_args) {
    dialogLayout.loadFinished(rComponent);
  });
}
```

**getChildren(rComponent)**

Returns an array of numbers, which identify child component of the given rComponent.

**getBounds(rComponent)**

Returns an object contacting information of the component bounds in the following fields.

For leaf components (without children):

- minimumWidth
- maximumWidth
- preferredWidth
- minimumHeight
- maximumHeight
- preferredHeight
- leftMargin
- rightMargin
- topMargin
- bottomMargin

For containers (components that may contain other components) the following fields are also considered:

- leftPadding
- rightPadding
- topPadding
- bottomPadding
- horizontalSpacing
- verticalSpacing
- horizontalAlignment ( &quot;LEFT&quot;, &quot;RIGHT&quot;, or &quot;CENTER&quot;)
- verticalAlignment ( &quot;TOP&quot;, &quot;BOTTOM&quot;, or &quot;CENTER&quot;)

**getHorizontalRelativeInfo(rComponent)**

Returns an object contacting relative sizes w.r.t. to other components in the same group. See also getHorizontalRelativeInfoGroup/getVerticalRelativeInfoGroup, which define relative groups.

The fields are:

- minimumRelativeWidth
- preferredRelativeWidth
- maximumRelativeWidth

**getVerticalRelativeInfo(rComponent)**

Returns an object contacting relative sizes w.r.t. to other components in the same group. See also getHorizontalRelativeInfoGroup/getVerticalRelativeInfoGroup, which define relative groups.

The fields are:

- minimumRelativeHeight
- preferredRelativeHeight
- maximumRelativeHeight

**getHorizontalRelativeInfoGroup(rComponent)**

**getVerticalRelativeInfoGroup(rComponent)**

Return an integer representing a group. Relative infos are attached to groups defined by these integers. The same integer can be returned for horizontal and vertical groups to specify horizontal and vertical dimensions that relate on each other.

**getLayoutName(rComponent)**

Returns null, if a component is a leaf-component, which does not contain children (e.g., a button or an input field). Otherwise, returns one of the following values:

- &quot;VerticalBox&quot;
- &quot;HorizontalBox&quot;
- &quot;VerticalScrollBox&quot;
- &quot;HorizontalScrollBox&quot;
- &quot;ScrollBox&quot;
- &quot;Column&quot;
- &quot;Row&quot;
- &quot;Stack&quot;

**layout(rComponent, x, y, w, h)**

When the layout has been calculated, this function is called for each dialog component to set its physical coordinates and dimensions. The numbers are relative to the parent. For the root forms x=y=0.

**destroy(rComponent)**

When a form (or a part of the form) is unloaded (e.g., during refresh), the corresponding components in the subtree must be destroyed. This function is called for such components to physically remove them (e.g., from the HTML DOM).

## IMCSDialogLayout Methods

**IMCSDialogLayout.loadAndLayout(rForm)**

Loads the component tree starting from the root component rForm and performs the initial layout (e.g., the layout callback function will be called for each component).

**IMCSDialogLayout.refreshAndLayout (rRootComponent, formWidth, formHeight)**

When some component subtree is changed, this function may be called to re-load that subtree and to perform incremental layout of the dialog window. If form width and height have been changed (e.g.,during resize), they may also be specified (thus, the form will try to be of that size).
