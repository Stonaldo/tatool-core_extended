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
package ch.tatool.core.element.handler;

import ch.tatool.core.data.Misc;
import ch.tatool.core.display.swing.status.StatusPanel;
import ch.tatool.core.display.swing.status.StatusRegionUtil;
import ch.tatool.core.element.CompoundElement;
import ch.tatool.core.element.CompoundSelector;
import ch.tatool.core.element.ElementUtils;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionOutcome;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Counts the number of executed trials
 * 
 * TODO: split out the UI update into a separate handler
 */
public class TrialCountHandler extends NodeImpl implements ExecutionPhaseListener {

	/** Id used to display the outcome. */
	private String statusPanelId = StatusPanel.STATUS_PANEL_TRIAL;
	
	/**
	 * Internal trial counter.
	 */
	private int trialCounter;
	
	public TrialCountHandler() {
		super("trial-count-handler");
	}

	private void initialize() {
		trialCounter = 1;
    }
	
    public void processExecutionPhase(ExecutionContext context) {
    	switch(context.getPhase()) {
    	case SESSION_START:
    		initialize();
    		break;
    	case PRE_PROCESS:
    		preProcess(context);
    		break;
    	case POST_PROCESS:
    		postProcess(context);
    		break;
    	default:
    		// do nothing
    	}
    }
	
	private void preProcess(ExecutionContext context) {
		// only update if we are not skipping the current executable
		String outcome = Misc.getOutcomeProperty().getValue(context.getActiveExecutable());
		if (trialCounter == 1 || (outcome != null && !outcome.equals(ExecutionOutcome.SKIP))) {
			updateStatusPanel(context);
		}
	}
	
	/**
	 * Updates the statuspanel with the current trial count. 
	 */
	private void updateStatusPanel(ExecutionContext context) {
		// get the status panel if available
		StatusPanel panel = StatusRegionUtil.getStatusPanel(statusPanelId);
		if (panel != null) {
			panel.setEnabled(true);
			panel.setProperty(StatusPanel.PROPERTY_VALUE, trialCounter);
		}
	}
	
	/**
	 * Increments the trial counter, depending on the trial outcome
	 */
	private void postProcess(ExecutionContext context) {
		if (isCompoundDone(context)) {
			trialCounter += getTrialCount(context);
			//trialCounter += 1;
		} else {
			
		}
		//trialCounter += getTrialCount(context);
	}
	
	/** Calculates the trial count for the context.
	 * This method delegates the actual calculates to the bottom-most TrialCountEvaluator
	 */
	private int getTrialCount(ExecutionContext context) {

		// Find the first trial count handler - that one is used to evaluate what
		// value should be fetched
		TrialCountEvaluator evaluator = (TrialCountEvaluator)
			ElementUtils.findHandlerInStackByType(context, TrialCountEvaluator.class);
		
		// add the count provided by the evaluator
		if (evaluator != null) {
			return evaluator.getTrialCount(context);
		} else {
			return 0;
		}
	}
	
	/**
	 * Checks whether the current trial is complete. The algorithm only gets
	 * triggered if a compound element is finished
	 * 
	 * @return whether the trial is complete
	 */
	private boolean isCompoundDone(ExecutionContext context) {

		// CompoundElement
		if (this.getParent() instanceof CompoundElement) {
			CompoundElement comp = (CompoundElement) this.getParent();

			for (Object handler : comp.getHandlers()) {
				if (handler instanceof CompoundSelector) {
					CompoundSelector selector = (CompoundSelector) handler;
					return selector.isDone();
				}
			}

			// other Element types
		} else {
			return true;
		}
		
		return true;
	}
	
	public String getStatusPanelId() {
		return statusPanelId;
	}

	public void setStatusPanelId(String statusPanelId) {
		this.statusPanelId = statusPanelId;
	}

	public int getTrialCounter() {
		return trialCounter;
	}

	public void setTrialCounter(int trialCounter) {
		this.trialCounter = trialCounter;
	}
	
}
