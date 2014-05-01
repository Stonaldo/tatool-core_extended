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
import ch.tatool.core.element.NodeImpl;
import ch.tatool.element.Executable;
import ch.tatool.exec.ExecutionContext;

/**
 * This executable element implementation provides support
 * for executing the code in the AWT event dispatch thread, thus
 * making sure that changes to the task display from within the executable
 * work as expected.
 * 
 * Note: Use the BlockingAWTExecutable for implementations that require
 * user input in order to finish the execution.
 * 
 * @author Michael Ruflin
 */
public abstract class NonBlockingAWTExecutable extends NodeImpl implements Executable {
  
    private ExecutionContext executionContext;
    
    public ExecutionContext getExecutionContext() {
    	return executionContext;
    }
    public void setExecutionContext(ExecutionContext executionContext) {
    	this.executionContext = executionContext;
    }
    
    public NonBlockingAWTExecutable() {
    	super();
    }
    
    public NonBlockingAWTExecutable(String defaultId) {
    	super(defaultId);
    }
    
    /**
     * Implementation of executeTask.
     * 
     * Calls doExecutionAWT then returns.
     */
    public final void execute() {	
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    executeAWT();
                }
            });
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        }
    }
    
    /**
     * Empty implementation - stopping is not implemented as element execution is expected to be quick anyways
     */
    public void cancel() {
    	// do nothing, execute() should be fast enough
    }
    
    // Abstract methods
    
    /**
     * Subclasses implement this method to execute some code.
     * 
     * It is guaranteed that the caller thread is the AWT thread.
     */
    protected abstract String executeAWT();
}
