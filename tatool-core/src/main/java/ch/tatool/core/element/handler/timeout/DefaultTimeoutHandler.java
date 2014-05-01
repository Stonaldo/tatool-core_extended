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
package ch.tatool.core.element.handler.timeout;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.data.LongProperty;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Aspect that provides task timeout capability.
 * This aspect only makes sense when attached to a task.
 * 
 * @author Michael Ruflin
 */
public class DefaultTimeoutHandler extends NodeImpl implements ExecutionPhaseListener, TimeoutHandler {
    
    public static final String PROPERTY_DURATION = "duration";
    public static final String PROPERTY_REACTION_TIME = "reactionTime";
    
    Logger logger = LoggerFactory.getLogger(DefaultTimeoutHandler.class);
    
    /** Set to true to log the reaction and timeout information to the trial. */
    private boolean logTrialProperties = true;
    
    /** Timeout used by this handler. */
    private long defaultTimerDuration = 3000;

	/** Timeout currently set. */
    private long timerStartTimestamp = 0;
    private long timerStop = 0;
    private long reactionTime = -1;
    private static long convertRateToMillis = 1000000;

    /** Timer used for the timeout. */
    protected Timer timer;
    
    private TimeoutTask timeoutTask;
    
    /** Is the timer currently running. */
    private boolean timerRunning = false;
    
    private ExecutionContext context;
    
    protected static LongProperty durationProperty = new LongProperty(PROPERTY_DURATION);
    protected static LongProperty reactionTimeProperty = new LongProperty(PROPERTY_REACTION_TIME);
    
    public DefaultTimeoutHandler() {
        super();
    }
    
    /**
     * Starts the timer.
     */
    public void startTimeout(ExecutionContext context) {
        logger.info("Starting timeout timer with delay of {} milliseconds", defaultTimerDuration);
        
        this.context = context;
        timerRunning = true;
        reactionTime = -1;
        timeoutTask = new TimeoutTask();
        timer = new Timer();
        timer.schedule(timeoutTask, defaultTimerDuration);
        timerStartTimestamp = System.nanoTime();
    }
    
    /**
     * Cancels the timeout.
     */
    public void cancelTimeout() {
        timerStop = System.nanoTime();
        timer.cancel();
        timerRunning = false;
        if (timeoutTask != null) {
            timeoutTask.cancel();
          
            // calculate the reaction time and make sure it is not bigger than defaultTimerDuration
            reactionTime = (timerStop - timerStartTimestamp)/convertRateToMillis;
            reactionTime = (reactionTime > defaultTimerDuration) ? defaultTimerDuration : reactionTime;
            timeoutTask = null;
        }
        logger.info("Timer stopped.");
    }

    protected void timerElapsed() {
    	timer.cancel();
    	if (context.getActiveExecutable() != null) {
    		context.getActiveExecutable().cancel();
    	}
    }
    
    protected long getTimerStartTimestamp() {
    	return timerStartTimestamp/convertRateToMillis;
    }
    
    public void processExecutionPhase(ExecutionContext context) {
    	switch(context.getPhase()) {
    	case POST_PROCESS:
    		executionPostProcess(context);
    		break;
    	default:
    		// do nothing
    	}
    }
    
    /** Set the timeout in the trial properties. */
    private void executionPostProcess(ExecutionContext context) {
        // only set if we are applied to the current task
        if (this.getParent() == context.getActiveExecutable() && logTrialProperties) {
        	Trial firstTrial = context.getExecutionData().getCreateFirstTrial();
        	durationProperty.setValue(firstTrial, this, defaultTimerDuration);
        	reactionTimeProperty.setValue(firstTrial, this, reactionTime);
        }
    }
    
    protected Timer getTimer() {
    	return timer;
    }
    
    public boolean isLogTrialProperties() {
        return logTrialProperties;
    }

    /** Set whether timeout information should be logged in the trial. */
    public void setLogTrialProperties(boolean logTrialProperties) {
        this.logTrialProperties = logTrialProperties;
    }

    public long getDefaultTimerDuration() {
        return defaultTimerDuration;
    }

    public void setDefaultTimerDuration(long defaultTimerDuration) {
        this.defaultTimerDuration = defaultTimerDuration;
    }

    public long getReactionTime() {
		return reactionTime;
	}

	public void setReactionTime(long reactionTime) {
		this.reactionTime = reactionTime;
	}

    /**
     * Timeout task.
     */
    class TimeoutTask extends TimerTask {
        @Override
        public void run() {
            if (timerRunning) {
            	timerElapsed();
            }
        }
    }
}
