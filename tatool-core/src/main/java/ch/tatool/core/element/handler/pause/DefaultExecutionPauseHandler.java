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
package ch.tatool.core.element.handler.pause;

import ch.tatool.core.element.NodeImpl;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhase;
import ch.tatool.exec.ExecutionPhaseListener;
import ch.tatool.exec.PhaseRunnable;
import ch.tatool.exec.PhaseRunnableManager;

/**
 * Pauses the execution between two elements.
 */
public class DefaultExecutionPauseHandler extends NodeImpl implements ExecutionPhaseListener, ExecutionPauseHandler {
	
	private static final int DEFAULT_PAUSE_DURATION = 500;
	
    /** Default pause duration between two tasks. */
    private long defaultInterElementPauseDuration = DEFAULT_PAUSE_DURATION;
    
    /** Current pause duration between two tasks. */
    private long currentInterElementPauseDuration;
	
    /** PhaseRunnable that stops the execution thread. */
    private ExecutionPauser pauser;
    
	public DefaultExecutionPauseHandler() {
		super("executor-pause-handler");
		pauser = new ExecutionPauser();
	}
	
    public void processExecutionPhase(ExecutionContext event) {
    	switch(event.getPhase()) {
    	case SESSION_START:
    		registerPauser(event);
    		break;
    	case SESSION_FINISH:
    		unregisterPauser(event);
    		break;
    	case PRE_PROCESS:
    		resetPauseDuration();
    		break;
    	case POST_PROCESS:
    		//postProcess(event);
    		break;
    	default:
    		// do nothing
    	}
    }
    
    private void registerPauser(ExecutionContext context) {
    	// to execute the pause
    	PhaseRunnableManager execManager = context.getExecutor().getPhaseRunnableManager();
    	execManager.addPhaseExecutable(pauser, ExecutionPhase.PRE_EXECUTABLE_EXECUTION, true);
    	// to fetch what pause duration should be used in the next pre element execution
    	execManager.addPhaseExecutable(pauser, ExecutionPhase.POST_EXECUTABLE_EXECUTION, true);
    }
    
    private void unregisterPauser(ExecutionContext context) {
    	PhaseRunnableManager execManager = context.getExecutor().getPhaseRunnableManager();
    	execManager.removePhaseExecutable(pauser, ExecutionPhase.PRE_EXECUTABLE_EXECUTION);
    	execManager.removePhaseExecutable(pauser, ExecutionPhase.POST_EXECUTABLE_EXECUTION);
    }
	
	/** Overwrite the duration for which the module is paused before execution the next element. */
	public void setCurrentInterElementPauseDuration(long duration) {
		currentInterElementPauseDuration = duration;
	}
	
	public long getCurrentInterElementPauseDuration() {
		return currentInterElementPauseDuration;
	}

	/** Set the default duration for which the execution is paused in between two elements. */
	public void setDefaultInterElementPauseDuration(long duration) {
		defaultInterElementPauseDuration = duration;
	}
	
	public long getDefaultInterElementPauseDuration() {
		return defaultInterElementPauseDuration;
	}
	
    /** Resets the pause duration. */
    public void resetPauseDuration() {
    	currentInterElementPauseDuration = defaultInterElementPauseDuration;
    }
    
    /**
     * PhaseRunnable that stops the execution thread for a predefined duration
     */
    class ExecutionPauser implements PhaseRunnable {

    	private Thread thread;
    	private long duration = 0;
    	
    	public void setDuration(long duration) {
    		this.duration = duration;
    	}
    	
		public void run(ExecutionContext context) {
			switch (context.getPhase()) {
			case PRE_EXECUTABLE_EXECUTION:
				// before the element execution, pause the time we previously stored
				if (duration <= 0) return;
				thread = Thread.currentThread();
				try {
		            Thread.sleep(duration);
		        } catch (InterruptedException ie) {
		        	// ok, just return
		        }
		        thread = null;
		        break;
			case POST_EXECUTABLE_EXECUTION:
				// after the element has been executed, store the duration we want to wait fore before the next
				// element execution
				setDuration(getCurrentInterElementPauseDuration());
			default:
				break;
			}
		}

		public void stop() {
			// wake the thread up if possible
			try {
				if (thread != null) {
					thread.interrupt();
				}
			} catch (NullPointerException npe) {
				// bad luck
			}
		}
    	
    }
}
