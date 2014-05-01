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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.element.AbstractListSelector;
import ch.tatool.core.element.ElementUtils;
import ch.tatool.element.Element;
import ch.tatool.exec.ExecutionContext;

/**
 * Iterates through the list of elements, executing each them in order.
 * A general timer cancels the execution of the current element.
 * 
 * @author Andre Locher
 */
public class TimerListSelector extends AbstractListSelector implements ExecutionStartHandler {
    
	private static Logger logger = LoggerFactory.getLogger(TimerListSelector.class);
	
	/** Number of trials this scheduler executes. */
    private int numIterations = -1;
    
    /** Executed executedIterations. */
    private int executedIterations;
    
    /** Time this scheduler executes. */
	private long timeout = 10000;

    /** Iterator that holds the current iteration order. */
    private Iterator<Element> iterator;
    
    private TimerTask stopTask;
    private boolean timerRunning;
    private boolean canCreateIterator;
    private Timer execTimer;
    
    public TimerListSelector() { }
    
    public TimerListSelector(int timeoutSeconds) {
    	this.timeout = timeoutSeconds;
    }
    
    /**
     * Initializes this scheduler.
     */
    public void initialize(Element element) {
    	super.initialize(element);
        iterator = null;
        execTimer = new Timer();
        canCreateIterator = true;
        timerRunning = false;
        executedIterations = 0;
    }
    
	public boolean selectNextElement(ExecutionContext context) {
		// create an iterator if necessary
		if (canCreateIterator) {
			 if (iterator == null || ! iterator.hasNext()) {
		        	if (canCreateIterator()) {
		                iterator = createIterator(context);
		                executedIterations++;
		            } else {
		            	stopTimer();
		                iterator = null;
		            }
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
		return (executedIterations < numIterations) || (numIterations < 0);
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
    
    public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	
	public int getNumIterations() {
        return numIterations;
    }

    public void setNumIterations(int numIterations) {
        this.numIterations = numIterations;
    }

	public void startExecution(final ExecutionContext context) {
		if (!timerRunning) {
			timerRunning = true;
			stopTask = new TimerTask() {
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							stopExecution(context);
						}
					});
				}
			};
			execTimer.schedule(stopTask, timeout);
		}
	}

	public void stopExecution(ExecutionContext context) {
		stopTimer();
		if (context != null) {
			context.getExecutor().stopCurrentElementExecution();
		}
		logger.info("Stopping execution after timeout.");
	}
	
	public void stopTimer() {
		stopTask.cancel();
		canCreateIterator = false;
		iterator = null;
		timerRunning = false;
	}

}
