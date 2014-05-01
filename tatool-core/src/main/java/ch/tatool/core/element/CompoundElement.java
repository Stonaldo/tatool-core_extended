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

import ch.tatool.element.Element;
import ch.tatool.element.Executable;

/**
 * Element that holds two child elements, the execution of which is alternated when a
 * special ExecutionOutcome is returned by a containing element execution.
 * 
 * @author Michael Ruflin
 *
 */
public class CompoundElement extends AbstractElement {

	private Element primary, secondary;
	private List<Element> children;
	
	public CompoundElement() {
		super("comp");
		children = new ArrayList<Element>();
	}
	
	public Element getPrimary() {
		return primary;
	}

	public void setPrimary(Element primary) {
		this.primary = primary;
		assignParent(this, primary);
		updateChildren();
	}

	public Element getSecondary() {
		return secondary;
	}

	public void setSecondary(Element secondary) {
		this.secondary = secondary;
		assignParent(this, secondary);
		updateChildren();
	}

	/** This returns a list containing primary and secondary element. */
	public List<Element> getChildren() {
		return children;
	}
	
	/** Updates the children object. */
	private void updateChildren() {
		children.clear();
		if (primary != null) {
			children.add(primary);
		}
		if (secondary != null) {
			children.add(secondary);
		}
	}
	
	/** Always returns null. */
	public Executable getExecutable() {
		return null;
	}
}
