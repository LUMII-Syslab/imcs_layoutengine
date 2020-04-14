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

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

import java.util.ArrayList;
import java.util.Arrays;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class Row extends Component {
	
	protected Row prevRow = null;
		
	public void writeGravity(ExtendedQuadraticOptimizer eqo, double coeff) {
		//stores references to first and last child
		Component first=null;
		Component last=null;
		
		double newCoeff = Layout.getChildGravity(coeff, children.size());
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's top and bottom borders
			eqo.addLinearDifference(bounds.xt, childBounds.xt, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			eqo.addLinearDifference(childBounds.xb, bounds.xb, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			//update first and last child's references
			if (first==null)
				first=child;
			last=child;
		}
		
		//add gravity to form's left and right borders
		if (first!=null) {
			eqo.addLinearDifference(bounds.xl, first.getComponentBounds().xl, coeff);
		}
		if (last!=null) {
			eqo.addLinearDifference(last.getComponentBounds().xr, bounds.xr, coeff);
		}
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
		
		Component prevChild=null;
		Component firstChild=null;
		Component lastChild=null;
		
		Component[] prevRowChildren = null;
		if (prevRow != null)
			prevRowChildren = prevRow.getChildren();
		if (prevRowChildren == null)
			prevRowChildren = new Component[]{};
		int prevRowI=0;
		
		//for every child
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			// equalizing rows...
			if (prevRowI<prevRowChildren.length) {
				Component pChild = prevRowChildren[prevRowI];
				ComponentBounds pChildBounds = pChild.getComponentBounds();

				eqo.addEquality(pChildBounds.xl, childBounds.xl, childBounds.leftM-pChildBounds.leftM);
				eqo.addEquality(childBounds.xr, pChildBounds.xr, childBounds.rightM-pChildBounds.rightM);
				
				prevRowI++;
			}
			
			if (prevChild!=null) {
				//consider horizontal spacing
				int horSpace=prevChild.getComponentBounds().rightM+bounds.horSpace+childBounds.leftM;
				//eqo.addInequality(prevChild.getComponentBounds().xr, childBounds.xl, horSpace);
				eqo.addEquality(prevChild.getComponentBounds().xr, childBounds.xl, horSpace);
			} else
				firstChild=child;
			
			//consider vertical padding and vertical alignment
			if (bounds.verAlign.equals("TOP"))
				eqo.addEquality(bounds.xt, childBounds.xt, bounds.topP+childBounds.topM);
			else
				eqo.addInequality(bounds.xt, childBounds.xt, bounds.topP+childBounds.topM);
			
			if (bounds.verAlign.equals("BOTTOM"))
				eqo.addEquality(childBounds.xb, bounds.xb, bounds.bottomP+childBounds.bottomM);
			else
				eqo.addInequality(childBounds.xb, bounds.xb, bounds.bottomP+childBounds.bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, childBounds.xt, childBounds.xb, Layout.ALIGNEMENTWEIGHT);
			
			//update last and previous child
			lastChild=prevChild=child;
		}
			
		if (firstChild!=null) {
			//consider horizontal padding and horizontal alignment
			if (bounds.horAlign.equals("LEFT")) {
				eqo.addEquality(bounds.xl, firstChild.getComponentBounds().xl,bounds.leftP+firstChild.getComponentBounds().leftM);
			}
			else
				eqo.addInequality(bounds.xl, firstChild.getComponentBounds().xl,bounds.leftP+firstChild.getComponentBounds().leftM);
			
			if (bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			else
				eqo.addInequality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, firstChild.getComponentBounds().xl, lastChild.getComponentBounds().xr, Layout.ALIGNEMENTWEIGHT);
		}
		
		if (prevRow != null) {
			if (parent!=null)
				eqo.addInequality(prevRow.getComponentBounds().xb, this.getComponentBounds().xt, parent.getComponentBounds().verSpace+prevRow.getComponentBounds().bottomM+this.getComponentBounds().topM);			
			else
				eqo.addInequality(prevRow.getComponentBounds().xb, this.getComponentBounds().xt, 0);			
		}
	}
	
	protected void updateChildrenSize() {		
		if (parent!=null)
			while (this.children.size() < parent.maxRowChildren) {
				// adding fake child...
				this.children.add( new LeafComponent(null, 0, this) );
			}
		if (this.prevRow!=null)
			this.prevRow.updateChildrenSize();
	}

	public Row(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);
		if (_parent!=null) {
			this.prevRow = _parent.lastRow;
			_parent.lastRow = this;
			if (this.children.size() > _parent.maxRowChildren)
				_parent.maxRowChildren = this.children.size();
			this.updateChildrenSize();
		}
	}
	
	public void reinitialize() {
		super.reinitialize();
		if (parent!=null) {
			if (this.children.size() > parent.maxRowChildren)
				parent.maxRowChildren = this.children.size();
			this.updateChildrenSize();
		}		
	}
	
	public void destroyChildrenRecursively(IMCSDialogLayout.ComponentCallback _callback) {
		super.destroyChildrenRecursively(_callback);
		this.prevRow = null; // since the previous row could be deleted	
	}
}
