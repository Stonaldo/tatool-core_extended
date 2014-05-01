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

import java.util.List;

import ch.tatool.core.display.swing.status.StatusPanel;
import ch.tatool.core.display.swing.status.StatusRegionImpl;
import ch.tatool.core.display.swing.status.StatusRegionUtil;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhase;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * This handler takes care of resetting status panel states (enabled, reset).
 * It is used for example to disable the timer status panel, to display it as
 * grayed out when not used.
 * 
 * @author Michael Ruflin
 */
public class StatusPanelStateHandler extends NodeImpl implements ExecutionPhaseListener {

	private List<String> resetIds;
	private List<String> disableIds;
	private List<String> enableIds;
	private ExecutionPhase phase = ExecutionPhase.PRE_PROCESS;
	
	public void processExecutionPhase(ExecutionContext event) {
		if (! event.getPhase().equals(phase)) {
			return;
		}
		
		// fetch the status region
		StatusRegionImpl statusRegion = StatusRegionUtil.getInstance(); 
		
		// reset
		if (resetIds != null) {
			for (String id : resetIds) {
				StatusPanel panel = statusRegion.getStatusPanel(id);
				if (panel != null) {
					panel.reset();
				}
			}
		}
		
		// disable
		if (disableIds != null) {
			for (String id : disableIds) {
				StatusPanel panel = statusRegion.getStatusPanel(id);
				if (panel != null) {
					panel.setEnabled(false);
				}
			}
		}
		
		// enable
		if (enableIds != null) {
			for (String id : enableIds) {
				StatusPanel panel = statusRegion.getStatusPanel(id);
				if (panel != null) {
					panel.setEnabled(true);
				}
			}
		}
	}

	// Getters and setters
	
	public List<String> getResetIds() {
		return resetIds;
	}

	public void setResetIds(List<String> resetIds) {
		this.resetIds = resetIds;
	}

	public List<String> getDisableIds() {
		return disableIds;
	}

	public void setDisableIds(List<String> disableIds) {
		this.disableIds = disableIds;
	}

	public List<String> getEnableIds() {
		return enableIds;
	}

	public void setEnableIds(List<String> enableIds) {
		this.enableIds = enableIds;
	}

	public ExecutionPhase getPhase() {
		return phase;
	}

	/** Set the phase in which to execute the reset/enable/disable actions.
	 * @param phase the phase in which to execute the actions. Default is PRE_PROCESS
	 */
	public void setPhase(ExecutionPhase phase) {
		this.phase = phase;
	}
	
}
