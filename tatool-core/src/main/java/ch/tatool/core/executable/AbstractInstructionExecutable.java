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

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import ch.tatool.core.display.swing.ExecutionDisplayUtils;
import ch.tatool.core.display.swing.SwingExecutionDisplay;
import ch.tatool.core.display.swing.action.ActionPanel;
import ch.tatool.core.display.swing.action.ActionPanelListener;
import ch.tatool.core.display.swing.action.KeyActionPanel;
import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;

/**
 * Node intended for use cases where information needs to be presented to the user.
 * The actual information to display has to be provided by subclasses, flipping through
 * the information panels happens through user input (space key currently).
 * 
 * @author Andre Locher
 */
public abstract class AbstractInstructionExecutable extends BlockingAWTExecutable
		implements ActionPanelListener
{

	/** Key panel used to flip through the instructions. */
	private KeyActionPanel actionPanel;
	
	/** Currently displayed index. */
	private int currentIndex;
	
	/** Default constructor. */
	public AbstractInstructionExecutable() {
		this("instruction");
	}
	
	/** Constructor with element id parameter. */
	public AbstractInstructionExecutable(String elementName) {
		super(elementName);
		// setup the action panel
		actionPanel = new KeyActionPanel();
		actionPanel.addActionPanelListener(this);
	}
	
	private void setupActionPanelKeys(int index) {
		actionPanel.removeKeys();
		
		if (index > 0) {
			actionPanel.addKey(KeyEvent.VK_LEFT, "Zurück", -1);
			actionPanel.addKey(KeyEvent.VK_RIGHT, "Weiter", 1);	
		} else {
			actionPanel.addKey(KeyEvent.VK_RIGHT, "Weiter", 1);
		}
		actionPanel.validate();
	}

	/** Called at element start. */
	@Override
	protected void startExecutionAWT() {
		// setup the RegionsContainer
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		SwingExecutionDisplay display = ExecutionDisplayUtils.getDisplay(getExecutionContext());
		ContainerUtils.showRegionsContainer(display);
		regionsContainer.setRegionVisibility(Region.NORTH, false);
		
		// only continue if there are instructions to display
		if (getInstructionCount() < 1) {
			getFinishExecutionLock();
			return;
		}
		
		// setup first instruction panel
		setupInstruction(0);
	}

	/** Sets up the next instruction panel. */
	private void setupInstruction(int index) {
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		JPanel instructionPanel = getInstruction(index);
		regionsContainer.removeRegionContent(Region.CENTER);
		regionsContainer.setRegionContent(Region.CENTER, instructionPanel);
		regionsContainer.removeRegionContent(Region.SOUTH);
		setupActionPanelKeys(index);
		regionsContainer.setRegionContent(Region.SOUTH, actionPanel);
		regionsContainer.setRegionContentVisibility(Region.SOUTH, true);
		regionsContainer.setRegionContentVisibility(Region.CENTER, true);
		actionPanel.enableActionPanel();
		currentIndex = index;
	}

	/**
	 * Cleanup after element execution
	 */
	@Override
	protected void cancelExecutionAWT() {
		// disable actions
		actionPanel.disableActionPanel();
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		regionsContainer.removeRegionContent(Region.CENTER);
		regionsContainer.removeRegionContent(Region.SOUTH);
	}

	/** ActionPanelListener interface. */
	public void actionTriggered(ActionPanel source, Object actionValue) {
		int newIndex = 0;
		int addIndex = (Integer) actionValue;
		actionPanel.disableActionPanel();
		// increment the displayed image
		if (currentIndex + addIndex >= 0 ) {
			newIndex = currentIndex + addIndex;
		}

		// setup next instruction / finish executable
		if (newIndex < getInstructionCount()) {
			setupInstruction(newIndex);
			return;
		} else {
			if (getFinishExecutionLock()) {
				cancelExecutionAWT();
				finishExecution();
			}
			return;
		}
	}

	// Abstract methods
	
	/**
	 * Get the number of available instruction panels.
	 */
	protected abstract int getInstructionCount();
	
	/**
	 * Get one of the instruction panels.
	 */
	protected abstract JPanel getInstruction(int index);

}
