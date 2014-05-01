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

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.element.Executable;

/**
 * Blocking executable element implementation
 * 
 * This implementation blocks the executable thread until finishExecution is called.
 * Prior to calling finishExecution, a lock needs to be acquired. This way each thread
 * finishing can first check whether it is "the first" or simply abort its operation in
 * case it is not.
 * 
 * @author Michael Ruflin
 */
public abstract class BlockingAWTExecutable extends BlockingExecutable implements Executable {
    
    private Logger logger = LoggerFactory.getLogger(BlockingAWTExecutable.class);
    
    public BlockingAWTExecutable() {
    	super();
    }
    
    public BlockingAWTExecutable(String defaultId) {
        super(defaultId);
    }
    
    /**
     * Calls startExecutionAWT on the awt thread.
     */
    protected final void startExecution() {
    	// give the task the chance to kick off animations, timers and such stuff
    	try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                	startExecutionAWT();
                }
            });
        } catch (InvocationTargetException e) {
        	logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
        	logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * To be implemented. Called at the start of the executable execution.
     */
    protected abstract void startExecutionAWT();
    
    /**
     * Calls cancelExecutionAWT on the awt thread.
     */
    protected final void cancelExecution() {
    	if (SwingUtilities.isEventDispatchThread()) {
        	cancelExecutionAWT();
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                    	cancelExecutionAWT();
                    }
                });
            } catch (InvocationTargetException e) {
            	logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
            	logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
    }
    
    /** cancelExecution called in the awt thread. */
    protected void cancelExecutionAWT() {
    }
}
