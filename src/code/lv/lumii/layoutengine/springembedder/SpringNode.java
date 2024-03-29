/*
*
* Copyright (c) 2013-2015 Institute of Mathematics and Computer Science, University of Latvia (IMCS UL). 
*
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

package lv.lumii.layoutengine.springembedder;



/**
 * This auxilary class defines a node of the graph used in the spring
 * embedder algorithm. The main fields are its position in the 3D space,
 * its radius and the force vector.
 */

final class SpringNode extends BaseSpringNode
{
	//force vector
	double dx;
	double dy;
	double dz;

	// accumulated value of spring constants of all edges adjacent to
	// the node
	double fsum;
}

