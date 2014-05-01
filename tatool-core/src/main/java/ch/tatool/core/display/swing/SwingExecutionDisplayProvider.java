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
package ch.tatool.core.display.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.data.Module;
import ch.tatool.display.ExecutionDisplayProvider;
import ch.tatool.exec.ExecutionPhase;
import ch.tatool.exec.Executor;


/**
 * Full screen window used for task execution.
 * 
 * Tasks should get partial access to the frame to insert own gui elements into the window,
 * the window itself on the other hand should be managed by Tatool exclusively.
 *
 * @author Michael Ruflin
 */
public class SwingExecutionDisplayProvider implements ExecutionDisplayProvider {

    Logger logger = LoggerFactory.getLogger(SwingExecutionDisplayProvider.class);
    
	/**
	 * Module property that defines in what mode the display should be in.
	 * Currently window and fullscreen are the only supported modes, with fullscreen being default.
	 */
	public static final String PROPERTY_DISPLAY_MODE = "module.execution.display.mode";
	public static final String DISPLAY_MODE_FULLSCREEN = "fullscreen";
	public static final String DISPLAY_MODE_WINDOW = "frame";
    
	/** If set to true then the RegionsContainer will be initialized automatically. */
	public static final String PROPERTY_INITIALIZE_REGIONS_CONTAINER = "module.execution.display.regionsContainer";
	
    /** ClosingListener - stops the trainin when the user closes the windows */
    private WindowClosingListener windowClosingListener;
    
    /** ExecutionDisplay implementation. */
    private SwingExecutionFrame executionDisplay;
    
    /** Executor. */
    private Executor executor; 
    
    /** Creates a new SwingExecutionDisplayProvider */
    public SwingExecutionDisplayProvider() {
    	// ExecutionDisplay implementation
    	executionDisplay = new SwingExecutionFrame();

        // add a closing listener to the frame, which cancels the module if the windows is closed
        // Only applies to testing, where the frame is not displayed in full screen mode
        windowClosingListener = new WindowClosingListener();
        executionDisplay.addWindowListener(windowClosingListener);

        // add focus to the root pane for key listeners
        executionDisplay.getRootPane().setFocusable(true);
    }

    /** Get the ExecutionDisplay provided by this provider. */
	public SwingExecutionDisplay getExecutionDisplay() {
    	return executionDisplay;
    }
    
	/** Initializes the display. */
	public void setup(Executor executor, Module module) {
    	this.executor = executor;
    	executionDisplay.setExecutor(this.executor);
    	this.executor.getPhaseListenerManager().addExecutionPhaseListener(executionDisplay, ExecutionPhase.POST_ELEMENT_SELECTION);
    	
    	String mode = module.getModuleProperties().get(PROPERTY_DISPLAY_MODE);
    	if (DISPLAY_MODE_WINDOW.equals(mode)) {
    		executionDisplay.setFullScreenModeEnabled(false);
    	}
    	
    	String regContainer = module.getModuleProperties().get(PROPERTY_INITIALIZE_REGIONS_CONTAINER);
    	if (Boolean.parseBoolean(regContainer)) {
    		ContainerUtils.initialize(executionDisplay);
    	}
    }
	
	/** Opens the display. */
    public void open() {
    	executionDisplay.showWindow();
    }
    
    /** Close the display. */
    public void destroy() {
    	executionDisplay.disposeWindow();
    }
    
    /** Stops the module execution. */
    public void stopModuleExecution() {
    	if (executor != null) {
    		executor.stopExecution();
    	}
    }
    
    /** Triggers module canceling upon window closing. */
    class WindowClosingListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			stopModuleExecution();
		}
	}
}
