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

import lv.lumii.dialoglayout.components.Component;

public class RelativeInfo {
	public Component obj;
	public Double rMin=null;
	public Double rPre=null;
	public Double rMax=null;
	public boolean vertical=false; 
	
	
	public void updateMin(Double k) {
		if (k==null || rMin==null)
			return;
		if (vertical)
			obj.getComponentBounds().minH=(int) (k*rMin);
		else
			obj.getComponentBounds().minW=(int) (k*rMin);
	}
	
	public void updateMax(Double k) {
		if (k==null || rMax==null)
			return;
		if (vertical)
			obj.getComponentBounds().maxH=(int)(k*rMax);
		else
			obj.getComponentBounds().maxW=(int)(k*rMax);
	}
	
	public Double getMinK() {
		if (rMin==null)
			return null;
		if (vertical) {
			if (obj.getComponentBounds().minH==null)
				return null;
			return obj.getComponentBounds().minH/rMin;
		}
		if (obj.getComponentBounds().minW==null)
			return null;
		return obj.getComponentBounds().minW/rMin;
	}
	
	public Double getMaxK() {
		if (rMax==null)
			return null;
		if (vertical) {
			if (obj.getComponentBounds().maxH==null)
				return null;
			return obj.getComponentBounds().maxH/rMax;
		}
		if (obj.getComponentBounds().maxW==null)
			return null;
		return obj.getComponentBounds().maxW/rMax;
	}
	
}
