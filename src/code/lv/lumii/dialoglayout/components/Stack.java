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
package lv.lumii.dialoglayout.components;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class Stack extends Component {

	public void writeGravity(ExtendedQuadraticOptimizer eqo,double coeff) {
		//??? double newCoeff = coeff/2; // assume comp_count==1, since all child components are with equal gravity
		double newCoeff = Layout.getChildGravity(coeff, children.size());
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's left and right borders
			eqo.addLinearDifference(bounds.xl, childBounds.xl, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			eqo.addLinearDifference(childBounds.xr, bounds.xr, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			eqo.addLinearDifference(bounds.xt, child.getComponentBounds().xt, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			eqo.addLinearDifference(child.getComponentBounds().xb, bounds.xb, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
		}
		
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
				
		//consider information about all inner components
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			//consider horizontal padding and horizontal alignment 
			if (bounds.horAlign.equals("LEFT"))
				eqo.addEquality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			else
				eqo.addInequality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			
			if (bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			else
				eqo.addInequality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, childBounds.xl, childBounds.xr, Layout.ALIGNEMENTWEIGHT);
			
		}
		

		Component prevChild = null;
		for (Component child:getChildren()) {
			if (bounds.verAlign.equals("TOP")) {
				eqo.addEquality(bounds.xt, child.getComponentBounds().xt,bounds.topP+child.getComponentBounds().topM);
			}
			else
				eqo.addInequality(bounds.xt, child.getComponentBounds().xt,bounds.topP+child.getComponentBounds().topM);
			
			if (bounds.verAlign.equals("BOTTOM"))
				eqo.addEquality(child.getComponentBounds().xb,bounds.xb,bounds.bottomP+child.getComponentBounds().bottomM);
			else
				eqo.addInequality(child.getComponentBounds().xb,bounds.xb,bounds.bottomP+child.getComponentBounds().bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, child.getComponentBounds().xt, child.getComponentBounds().xb, Layout.ALIGNEMENTWEIGHT);
			
			if (prevChild != null) {
				// alignment of children...
				/*
				ComponentBounds childBounds=child.getComponentBounds();
				ComponentBounds pChildBounds = prevChild.getComponentBounds();
				eqo.addEquality(childBounds.xl, pChildBounds.xl, 0);
				eqo.addEquality(childBounds.xr, pChildBounds.xr, 0);
				eqo.addEquality(childBounds.xt, pChildBounds.xt, 0);
				eqo.addEquality(childBounds.xb, pChildBounds.xb, 0);*/
			}
			prevChild = child;
		}
	}

	public Stack(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);		
	}

}
