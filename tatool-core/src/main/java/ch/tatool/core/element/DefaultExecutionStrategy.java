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

import java.util.LinkedList;
import java.util.Queue;

import ch.tatool.core.data.Misc;
import ch.tatool.element.ElementSelector;
import ch.tatool.element.Element;
import ch.tatool.element.ExecutionStrategy;
import ch.tatool.element.ElementTree;
import ch.tatool.element.TemporaryElementSupport;
import ch.tatool.exec.ExecutionContext;

/**
 * Default execution strategy implementation. 
 *
 * This strategy updates the stack so that the top element contains an executable element. 
 * 
 * @author Michael Ruflin.
 */
public class DefaultExecutionStrategy implements ExecutionStrategy, TemporaryElementSupport {

	/** List of temporary elements to be executed. */
	private Queue<Element> temporaryElements;
	
	public DefaultExecutionStrategy() {
		temporaryElements = new LinkedList<Element>();
	}
	
	public void addTemporaryElement(Element element) {
		temporaryElements.add(element);
	}
	
	/** Updates the stack
	 * @return true if another element is ready for execution 
	 */
	public boolean updateElementStack(ExecutionContext executionContext) {
		ElementTree tree = executionContext.getExecutor().getExecutionTree();
		
		// push the root element onto the stack if the stack is empty
		if (tree.getElementStack().isEmpty()) {
			tree.pushElement(tree.getRootElement());
			ElementUtils.initialize(tree.getRootElement());
		}
		
		// make sure we don't loop forever
		int x=0;
		while (x < 100) {
			if (isTopExecutable(tree)) {
				return true;
			}
			else if (runTopSelectors(executionContext, tree)) {
				continue;
			}
			else if (hasTemporaryElement(tree)) {
				continue;
			}
			/*else if (isTopMarkedForReexecution(tree)) {
				return true;
			}*/
			// top element is used, pop it from the stack
			else {
				tree.popElement();
				// check whether the whole tree has been executed
				if (tree.getElementStack().isEmpty()) {
					return false;
				}
			}
		}
		
		// remove the Execution outcome property if set
		Misc.getOutcomeProperty().clearValue(executionContext);
		
		// ensure that the top element contains an executable element
		Element top = tree.getTop();
		return top != null && top.getExecutable() != null;
	}
	
	/** Returns true if the top element contains an element that is yet to be executed. */
	private boolean isTopExecutable(ElementTree tree) {
		Element top = tree.getTop();
		// check whether there is an executable
		if (top != null && top.getExecutable() != null) {
			// check whether it is already executed
			Object executed = top.getProperty(Element.EXECUTED);
			if (executed == null || executed.equals(Boolean.FALSE)) {
				return true;
			}
		}
		return false;
	}
	
	/** Handles the temporary elements. */
	private boolean hasTemporaryElement(ElementTree tree) {
		Element first = temporaryElements.poll();
		if (first != null) {
			tree.pushElement(first);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Runs selectors found on the top element as long as they execute a next element 
	 */
	private boolean runTopSelectors(ExecutionContext executionContext, ElementTree tree) {
		Element top = tree.getTop();
		for (Object handler : top.getHandlers()) {
			if (handler instanceof ElementSelector) {
				boolean adapted = ((ElementSelector) handler).selectNextElement(executionContext);
				if (adapted) {
					return true;
				}
			}
		}
		return false;
	}
	
}
