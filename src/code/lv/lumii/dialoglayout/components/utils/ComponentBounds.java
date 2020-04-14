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

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

public class ComponentBounds {
	public int xl=-1;
	public int xr=-1;
	public int xt=-1;
	public int xb=-1;
	
	public Integer minW = null;
	public Integer preW = null;
	public Integer maxW = null;
	public Integer minH = null;
	public Integer preH = null;
	public Integer maxH = null;

	//default values
	public Integer leftM=0;//5; 
	public Integer rightM=0;//5;
	public Integer topM=0;//5;
	public Integer bottomM=0;//5;
	
	
	//default values (for containers)
	public Integer leftP=0;
	public Integer rightP=0;
	public Integer topP=0;
	public Integer bottomP=0;
	
	public Integer horSpace=10;
	public Integer verSpace=10;
	
	public String horAlign="CENTER";
	public String verAlign="CENTER";
	
		
	public int updateIndicesFrom(int i){
		xl=i++;
		xt=i++;
		xr=i++;
		xb=i++;
		return i;
	}	
	
	public int updateIndicesFrom(int i, double[] lastSolution, Map<Integer, Double> newInitialValues){
		if (xl!=-1)
			newInitialValues.put(i+1, lastSolution[xl]);
		xl=i++;		
		if (xt!=-1)
			newInitialValues.put(i+1, lastSolution[xt]);
		xt=i++;
		if (xr!=-1)
			newInitialValues.put(i+1, lastSolution[xr]);
		xr=i++;
		if (xb!=-1)
			newInitialValues.put(i+1, lastSolution[xb]);
		xb=i++;
		return i;
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		if (minW!=null) {
			eqo.addInequality(xl, xr, minW);
		}
		else
			eqo.addInequality(xl, xr, 0);
		
		if (maxW!=null) {
			eqo.addReducibleInequality(xr, xl, -maxW, -Layout.MAXWIDTH);
		}
		else {
			eqo.addInequality(xr, xl,-Layout.MAXWIDTH);
		}
		
		if (minH!=null) {
			eqo.addInequality(xt, xb, minH);
		}
		else
			eqo.addInequality(xt, xb, 0);
		
		if (maxH!=null) {
			eqo.addReducibleInequality(xb, xt, -maxH, -Layout.MAXHEIGHT);
		}
		else {
			eqo.addInequality(xb, xt, -Layout.MAXHEIGHT);
		}
		
		if (preW!=null) {
			eqo.addQuadraticDifference(xr, xl, Layout.PREFERREDWEIGHT);
			eqo.addLinearDifference(xl, xr, -2*preW*Layout.PREFERREDWEIGHT);
		}
		
		if (preH!=null) {
			eqo.addQuadraticDifference(xb, xt, Layout.PREFERREDWEIGHT);
			eqo.addLinearDifference(xt, xb, -2*preH*Layout.PREFERREDWEIGHT);
		}
	}
	
}
