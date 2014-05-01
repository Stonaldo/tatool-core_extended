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

import java.util.Iterator;

import ch.tatool.element.Element;
import ch.tatool.exec.ExecutionContext;


/**
 * Iterates through the list of elements, executing each them in order.
 * By default each element is executed once, but this behavior can be overridden
 * restart the iteration as many times as requested.
 * 
 * @author Michael Ruflin
 */
public class IteratedListSelector extends AbstractListSelector {
    
    /** Number of trials this scheduler executes. */
    private int numIterations = 1;
    
    /** Executed executedIterations. */
    private int executedIterations;
    
    /** Iterator that holds the current iteration order. */
    private Iterator<Element> iterator;
    
    public IteratedListSelector() { }
    
    public IteratedListSelector(int numberOfIterations) {
    	this.numIterations = numberOfIterations;
    }
    
    /**
     * Initializes this scheduler.
     */
    public void initialize(Element element) {
    	super.initialize(element);
        executedIterations = 0;
        iterator = null;
    }
    
	public boolean selectNextElement(ExecutionContext context) {
		// create an iterator if necessary
        if (iterator == null || ! iterator.hasNext()) {
        	if (canCreateIterator()) {
                iterator = createIterator(context);
                executedIterations++;
            } else {
                iterator = null;
            }
        }

        // check whether we can push another element
        if (iterator == null || ! canExecuteNext()) {
            return false;
        }
        
        // get the next element and push it onto the stack
        Element next = iterator.next();
        ElementUtils.initialize(next);
        context.getExecutor().getExecutionTree().pushElement(next);
        return true;
	}
	
	/** Returns whether a new iterator can be created. */
	protected boolean canCreateIterator() {
        return ((executedIterations < numIterations) || (numIterations < 0));
	}
	
	/** Can a next element be executed.
	 * By default we execute full iterations, so this method by default returns true
	 */
	protected boolean canExecuteNext() {
		return true;
	}
	
    /** Get the iterator to use for the iteration. */
    protected Iterator<Element> createIterator(ExecutionContext context) {
    	return getExecutionElement().getChildren().iterator();
    }
    
    public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }
    
    public int getExecutedIterations() {
    	return executedIterations;
    }

}
