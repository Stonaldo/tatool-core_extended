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
import java.util.ListIterator;

import ch.tatool.element.Element;
import ch.tatool.element.Initializable;
import ch.tatool.exec.ExecutionContext;

public class ElementUtils {

	/** Calls initialize on all objects contained in the element that implement Initializable. */
	public static void initialize(Element element) {
		// clear the properties of the element
		element.clearProperties();
		
		// initialize the executable if requested
		Object executable = element.getExecutable();
		if (executable != null) {
			ElementUtils.initialize(element, executable);
		}
		
		// initialize the handlers
		for (Object handler : element.getHandlers()) {
			ElementUtils.initialize(element, handler);
		}
	}

	/** Initialize an object with the given parent Element.
	 * This method does nothing if object does not implement Initializable
	 * @param parent
	 * @param object
	 */
	public static void initialize(Element parent, Object object) {
		if (object instanceof Initializable) {
			((Initializable) object).initialize(parent);
		}
	}

	/**
	 * Finds a handler or element in the current stack by a specific type
	 * 
	 * This method searches from the top of the stack down to the root element.
	 */
	public static Object findHandlerInStackByType(ExecutionContext context, Class<?> type) {
		List<Element> elements = context.getElementStack();
		ListIterator<Element> iterator = elements.listIterator(elements.size());
		while (iterator.hasPrevious()) {
			Element element = iterator.previous();
			for (Object handler : element.getHandlers()) {
				if (type.isAssignableFrom(handler.getClass())) {
					return handler;
				}
			}
		}
		return null;
	}
	
	/**
	 * Finds list of handlers or elements in the current stack by a specific type
	 * 
	 * This method searches from the top of the stack down to the root element.
	 */
	public static List<Object> findHandlersInStackByType(ExecutionContext context, Class<?> type) {
		List<Object> handlers = new ArrayList<Object>();
		List<Element> elements = context.getElementStack();
		ListIterator<Element> iterator = elements.listIterator(elements.size());
		while (iterator.hasPrevious()) {
			Element element = iterator.previous();
			for (Object handler : element.getHandlers()) {
				if (type.isAssignableFrom(handler.getClass())) {
					handlers.add(handler);
				}
			}
		}
		return handlers;
	}
}
