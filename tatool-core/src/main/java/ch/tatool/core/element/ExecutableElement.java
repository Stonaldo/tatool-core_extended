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

import java.util.Collections;
import java.util.List;

import ch.tatool.element.Element;
import ch.tatool.element.Executable;

/**
 * Default implementation of an execution element which does not support child elements.
 * 
 * @author Michael Ruflin
 */
public class ExecutableElement extends AbstractElement {

	private Executable executable;
	
	public ExecutableElement() {
		super("exec");
	}
	
	public ExecutableElement(Executable executable) {
		this();
		setExecutable(executable);
	}
	
	/** Get all children. This method always returns an empty list. */
	public List<Element> getChildren() {
		return Collections.emptyList();
	}

	public Executable getExecutable() {
		return executable;
	}

	public void setExecutable(Executable executable) {
		this.executable = executable;
		if (executable != null) {
			assignParent(this, executable);
		}
	}
}
