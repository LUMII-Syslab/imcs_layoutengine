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
import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class Column extends Component {
	
	protected Column prevColumn = null;	
	
	public void writeGravity(ExtendedQuadraticOptimizer eqo,double coeff) {
		//stores references to first and last child
		Component first=null;
		Component last=null;
		
		double newCoeff = Layout.getChildGravity(coeff, children.size());		
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's left and right borders
			eqo.addLinearDifference(bounds.xl, childBounds.xl, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
			eqo.addLinearDifference(childBounds.xr, bounds.xr, newCoeff*Layout.GRAVITY_WEIGHT_ADJUSTMENT);
						
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			//update first and last child's references
			if (first==null)
				first=child;
			last=child;
		}
		
		//add gravity to form's top and bottom borders
		if (first!=null)
			eqo.addLinearDifference(bounds.xt, first.getComponentBounds().xt, coeff);
		if (last!=null)
			eqo.addLinearDifference(last.getComponentBounds().xb, bounds.xb, coeff);
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
		
		Component prevChild=null;
		Component firstChild=null;
		Component lastChild=null;
		
		Component[] prevColumnChildren = null;
		if (prevColumn != null)
			prevColumnChildren = prevColumn.getChildren();
		if (prevColumnChildren == null)
			prevColumnChildren = new Component[]{};
		int prevColumnI=0;
		
		//consider information about all inner components
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			// equalizing columns...
			if (prevColumnI<prevColumnChildren.length) {
				Component pChild = prevColumnChildren[prevColumnI];
				ComponentBounds pChildBounds = pChild.getComponentBounds();
				
				
				eqo.addEquality(pChildBounds.xt, childBounds.xt, childBounds.topM-pChildBounds.topM);
				eqo.addEquality(childBounds.xb, pChildBounds.xb, childBounds.bottomM-pChildBounds.bottomM);
				
				prevColumnI++;
			}
			
			if (prevChild!=null) {
				//consider vertical spacing
				int verSpace=prevChild.getComponentBounds().bottomM+bounds.verSpace+childBounds.topM;
				eqo.addEquality(prevChild.getComponentBounds().xb, childBounds.xt, verSpace);
			} else
				firstChild=child;
			
			//consider horizontal padding and horizontal alignment 
			if (child.getClass()!=Column.class && bounds.horAlign.equals("LEFT"))
				eqo.addEquality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			else
				eqo.addInequality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			
			if (child.getClass()!=Column.class && bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			else
				eqo.addInequality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, childBounds.xl, childBounds.xr, Layout.ALIGNEMENTWEIGHT);
			
			//update last and previous child
			lastChild=prevChild=child;
		}
		
		if (firstChild!=null) {
			//consider vertical padding and vertical alignment 
			if (bounds.verAlign.equals("TOP")) {
				eqo.addEquality(bounds.xt, firstChild.getComponentBounds().xt,bounds.topP+firstChild.getComponentBounds().topM);
			}
			else {
				eqo.addInequality(bounds.xt, firstChild.getComponentBounds().xt,bounds.topP+firstChild.getComponentBounds().topM);
			}
			
			if (bounds.verAlign.equals("BOTTOM")) {
				eqo.addEquality(lastChild.getComponentBounds().xb,bounds.xb,bounds.bottomP+lastChild.getComponentBounds().bottomM);
			}
			else
				eqo.addInequality(lastChild.getComponentBounds().xb,bounds.xb,bounds.bottomP+lastChild.getComponentBounds().bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, firstChild.getComponentBounds().xt, lastChild.getComponentBounds().xb, Layout.ALIGNEMENTWEIGHT);
		}
	}

	
	protected void updateChildrenSize() {		
		if (parent!=null)
			while (this.children.size() < parent.maxColumnChildren) {
				// adding fake child...
				this.children.add( new LeafComponent(null, 0, this) );
			}
		if (this.prevColumn!=null)
			this.prevColumn.updateChildrenSize();
	}

	
	public Column(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);
		if (_parent!=null) {
			this.prevColumn = _parent.lastColumn;
			_parent.lastColumn = this;
			if (this.children.size() > _parent.maxColumnChildren)
				_parent.maxColumnChildren = this.children.size();
			this.updateChildrenSize();
		}
	}
	
	public void reinitialize() {
		super.reinitialize();
		if (parent!=null) {
			if (this.children.size() > parent.maxColumnChildren)
				parent.maxColumnChildren = this.children.size();
			this.updateChildrenSize();
		}
	}
	
	public void destroyChildrenRecursively(IMCSDialogLayout.ComponentCallback _callback) {
		super.destroyChildrenRecursively(_callback);
		this.prevColumn = null; // since the previous column could be deleted	
	}
}
