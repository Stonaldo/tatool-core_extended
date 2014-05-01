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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Default status region configurator that is suitable for module
 * sessions.
 * 
 * @author Michael Ruflin
 */
public class DefaultModuleStatusRegionConfigurator extends StatusRegionConfigurator {
	
	private List<String> panels;

	public DefaultModuleStatusRegionConfigurator() {
		panels = new ArrayList<String>();
	}
	
	private void setupStatusPanels() {
		Map<String, StatusPanel> statusPanels = new LinkedHashMap<String, StatusPanel>(); // used a linked map to retain order!

		// Add level panel
		if (panels.contains(StatusPanel.STATUS_PANEL_LEVEL)) {
			TextStatusPanel levelPanel = new TextStatusPanel();
			levelPanel.setProperty(StatusPanel.PROPERTY_TITLE, "L E V E L");
			statusPanels.put(StatusPanel.STATUS_PANEL_LEVEL, levelPanel);
		}
		
		// Add trial count
		if (panels.contains(StatusPanel.STATUS_PANEL_TRIAL)) {
			TextStatusPanel trialPanel = new TextStatusPanel();
			trialPanel.setProperty(StatusPanel.PROPERTY_TITLE, "T R I A L");
			statusPanels.put(StatusPanel.STATUS_PANEL_TRIAL, trialPanel);
		}
		
		// Add task feedback
		if (panels.contains(StatusPanel.STATUS_PANEL_OUTCOME)) {
			CorrectWrongStatusPanel feedbackPanel = new CorrectWrongStatusPanel();
			feedbackPanel.setProperty(StatusPanel.PROPERTY_TITLE, "F E E D B A C K");
			statusPanels.put(StatusPanel.STATUS_PANEL_OUTCOME, feedbackPanel);
		}
		
		// Add timer
		if (panels.contains(StatusPanel.STATUS_PANEL_TIMER)) {
			TimerStatusPanel timerPanel = new TimerStatusPanel();
			timerPanel.setProperty(StatusPanel.PROPERTY_TITLE, "T I M E R");
			statusPanels.put(StatusPanel.STATUS_PANEL_TIMER, timerPanel);
		}
		
		// Add block
		if (panels.contains(StatusPanel.STATUS_PANEL_BLOCK)) {
			TextStatusPanel blockPanel = new TextStatusPanel();
			blockPanel.setProperty(StatusPanel.PROPERTY_TITLE, "B L O C K");
			statusPanels.put(StatusPanel.STATUS_PANEL_BLOCK, blockPanel);
		}
		
		// add Custom 
		if (panels.contains(StatusPanel.STATUS_PANEL_CUSTOM_ONE)) {
			TextStatusPanel customPanel1 = new TextStatusPanel();
			customPanel1.setProperty(StatusPanel.PROPERTY_TITLE, "C U S T O M");
			statusPanels.put(StatusPanel.STATUS_PANEL_CUSTOM_ONE, customPanel1);
		}
		
		// add Custom 
		if (panels.contains(StatusPanel.STATUS_PANEL_CUSTOM_TWO)) {
			TextStatusPanel customPanel2 = new TextStatusPanel();
			customPanel2.setProperty(StatusPanel.PROPERTY_TITLE, "C U S T O M");
			statusPanels.put(StatusPanel.STATUS_PANEL_CUSTOM_TWO, customPanel2);
		}
		
		// add Custom 
		if (panels.contains(StatusPanel.STATUS_PANEL_CUSTOM_THREE)) {
			TextStatusPanel customPanel3 = new TextStatusPanel();
			customPanel3.setProperty(StatusPanel.PROPERTY_TITLE, "C U S T O M");
			statusPanels.put(StatusPanel.STATUS_PANEL_CUSTOM_THREE, customPanel3);
		}
		
		// DUAL N BACK FEEDBACK PANELS
		if (panels.contains(StatusPanel.STATUS_PANEL_DNB_FEEDBACK_LOCI)) {
			CorrectWrongStatusPanel dnbLociPanel = new CorrectWrongStatusPanel();
			dnbLociPanel.setProperty(StatusPanel.PROPERTY_TITLE, "G R I D");
			statusPanels.put(StatusPanel.STATUS_PANEL_DNB_FEEDBACK_LOCI, dnbLociPanel);
		}

		if (panels.contains(StatusPanel.STATUS_PANEL_DNB_FEEDBACK_AUDIO)) {
			CorrectWrongStatusPanel dnbAudioPanel = new CorrectWrongStatusPanel();
			dnbAudioPanel.setProperty(StatusPanel.PROPERTY_TITLE, "L E T T E R");
			statusPanels.put(StatusPanel.STATUS_PANEL_DNB_FEEDBACK_AUDIO, dnbAudioPanel);
		}	

		setStatusPanels(statusPanels);
	}
	
	public void setPanels(List<String> panels) {
		this.panels = panels;
		setupStatusPanels();
	}
	
}
