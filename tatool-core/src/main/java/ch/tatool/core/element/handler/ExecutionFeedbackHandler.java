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

import ch.tatool.core.data.Result;
import ch.tatool.core.display.swing.status.CorrectWrongStatusPanel;
import ch.tatool.core.display.swing.status.StatusPanel;
import ch.tatool.core.display.swing.status.StatusRegionUtil;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Handler that updates the attributed statuspanel with the result of the last trial
 */
public class ExecutionFeedbackHandler extends NodeImpl implements ExecutionPhaseListener {

	/** Id used to display the outcome. */
	private String statusPanelId = StatusPanel.STATUS_PANEL_OUTCOME;
	
	public ExecutionFeedbackHandler() {
		super("execution-outcome-feedback-handler");
	}

    public void processExecutionPhase(ExecutionContext event) {
    	switch(event.getPhase()) {
    	case POST_PROCESS:
    		postProcess(event);
    		break;
    	default:
    		// do nothing
    	}
    }
	
	private void postProcess(ExecutionContext context) {
		// we use the outcome data to fetch information about task success
		String outcome = Result.getResultProperty().getValue(context.getActiveExecutable());
		
		// check whether we have anything to display
		if (outcome == null) {
			return;
		}
		
		// check the trial outcome
		// update if we either have success or failure
		if (Result.getResultProperty().isSuccess(context.getActiveExecutable())) {
			updateStatusPanel(context, true);
		}
		else if (Result.getResultProperty().isFailure(context.getActiveExecutable())) {
			updateStatusPanel(context, false);
		}
	}
	
	/**
	 * Updates the status panel with either true or false value. 
	 */
	private void updateStatusPanel(ExecutionContext event, boolean success) {
		// get the status panel if available
		CorrectWrongStatusPanel panel = (CorrectWrongStatusPanel) StatusRegionUtil.getStatusPanel(statusPanelId);
		if (panel != null) {
			panel.setProperty(StatusPanel.PROPERTY_VALUE, success);
		}
	}

	public String getStatusPanelId() {
		return statusPanelId;
	}

	public void setStatusPanelId(String statusPanelId) {
		this.statusPanelId = statusPanelId;
	}
	
	
}
