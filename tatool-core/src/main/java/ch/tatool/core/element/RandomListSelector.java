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

import ch.tatool.element.Element;
import ch.tatool.exec.ExecutionContext;


/**
 * Serves randomly a new element from the group.
 * 
 * The amount of elements served can be specified.
 * 
 * @author Michael Ruflin
 */
public class RandomListSelector extends AbstractListSelector {
    
    /** Number of trials this scheduler executes. */
    private int numIterations = -1;
    
    /** Trials executed so far. */
    private int executedIterations = 0;
    
    public RandomListSelector() {}
    
    public RandomListSelector(int numberOfTrials) {
    	this.numIterations = numberOfTrials;
    }
    
    /**
     * Initializes this scheduler.
     */
    public void initialize(Element element) {
        super.initialize(element);
        executedIterations = 0;
    }

	public boolean selectNextElement(ExecutionContext executionContext) {
		// return if we got a specific amount of trials to execute
		if (-1 < numIterations && numIterations <= executedIterations) {
			return false;
		}
		List<Element> elements = getExecutionElement().getChildren();
        int elementCount = (int) elements.size();
        if (elementCount < 1) {
        	return false;
        }
        
        // push a random element
        int index = ((int) ((Math.random() * (long) elementCount))) % elementCount;
        Element element = elements.get(index);
        ElementUtils.initialize(element);
        executionContext.getExecutor().getExecutionTree().pushElement(element);
        
        // increment the trials returned
        executedIterations++;
        return true;
	}

    /** Get the number of trials displayed. */
    public int getNumIterations() {
        return numIterations;
    }

    /** Set the number of trials returned.
     * @param numberOfTrials the number of trials or -1 to return infinite trials
     */
    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }    
}
