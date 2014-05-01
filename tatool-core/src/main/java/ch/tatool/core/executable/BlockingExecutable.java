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
package ch.tatool.core.executable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.element.NodeImpl;
import ch.tatool.element.Executable;
import ch.tatool.exec.ExecutionContext;

/**
 * Blocking executable element implementation
 * 
 * This implementation blocks the executable thread until finishExecution is called.
 * 
 * Prior to calling finishExecution, a lock needs to be acquired. This way each thread
 * finishing can first check whether it is "the first" or simply abort its operation in
 * case it is not.
 * 
 * @author Michael Ruflin
 */
public abstract class BlockingExecutable extends NodeImpl implements Executable {
    
    private Logger logger = LoggerFactory.getLogger(BlockingExecutable.class);
    
    /** ExecutionContext provided by the Executor. */
    private ExecutionContext executionContext;
    
    /** Thread that called execute(). */
    private Thread executeCallerThread;
    
    /** Finishing thread.
     * 
     * Finish can be called from different place, timeout threads, user input,
     * call to cancel etc. This variable holds a reference to the thread that
     * was granted finishing rights.
     */
    private Thread finishingThread;
    
    /** Boolean that signals whether stop has been called already. */
    private boolean running = false;
    
    public BlockingExecutable() {
    	super();
    }
    
    public BlockingExecutable(String defaultId) {
        super(defaultId);
    }
    
    public ExecutionContext getExecutionContext() {
    	return executionContext;
    }
    
    public void setExecutionContext(ExecutionContext executionContext) {
    	this.executionContext = executionContext;
    }
    
    /**
     * Implementation of execute.
     * 
     * Will call startExecution on the awt thread and then
     * block the caller thread until finishExecution is called
     */
    public final void execute() {
        // initialize
    	synchronized(this) {
    		running = true;
    		executeCallerThread = Thread.currentThread();
    		finishingThread = null;
    	}

        // start execution
    	startExecution();
        
        // put current thread to sleep until we wake it up with notify()
        // only wait as long as we don't have a request for finishing an outcome
        while (finishingThread == null) {
            try {
                synchronized(executeCallerThread) {
                    executeCallerThread.wait();
                }
            } catch(InterruptedException e) {
                logger.warn("TaskOutcome thread interrupted", e);
                e.printStackTrace();
            }
        }
        
        // cleanup
        synchronized(this) {
        	executeCallerThread = null;
        	running = false;
        }
    }
    
    /**
     * Start actual execution. 
     */
    protected abstract void startExecution();
    
    /**
     * Request the lock to finish execution.
     * 
     * This method needs to be called prior to calling finishExecution.
     * Calling finishExecution is only allowed for the thread that
     * receives true as a result.
     */
    protected synchronized boolean getFinishExecutionLock() {
    	if (running && finishingThread == null) {
    		finishingThread = Thread.currentThread();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * Call this method to finish execution.
     * 
     * Note that you need to request a lock from getFInishExecutionLock before calling this method.
     * This method will release the Executors thread, so finish any work *before* calling this method.
     */
    protected void finishExecution() {
    	// concurrency check
    	if (finishingThread == null) {
    		throw new IllegalStateException("You need first to fetch true from getFinishExecutionLock() before calling this method.");
    	} else if (finishingThread != Thread.currentThread()) {
    		throw new IllegalStateException("Another thread was attributed with the finishExecutionLock!.");
    	}
    	
    	// wake up the element execution thread, but first set the outcome
		synchronized(executeCallerThread) {
			executeCallerThread.notifyAll();
		}
	}
    
    /**
     * Cancels the task.
     * 
     * This method fetches the finish execution lock and if successful calls cancelExecution.
     * Once cancelExecution returns finishExecution is called
     */
    public void cancel() {
    	boolean first = getFinishExecutionLock();
    	if (first) {
    		cancelExecution();
    		finishExecution();
    	}
    }
    
    /** Called when cancel successfully acquires the finish execution lock. */
    protected void cancelExecution() {
    }
}
