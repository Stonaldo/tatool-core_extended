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

import java.util.List;


/**
 * Backing object for the status region.
 * Instead of providing direct access to the region, status panels can be added and removed to
 * this object, and accessed by tasks using simple ids. This decouples the status regions and panels
 * from the tasks, yet provides a standard way of getting direct access to the status elements
 * displayed in it. 
 * 
 * @author Michael Ruflin
 */
public interface StatusRegion {
	
	/** Get the status panels in the order in which they appear on the screen. */
	public List<StatusPanel> getStatusPanels();
	
	/** Get the id's of all added status panels.
	 * The list also contains the order of the panels on the screen 
	 */
    public List<String> getStatusPanelIds();
    
    /** Reorder the status panels according to the provided list.
     * 
     * @param statusPanelIds the ids of the statuspanels in the order they should appear on the screen.
     *                       Missing ids will be added to the end, unknown ids ignored. 
     */
    public void reoderStatusPanels(List<String> statusPanelIds);
    
    /** Get a status panel given it's id. */
    public StatusPanel getStatusPanel(String id);

    /** Add a status panel to the status region.
     * 
     *  @param id the id of the status panel.
     *  @param statusPanel the status panel to add
     */
    public void addStatusPanel(String id, StatusPanel statusPanel);
    
    /** Add a status panel to the status region.
     * 
     *  @param id the id of the status panel.
     *  @param statusPanel the status panel to add
     *  @param index the index to insert the panel into
     */
    public void addStatusPanel(String id, StatusPanel statusPanel, int index);
    
    /** Remove a status panel given it's id. */
	public void removeStatusPanel(String id);
    
	/** Remove all status panels. */
    public void removeAllPanels();
}
