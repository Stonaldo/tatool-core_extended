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
package ch.tatool.core.display.swing.status;

import java.util.Map;

import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;
import ch.tatool.core.executable.NonBlockingAWTExecutable;
import ch.tatool.exec.ExecutionOutcome;

public class StatusRegionConfigurator extends NonBlockingAWTExecutable {

	private Map<String, StatusPanel> statusPanels;
	
	public StatusRegionConfigurator() {
		super("status-panel-configurer");
	}
	
	/** 
	 * Set a map of info panels to add to the statusregion.
	 * Use a LinkedHashMap to make sure the order of the statuspanel as inserted into the map is retained
	 * @param statusPanels
	 */
	public void setStatusPanels(Map<String, StatusPanel> statusPanels) {
		this.statusPanels = statusPanels;
	}

	@Override
	protected String executeAWT() {
		StatusRegionImpl statusRegion = StatusRegionUtil.getInstance();
		
		statusRegion.removeAllPanels();
		ContainerUtils.getRegionsContainer().removeRegionContent(Region.NORTH);
		
		if (statusPanels.size() > 0) {
			ContainerUtils.getRegionsContainer().setPreferredRegionHeight(Region.NORTH, 90);
		}
		
		ContainerUtils.getRegionsContainer().setRegionContent(Region.NORTH, statusRegion);
		ContainerUtils.getRegionsContainer().setRegionVisibility(Region.NORTH, true);
		ContainerUtils.getRegionsContainer().setRegionContentVisibility(Region.NORTH, true);
		
		// set new panels if available
		if (statusPanels != null && ! statusPanels.isEmpty()) {
			for (String key : statusPanels.keySet()) {
				StatusPanel statusPanel = statusPanels.get(key);
				statusRegion.addStatusPanel(key, statusPanel);
			}
		}
		
		return ExecutionOutcome.SKIP;
	}
}
