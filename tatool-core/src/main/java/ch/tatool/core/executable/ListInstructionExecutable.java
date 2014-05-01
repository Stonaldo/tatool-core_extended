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
package ch.tatool.core.executable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * ListInstructionExecutable
 * 
 * Holds the individual instructions in a list of panels.
 * 
 * @author Andre Locher
 */
public class ListInstructionExecutable extends AbstractInstructionExecutable {
	
	/** Panels used for the instructions. */
	private List<JPanel> panels;

	public ListInstructionExecutable() {
		this("list-instruction");
		panels = new ArrayList<JPanel>();
	}
	
	public ListInstructionExecutable(String id) {
		super(id);
	}

	/** Get the list of panels to display. */
	public List<JPanel> getPanels() {
		return panels;
	}

	/** Set the list of panels to display. */
	public void setPanels(List<JPanel> panels) {
		this.panels = panels;
	}
	
	/**
	 * Get the number of available panels.
	 */
	protected int getInstructionCount() {
		return panels.size();
	}
	
	/**
	 * Get one of the panels.
	 */
	protected JPanel getInstruction(int index) {
		if (0 <= index && index < panels.size()) {
			return panels.get(index);
		} else {
			return null;
		}
	}
}
