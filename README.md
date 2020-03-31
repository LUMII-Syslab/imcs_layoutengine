[![License](https://img.shields.io/badge/license-GPLv2wClasspathException%2BW3C-brightgreen)](https://raw.githubusercontent.com/LUMII-Syslab/imcs_layoutengine/master/imcs_layoutengine.COPYING)

# IMCS Layout Engine
IMCS Layout Engine is a JavaScript library for laying out graph diagrams and dialog windows.

- It has Java and JavaScript versions
- It uses fast quadratic optimization algorithm
- It is open-source

The initial Java version was low-level and not suitable for execution is web browsers.
Thus, Sergejs Kozlovičs developed two wrappers over the initial low-level Java code. Furthermore,
he created glue code and non-trivial build scripts for transpiling Java sources to JavaScript.

## Java classes
 The higher-level wrappers over the initial low-level code are located in the Java packages lv.lumii.diagramlayout and lv.lumii.dialoglayout. The wrappers provide two Java classes:
- IMCSDiagramLayout for laying out graph diagrams, and
- IMCSDialogLayout for laying out dialog windows.
 
It is advised to use these wrappers instead of the initial low-level code (unless you have certain reasons to do otherwise). 

## JavaScript module
The JS module imcs_layoutengine.min.js provides two JavaScript "classes" (objects that support the "new" operator) with the same names as the corresponding Java classes: IMCSDiagramLayout and IMCSDialogLayout.


## How to import the library

### Using a script tag (local .js):
```html
<script type="text/javascript" src="dist/imcs_layoutengine.min.js"></script>
```

### Using a script tag (remote .js):
```html
<script type="text/javascript" src="https://raw.githubusercontent.com/LUMII-Syslab/imcs_layoutengine/master/dist/imcs_layoutengine.min.js"></script>
```

### Using as a node.js/npm package:
  1) install the package:
     ```bash
     npm install @LUMII-Syslab/imcs_layoutengine --registry=https://npm.pkg.github.com
     ```
  2) import it and use in your code:
     ```bash
     import {IMCSDiagramLayout, IMCSDialogLayout} from '@LUMII-Syslab/imcs_layoutengine';
     ```

## Samples

* samples/diagram_layout_sample.html - an example showing how to use the IMCSDiagramLayout class
* samples/dialog_layout_sample.html - an example showing how to use the IMCSDialogLayout class

In addition, you can use samples/imcs_canvas.html to test the IMCSDiagramLayout class and
visualize the diagram right away.

## API Documentation

The documentation below explains the API of the two classes IMCSDiagramLayout and IMCSDialogLayout using the JavaScript syntax.
If you prefer to use the Java version, the syntax is similar.

* [IMCSDiagramLayout](https://github.com/LUMII-Syslab/imcs_layoutengine/blob/master/doc/IMCSDiagramLayout.md)
* [IMCSDialogLayout](https://github.com/LUMII-Syslab/imcs_layoutengine/blob/master/doc/IMCSDialogLayout.md)

The initial low-level Java code lays in the package lv.lumii.layoutengine. The documentation (JavaDoc) for it can be found in the folder doc/internal\_javadoc.

## How to compile from sources
The src/code folder contains Java classes. 
The "client" Java package contains glue code for generating JavaScript from Java. 
JavaScript code is generated from Java classes using GWT  via ant script (build.xml). Then we use webpack (we run it as an npm module) to minimize
the .js file and to make a JS module conforming to the Universal Module Definition standard (umd).

To compile:
* You should have Java8+ installed (java command or JAVA_HOME envvar must be available).
* You should have npm installed (npm command must be available).
* You should have Apache ant installed in ./apache-ant.
* You should have Google Web Toolkit (GWT) installed in ./gwt.

Then run:
```bash
npm install
npm run build
```

## Licences
The compiled JavaScript files are built from the code having
the following licenses:
* IMCS layout engine Java classes: src/code/lv/lumii/COPYING (GPLv2 or later with Classpath exception)
* W3C Java classes: src/code/org/w3c/COPYING (W3C liberal license)
* GWT-AWT classes: gwt-awt/COPYING  (GPLv2 with Classpath exception)

In addition, the following libraries, which are used at compile time, have their own licenses:
* GWT (Google Web Toolkit)
* webpack (as npm module) and dependencies

## Publications on IMCS Layout Engine Algorithm and Its Usage

* K. Freivalds, P. Kikusts Optimum Layout Adjustment Supporting Ordering Constraints in Graph-Like Diagram Drawing. Proc. of the Latvian Academy of Sciences, Section B, Vol. 55 (2001), No. 1.
* S. Kozlovics. A Dialog Engine Metamodel for the Transformation-Driven Architecture. In: Scientific Papers, University of Latvia. vol. 756, pp. 151-170 (2010)
* S. Kozlovics. Calculating The Layout For Dialog Windows Specified As Models. In: Scientific Papers, University of Latvia. vol. 787, pp. 106-124 (2012)