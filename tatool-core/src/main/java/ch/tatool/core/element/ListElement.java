/*******************************************************************************
 * Copyright (c) 2011 Michael Ruflin, André Locher, Claudia von Bastian.
 * 
 * This file is part of Tatool.
 * 
 * Tatool is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published 
 * by the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Tatool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Tatool. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ch.tatool.core.element;

import java.util.ArrayList;
import java.util.List;

import ch.tatool.element.Executable;
import ch.tatool.element.Element;

/**
 * Execution element that holds/manages a list of other execution elements.
 *  
 * @author Michael Ruflin
 */
public class ListElement extends AbstractElement {

	/** List of child execution elements. */
	private List<Element> children;
	
	public ListElement() {
		super("list");
		children = new ArrayList<Element>();
	}

	public List<Element> getChildren() {
		return children;
	}

	public void setChildren(List<Element> children) {
		this.children = children;
		assignParents(this, children);
	}
	
	public void addChild(Element child) {
		children.add(child);
		assignParent(this, child);
	}
	
	public void removeChild(Element child) {
		children.remove(child);
		assignParent(null, child);
	}

	/** Always returns null as this execution element only acts as a container for other elements. */ 
	public Executable getExecutable() {
		return null;
	}
}
