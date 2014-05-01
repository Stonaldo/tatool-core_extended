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
package ch.tatool.core.display.swing.action;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;


/**
 * Abstract action panel implementation.
 * 
 * Subclass this class for specific action panel implementations.
 * 
 * @author Michael Ruflin
 */
public class AbstractActionPanel extends JPanel implements ActionPanel {

	private static final long serialVersionUID = 1971211497781330743L;
	
	private List<ActionPanelListener> actionPanelListeners;
	
	public AbstractActionPanel() {
		actionPanelListeners = new LinkedList<ActionPanelListener>();
		
		// make it see through by default
		setOpaque(false);
	}
	
	/**
	 * Call this method to enable the action panel (e.g. enable key listeners.
	 */
	public void enableActionPanel() {
	}
	
	/**
	 * Call this method to disable the action panel
	 */
	public void disableActionPanel() {
	}
	
	public void addActionPanelListener(ActionPanelListener listener) {
		if (listener != null) {
			actionPanelListeners.add(listener);
		}
	}
	
	public void removeActionPanelListener(ActionPanelListener listener) {
		if (listener != null) {
			actionPanelListeners.remove(listener);
		}
	}
	
	public void fireActionTriggered(Object actionValue) {
		for (ActionPanelListener listener : actionPanelListeners) {
			listener.actionTriggered(this, actionValue);
		}
	}
}
