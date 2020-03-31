[![License](https://img.shields.io/badge/-GPLv2%20w%2FClasspath%20exception%20%26%26%20W3C-brightgreen)](https://raw.githubusercontent.com/LUMII-Syslab/imcs_layoutengine/master/imcs_layoutengine.COPYING)

# IMCS Layout Engine
IMCS Layout Engine is a library for laying out graph diagrams and dialog windows.

- It has Java and JavaScript versions
- It uses fast quadratic optimization algorithm
- It is open source

## How to use the library

### Using a script tag:
```html
<script type="text/javascript" src="dist/imcs_layoutengine.min.js"></script>
```
or
```html
<script type="text/javascript" src="https://raw.githubusercontent.com/LUMII-Syslab/imcs_layoutengine/master/dist/imcs_layoutengine.min.js"></script>
```

Samples:
* samples/diagram_layout_sample.html - an example showing how to use the IMCSDiagramLayout class
* samples/dialog_layout_sample.html - an example showing how to use the IMCSDialogLayout class

### Using as a node.js/npm package:
  1) install the package:
     npm install @LUMII-Syslab/imcs_layoutengine --registry=https://npm.pkg.github.com
  2) import it and use in your code:
     import {IMCSDiagramLayout, IMCSDialogLayout} from '@LUMII-Syslab/imcs_layoutengine';

In addition, you can use samples/imcs_canvas.html to test the IMCSDiagramLayout class and
visualize the diagram right away.

## How to compile from sources
You should have Java8+ installed (java command or JAVA_HOME envvar must be available).
You should have npm installed (npm command must be available)

You should have Apache ant installed in ./apache-ant.
You should have Google Web Toolkit (GWT) installed in ./gwt.

Then run:
```bash
npm run build
```

## Licences
The compiled JavaScript files are built from the code having
the following licenses:
* IMCS layout engine Java classes: src/code/lv/lumii/COPYING (GPLv2 or later with the classpath exception)
* W3C Java classes: src/code/org/w3c/COPYING (W3C liberal license)
* GWT-AWT classes: gwt-awt/COPYING  (GPLv2 with the classpath exception)

In addition, the following libraries, which are used at compile time, have their own licenses:
* GWT (Google Web Toolkit)
* webpack (as npm module) and dependencies
