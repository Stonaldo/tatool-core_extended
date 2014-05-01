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

/**
 * Abstract implementation that provides support for handlers.
 * 
 * @author Michael Ruflin
 */
public abstract class AbstractElement extends NodeImpl implements Element {

	private List<Object> handlers;
	
	public AbstractElement() {
		this.handlers = new ArrayList<Object>();
	}
	
	public AbstractElement(String defaultId) {
		super(defaultId);
		this.handlers = new ArrayList<Object>();
	}

	public List<Object> getHandlers() {
		return handlers;
	}

	public void setHandlers(List<Object> handlers) {
		this.handlers = handlers;
		assignParent(this, handlers);
	}
	
	public void addHandler(Object handler) {
		handlers.add(handler);
		assignParent(this, handler);
	}
	
	public void removeHandler(Object handler) {
		handlers.remove(handler);
		assignParent(null, handler);
	}
}
