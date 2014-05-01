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

import java.util.List;
import java.util.ListIterator;

import ch.tatool.element.Element;
import ch.tatool.element.ExecutionStrategy;
import ch.tatool.element.TemporaryElementSupport;
import ch.tatool.exec.ExecutionContext;

/**
 * Use this helper class to add temporary elements to the execution.
 * 
 * The actual execution order depends on the implementor of TemporaryExecutionElementSupport
 * 
 * @author Michael Ruflin
 */
public class TemporaryElementSupportUtil {

	/** Static instance object - protected so that subclasses could overwrite the default
	 * with a different implementation.
	 */
	protected static TemporaryElementSupportUtil INSTANCE = new TemporaryElementSupportUtil();

	/** Get the util instance. */
	public static TemporaryElementSupportUtil getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Finds a TemporaryElementSupport object.
	 * 
	 * The default implementation searches through all handlers and finally checks the strategy object
	 * 
	 * @param context the ExecutionContext to search through
	 * @return an object or null if none could be found
	 */
	public TemporaryElementSupport findTemporaryElementSupport(ExecutionContext context) {
		// find a handler that provides the support
		List<Element> elements = context.getElementStack();
		ListIterator<Element> iterator = elements.listIterator(elements.size());
		while (iterator.hasPrevious()) {
			Element e = iterator.previous();
			for (Object handler : e.getHandlers()) {
				if (handler instanceof TemporaryElementSupport) return (TemporaryElementSupport) handler;
			}
		}
		
		// check the strategy
		ExecutionStrategy strategy = context.getExecutor().getExecutionStrategy(); 
		if (strategy instanceof TemporaryElementSupport) {
			return (TemporaryElementSupport) strategy; 
		}
		
		// not found
		return null;
	}
}
