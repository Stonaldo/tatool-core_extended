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

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;


/**
 * Status region component implementation.
 */
public class StatusRegionImpl extends JPanel implements StatusRegion {

	private static final long serialVersionUID = 1L;
	
	private List<StatusPanel> panels;
	private List<String> ids;
	private Map<String, StatusPanel> idsToPanels;
	private GridLayout gridLayout;
	
	public StatusRegionImpl() {
		panels = new ArrayList<StatusPanel>();
		ids = new ArrayList<String>();
		idsToPanels = new HashMap<String, StatusPanel>();
		gridLayout = new GridLayout();
		gridLayout.setHgap(15);
		setOpaque(false);
		setLayout(gridLayout);
	}
	
	public List<StatusPanel> getStatusPanels() {
		return Collections.unmodifiableList(panels);
	}
	
	public void addStatusPanel(String id, StatusPanel statusPanel) {
		addStatusPanel(id, statusPanel, panels.size());
	}

	public void addStatusPanel(String id, StatusPanel statusPanel, int index) {
		// make sure we remove the current statuspanel with the given id (if any)
		removeStatusPanel(id);
		
		// add the status panel at the given position
		int position = index;
		if (position < 0) {
			position = 0;
		}
		if (position > panels.size()) {
			position = panels.size();
		}
		
		// add the panel now
		idsToPanels.put(id, statusPanel);
		panels.add(position, statusPanel);
		ids.add(position, id);
		this.add(statusPanel.getView(), position);
		
		this.revalidate();
		this.repaint();
	}

	public StatusPanel getStatusPanel(String id) {
		return idsToPanels.get(id);
	}

	public List<String> getStatusPanelIds() {
		// create a new list as the caller might want to reorder the list
		return new ArrayList<String>(ids);
	}


	public void reoderStatusPanels(List<String> statusPanelIds) {
		// create the new ids array, taking statusPanelIds into account
		List<String> newIds = new ArrayList<String>(statusPanelIds);
		newIds.retainAll(ids); // now newIds only contains valid ids
		ids.removeAll(statusPanelIds);
		newIds.addAll(ids); // now we added all ids that were missing in statusPanelIds
		ids = newIds;
		
		// remove all panels and add them again in the requested order
		this.removeAll();
		panels.clear();
		for (String id : ids) {
			StatusPanel statusPanel = idsToPanels.get(id);
			panels.add(statusPanel);
			this.add(statusPanel.getView());
		}
		this.revalidate();
		this.repaint();
	}
	
	public void removeStatusPanel(String id) {
		if (idsToPanels.containsKey(id)) {
			StatusPanel panel = idsToPanels.remove(id);
			panels.remove(panel);
			ids.remove(ids.indexOf(id));
			this.remove(panel.getView());
		}
		this.revalidate();
		this.repaint();
	}
	
	public void removeAllPanels() {
		panels.clear();
		ids.clear();
		idsToPanels.clear();
		removeAll();
		this.revalidate();
		this.repaint();
	}
}
