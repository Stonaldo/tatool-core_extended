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

import javax.swing.JPanel;

public interface StatusPanel {
	
	// default status panels
	public static final String STATUS_PANEL_LEVEL = "level";
	public static final String STATUS_PANEL_OUTCOME = "outcomeFeedback";
	public static final String STATUS_PANEL_TIMER = "timer";
	public static final String STATUS_PANEL_TRIAL = "currentTrial";
	public static final String STATUS_PANEL_BLOCK = "block";
	public static final String STATUS_PANEL_CUSTOM_ONE = "custom1";
	public static final String STATUS_PANEL_CUSTOM_TWO = "custom2";
	public static final String STATUS_PANEL_CUSTOM_THREE = "custom3";	
	public static final String STATUS_PANEL_DNB_FEEDBACK_LOCI = "dnbLoci";
	public static final String STATUS_PANEL_DNB_FEEDBACK_AUDIO = "dnbAudio";
	
	public static final String PROPERTY_TITLE = "title";
	public static final String PROPERTY_VALUE = "value";
	
	/** Enable/disable this info panel. In case of disabled, the panel should go to a grayed out default state. */
	public void setEnabled(boolean enabled);
	
	/** Reset the panel to its default state. */
	public void reset();
	
	/** Set a property of this info panel. */
	public void setProperty(String key, Object value);
	
	/** Get a property set on this info panel. */
	public Object getProperty(String key);
	
	/** Get the view for this info panel. */
	public JPanel getView();
}
