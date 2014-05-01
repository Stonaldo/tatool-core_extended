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

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import ch.tatool.core.display.swing.SwingExecutionDisplay;
import ch.tatool.core.display.swing.action.ActionPanel;
import ch.tatool.core.display.swing.action.ActionPanelListener;
import ch.tatool.core.display.swing.action.KeyActionPanel;
import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;
import ch.tatool.core.element.handler.timeout.DefaultTimeoutHandler;
import ch.tatool.core.element.handler.timeout.TimeoutHandler;

/**
 * Executable element that displays generic content in the different regions.
 * 
 * Executable finish can either be triggered through a timeout or through a user input
 * or both.
 * 
 * @author Michael Ruflin
 */
public class GenericContentExecutable extends BlockingAWTExecutable {

	/** Contains the panels to display. */
	private Map<Region, JPanel> contents;

	/** Background color to use for the display. */
	private Color backgroundColor = null;
	
	/** Contains the timeout duration. */
    private long displayDuration = 0;
    
    /** Whether the element should also finish upon user input. */
    private boolean finishOnInput = false;
    
    // runtime variables
    private TimeoutHandler timeoutHandler;
    private Color origColor; // store the previous color
    
	public GenericContentExecutable() {
		contents = new HashMap<Region, JPanel>();
	}
	
	public GenericContentExecutable(String id) {
		this();
		setLocalId(id);
	}
	
	
	@Override
	protected void startExecutionAWT() {
		// initialize the view
		SwingExecutionDisplay display = getDisplay(getExecutionContext());
		
		// set the defined background color
		if (backgroundColor != null) {
			origColor = display.getBackgroundColor();
			display.setBackgroundColor(backgroundColor);
		}
		
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		// add all defined content panels
		for (Region region : contents.keySet()) {
			JPanel panel = contents.get(region);
			regionsContainer.setRegionContent(region, panel, false);
		}
        
		// setup the timer (if applicable)
		if (displayDuration > 0) {
			timeoutHandler = new DefaultTimeoutHandler();
			timeoutHandler.setDefaultTimerDuration(displayDuration);
		}
        
		// check whether the user is allowed to finish by hitting a button
		if (finishOnInput) {
			// action panel
			ActionPanelListener myListener = new ActionPanelListener() {
				public void actionTriggered(ActionPanel source, Object actionValue) {
					if (getFinishExecutionLock()) {
						doCleanup();
						finishExecution();
					}
				}
			};
			KeyActionPanel actionPanel = new KeyActionPanel();
			actionPanel.addKey(KeyEvent.VK_SPACE, "", null);
			actionPanel.addActionPanelListener(myListener);
			actionPanel.enableActionPanel();
		}
		
		// display the information
		regionsContainer.setAllContentVisibility(true);
		
		// start the timer (if applicable)
		if (timeoutHandler != null) {
			timeoutHandler.startTimeout(getExecutionContext());
		}
	}

	private void doCleanup() {
		// cancel / clear timeout handler
		if (timeoutHandler != null) {
			timeoutHandler.cancelTimeout();
			timeoutHandler = null;
		}
		
		// revert color
		if (origColor != null) {
			getDisplay(getExecutionContext()).setBackgroundColor(origColor);
			origColor = null;
		}
		
		// remove added panels
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		for (Region region : contents.keySet()) {
			regionsContainer.removeRegionContent(region);
		}
	}
	
	@Override
	protected void cancelExecutionAWT() {
		doCleanup();
	}
	
	public void addContent(Region region, JPanel panel) {
		contents.put(region, panel);
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public long getDisplayDuration() {
		return displayDuration;
	}

	public void setDisplayDuration(long displayDuration) {
		this.displayDuration = displayDuration;
	}

	public boolean isFinishOnInput() {
		return finishOnInput;
	}

	public void setFinishOnInput(boolean finishOnInput) {
		this.finishOnInput = finishOnInput;
	}
}
