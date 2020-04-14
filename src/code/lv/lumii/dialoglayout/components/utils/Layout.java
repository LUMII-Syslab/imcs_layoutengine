/*
*
* Copyright (c) 2013-2020 Institute of Mathematics and Computer Science, University of Latvia (IMCS UL). 
* Relevant scientific papers:
*  - S. Kozlovics. A Dialog Engine Metamodel for the Transformation-Driven Architecture. In: Scientific Papers, University of Latvia. vol. 756, pp. 151-170 (2010)
*  - S. Kozlovics. Calculating The Layout For Dialog Windows Specified As Models. In: Scientific Papers, University of Latvia. vol. 787, pp. 106-124 (2012)

* This file is part of layoutengine
*
* You can redistribute it and/or modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation, either version 2 of the License,
* or (at your option) any later version.
*
* This file is also subject to the "Classpath" exception as mentioned in
* the COPYING file that accompanied this code.
*
* You should have received a copy of the GNU General Public License along with layoutengine. If not, see http://www.gnu.org/licenses/.
*
*/
package lv.lumii.dialoglayout.components.utils;

import java.util.Map;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.Component;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

public class Layout {
	//constants for quadratic optimization
	public static final double MAXWIDTH=9999;
	public static final double MAXHEIGHT=9999;
	public static final double PREFERREDWEIGHT=10000;
	public static final double ALIGNEMENTWEIGHT=100000.0;
	//public static final double GRAVITY=PREFERREDWEIGHT*100;/**100000*/;
	//public static final double GRAVITY_STEP=1.0;//0.01;
	public static final double INITIAL_GRAVITY_WEIGHT = 10000000000000.0;//10000000000.0;
	public static final double GRAVITY_WEIGHT_ADJUSTMENT = 1.5;
	
	public static final double RELATIVEPREFERREDWEIGHT=1;
	
	// RELATIVE_SIZE_WEIGHT = 1.0;
	public static final double INFINITY=100000000;
	
	public static double getChildGravity(double parentGravity, int nChildren) {
		if (nChildren==0)
			return parentGravity/100;
		return parentGravity/(nChildren);
	}
	
	public static int updateIndicesFor(int i, Component c){
		//update components indices
		i=c.getComponentBounds().updateIndicesFrom(i);
		//for every child update child's indices
		if (c.getChildren()!=null) {
			for (Component child:c.getChildren())
				i=updateIndicesFor(i,child);
		}
		return i;
	}
	
	public static int updateIndicesFor(int i, Component c, double[] lastSolution, Map<Integer, Double> newInitialValues){
		//update components indices
		i=c.getComponentBounds().updateIndicesFrom(i, lastSolution, newInitialValues);
		if (c.getChildren()!=null) {
			for (Component child:c.getChildren())
				i=updateIndicesFor(i,child);
		}
		return i;
	}
}
