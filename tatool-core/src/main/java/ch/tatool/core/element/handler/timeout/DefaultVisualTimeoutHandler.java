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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import ch.tatool.core.data.LongProperty;
import ch.tatool.core.display.swing.status.StatusPanel;
import ch.tatool.core.display.swing.status.StatusRegionUtil;
import ch.tatool.core.display.swing.status.TimerStatusPanel;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Timeout handler that gives the user a visual feedback through the status bar
 * 
 * @author Michael Ruflin
 */
public class DefaultVisualTimeoutHandler extends NodeImpl implements ExecutionPhaseListener, TimeoutHandler {

	public static final String PROPERTY_DURATION = "duration";
	public static final String PROPERTY_REACTION_TIME = "reactionTime";

	private String statusPanelId = StatusPanel.STATUS_PANEL_TIMER;
	private long uiRefreshPeriod = 100;
	private static long convertRateToMillis = 1000000;

	/** Timeout used by this handler. */
	private long defaultTimerDuration = 3000;

	/** Timeout currently set. */
	private long timerStartTimestamp = 0;
	private long timerStop = 0;
	private long reactionTime = -1;

	/** Timer used for the timeout. */
	protected Timer timer;

	private TimeoutTask timeoutTask;

	/** Is the timer currently running. */
	private boolean timerRunning = false;

	protected static LongProperty durationProperty = new LongProperty(
			PROPERTY_DURATION);
	protected static LongProperty reactionTimeProperty = new LongProperty(
			PROPERTY_REACTION_TIME);

	/** Holds the status panel used for the display. */
	private StatusPanel statusPanel;

	/** Updates the ui in regular intervals. */
	private StatusPanelUpdaterTask updaterTask;

	public DefaultVisualTimeoutHandler() {
		super();
		System.out.println("running DefaultVisualTimeoutHandler constructor");
	}

	public void startTimeout(ExecutionContext context) {
		// setup status panel
		System.out.println("Running startTimeout");
		if (statusPanel != null) {
			statusPanel.setProperty(TimerStatusPanel.PROPERTY_MIN_VALUE, 0);
			statusPanel.setProperty(TimerStatusPanel.PROPERTY_MAX_VALUE,
					getDefaultTimerDuration());
			statusPanel.setProperty(StatusPanel.PROPERTY_VALUE,
					getDefaultTimerDuration());
			statusPanel.setEnabled(true);
		}

		// start timer task
		timerRunning = true;
		reactionTime = -1;
		timeoutTask = new TimeoutTask();
		timer = new Timer();
		timer.schedule(timeoutTask, defaultTimerDuration);
		timerStartTimestamp = System.nanoTime();

		// start ui updater
		if (statusPanel != null) {
			updaterTask = new StatusPanelUpdaterTask();
			timer.schedule(updaterTask, uiRefreshPeriod, uiRefreshPeriod);
			
		}
	}

	public void cancelTimeout() {
		// stop timer task
		timerStop = System.nanoTime();
		timer.cancel();
		timerRunning = false;
		if (timeoutTask != null) {
			timeoutTask.cancel();

			// calculate the reaction time and make sure it is not bigger than
			// defaultTimerDuration
			reactionTime = (timerStop - timerStartTimestamp)
					/ convertRateToMillis;
			reactionTime = (reactionTime > defaultTimerDuration) ? defaultTimerDuration
					: reactionTime;
			timeoutTask = null;
		}

		// stop updating task
		if (updaterTask != null) {
			updaterTask.cancel();
			updaterTask = null;
		}
	}

	protected void timerElapsed() {
		if (statusPanel != null) {
			// update the ui a last time
			statusPanel.setProperty(StatusPanel.PROPERTY_VALUE, 0);

			// stop updating task
			updaterTask.cancel();
			updaterTask = null;
		}
		
		// stop timer
		timer.cancel();
	}

	private void updateStatusPanel() {
		if (statusPanel != null) {
			// calculate the remaining time and then update the status panel
			long now = System.nanoTime() / convertRateToMillis;// System.currentTimeMillis();
			final long remainingTime = getDefaultTimerDuration()
					+ getTimerStartTimestamp() - now;
			
			final StatusPanel statPanel = statusPanel;
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					statPanel.setProperty(StatusPanel.PROPERTY_VALUE,
							remainingTime);
					statPanel.setEnabled(true);
				}
			});
		}
	}

	public void processExecutionPhase(ExecutionContext event) {
		switch (event.getPhase()) {
		case PRE_PROCESS:
			executionPreProcess(event);
			break;
		case POST_PROCESS:
			executionPostProcess(event);
			break;
		default:
			// do nothing
		}
	}

	private void executionPreProcess(ExecutionContext event) {
		// find the status panel we will be updating
		
		statusPanel = StatusRegionUtil.getStatusPanel(statusPanelId);
	}

	private void executionPostProcess(ExecutionContext context) {
		if (this.getParent() == context.getActiveExecutable()) {
			List<Trial> trials = context.getExecutionData().getTrials();
			if (trials != null && trials.size() > 0) {
				Trial firstTrial = trials.get(0);
	        	durationProperty.setValue(firstTrial, this, defaultTimerDuration);
	        	reactionTimeProperty.setValue(firstTrial, this, reactionTime);
			}
        	
        }
		// cleanup
		statusPanel = null;
	}

	protected long getTimerStartTimestamp() {
		return timerStartTimestamp / convertRateToMillis;
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
	 * UI update task
	 */
	class StatusPanelUpdaterTask extends TimerTask {
		@Override
		public void run() {
			updateStatusPanel();
		}
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
