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

import static ch.tatool.core.display.swing.ExecutionDisplayUtils.getDisplay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.data.Rating;
import ch.tatool.core.display.swing.SwingExecutionDisplay;
import ch.tatool.core.display.swing.action.ActionPanel;
import ch.tatool.core.display.swing.action.ActionPanelListener;
import ch.tatool.core.display.swing.action.SliderActionPanel;
import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;
import ch.tatool.data.DescriptivePropertyHolder;
import ch.tatool.data.Property;

public class ConfidenceRatingExecutable extends BlockingAWTExecutable implements ActionPanelListener, DescriptivePropertyHolder {

	 Logger logger = LoggerFactory.getLogger(ConfidenceRatingExecutable.class);
	 
	/** Key panel used to flip through the instructions. */
	private SliderActionPanel actionPanel;
	
	/** Constructor with element id parameter. */
	public ConfidenceRatingExecutable() {
		super("confidence-rating");
		
		// setup the action panel
		actionPanel = new SliderActionPanel();
		actionPanel.addActionPanelListener(this);
	}
	
	@Override
	protected void startExecutionAWT() {
		actionPanel.setRatingLabel("How confident are you in your decision?");
		
		// setup the ExecutionDisplay
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		regionsContainer.setRegionContent(Region.CENTER, actionPanel);
		
		// display and enable the action panel
		SwingExecutionDisplay display = getDisplay(getExecutionContext());
		ContainerUtils.showRegionsContainer(display);
		
		// enable the action panel
        actionPanel.enableActionPanel();
	}

	protected void doCleanup() {
		// disable actions
		actionPanel.disableActionPanel();
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		regionsContainer.removeRegionContent(Region.CENTER);
	}
	
	@Override
	protected void cancelExecutionAWT() {
		doCleanup();
	}

	public void actionTriggered(ActionPanel source, Object actionValue) {
		if (getFinishExecutionLock()) {
			Rating.getRatingValueProperty().setValue(this, (Integer) actionValue);
			doCleanup();
			finishExecution();
		}
	}
	
	public Property<?>[] getPropertyObjects() {
		return new Property[] {
			Rating.getRatingValueProperty()
		};
	}
}
