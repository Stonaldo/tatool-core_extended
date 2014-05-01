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
package ch.tatool.core.element.handler.score;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.data.IntegerProperty;
import ch.tatool.core.data.Level;
import ch.tatool.core.data.Misc;
import ch.tatool.core.data.Points;
import ch.tatool.core.display.swing.status.StatusPanel;
import ch.tatool.core.display.swing.status.StatusRegionUtil;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.data.Module;
import ch.tatool.data.ModuleSession;
import ch.tatool.data.Trial;
import ch.tatool.element.Element;
import ch.tatool.element.Node;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Abstract store and level handler. Implements the PointAdapter logic and provides
 * utility methods for subclasses. 
 * 
 * @author Michael Ruflin
 */
public abstract class AbstractPointsAndLevelHandler extends NodeImpl implements ExecutionPhaseListener, PointsAndLevelHandler {
    
    Logger logger = LoggerFactory.getLogger(AbstractPointsAndLevelHandler.class);
    
    protected static final int ZERO_POINTS = 0;
    
    public int startLevel = 1;
	public int startMinPoints = 0;
	public int startMaxPoints = 0;
	public int startPoints = 0;
    
    public AbstractPointsAndLevelHandler() {
    	super();
    }
    
    public AbstractPointsAndLevelHandler(String id) {
    	super(id);
    }

	public String getDescription() {
		return Misc.getDescriptionProperty().getValue(this);
	}

	public void setDescription(String description) {
		Misc.getDescriptionProperty().setValue(this, description);
	}
    
    /**
     * Called by the Executor to signal a new phase in the execution 
     * @param context
     */
    public void processExecutionPhase(ExecutionContext context) {
    	switch (context.getPhase()) {
    	case SESSION_START:
    		sessionStart(context);
    		initializeHandler(context);
    		break;
    	case PRE_PROCESS:
    		preProcess(context);
    		break;
    	case POST_PROCESS:
    		postProcess(context);
    		break;
    	case SESSION_FINISH:
    		sessionFinish(context);
    		break;
    	default:
    		// do nothing
    	}
    }

	private void sessionStart(ExecutionContext context) {
    	
		// init handler values before first session
    	Module module = context.getExecutionData().getModule();

    	// read the last level state from the module
    	Integer level = Level.getLevelProperty().ensureValue(module, this);
    	Level.getLevelProperty().setValue(this, level);
    	
    	// initialize the total points of the session to zero
    	ModuleSession session = context.getExecutionData().getModuleSession();
    	Points.getTotalMinPointsProperty().setValue(session, this, 0);
    	Points.getTotalPointsProperty().setValue(session, this, 0);
    	Points.getTotalMaxPointsProperty().setValue(session, this, 0);
    	
    	// do the same for the module
    	Points.getTotalMinPointsProperty().ensureValue(module, this);
    	Points.getTotalPointsProperty().ensureValue(module, this);
    	Points.getTotalMaxPointsProperty().ensureValue(module, this);
    	
    	// register the handler if not yet done so.
    	PointsAndLevelUtils.registerPointAndLevelHandler(module, this);
    }
    
    private void preProcess(ExecutionContext context) {
    	// update the status region
    	updateStatusRegion(context);
    	
        // set the actual level to the current execution element
    	Level.getLevelProperty().setValue(
    			context.getActiveExecutable(), Level.getLevelProperty().getValueOrDefault(this)
    	);
    }
    
    /** Updates the level status panel. */
    protected void updateStatusRegion(ExecutionContext context) {
    	// update the level status panel if one such exists
    	StatusPanel levelPanel = StatusRegionUtil.getStatusPanel(StatusPanel.STATUS_PANEL_LEVEL);
    	if (levelPanel != null) {
    		levelPanel.setProperty(StatusPanel.PROPERTY_VALUE, Level.getLevelProperty().getValueOrDefault(this));
    	}
    }
    
    private void postProcess(ExecutionContext context) {
        // Process the trial points. This will give PointAdaptors the chance to change points set on a trial.
        processPoints(context);
        
        // update the totals
        updateTotals(context);

        // then process the level
        processLevel(context);
    }
    
    /**
     * Processes the points set by the executable element.
     * 
     * This default implementation first mirrors the points as set by the executable element,
     * followed by letting all PointAdaptors set on the executable element as aspects adapt
     * the points as they wish.
     */
    protected void processPoints(ExecutionContext context) {
    	Node u = context.getActiveExecutable();

    	// first copy the points from the executable element to the score and level handler
    	// check whether any of the properties has been set.
    	boolean avail = false;
    	for (Trial trial : context.getExecutionData().getTrials()) {
    		avail = avail | Points.getMinPointsProperty().copyValue(trial, u, this, ZERO_POINTS);
    		avail = avail | Points.getPointsProperty().copyValue(trial, u, this, ZERO_POINTS);
    		avail = avail | Points.getMaxPointsProperty().copyValue(trial, u, this, ZERO_POINTS);
    	}
    	
    	// return if no properties are available
    	if (! avail) {
    		return;
    	}
    	
    	// Now give all adaptors set on the executable element the possibility to adapt the points
    	// The adaptors directory change the values in the trials.
    	Element activeElement = context.getActiveElement();
    	for (Object handler : activeElement.getHandlers()) {
    		if (handler instanceof PointsAndLevelHandler.PointAdaptor) {
            	((PointsAndLevelHandler.PointAdaptor) handler).adaptPoints(this, context);
        	}
        }
    }
    
    protected void updateTotals(ExecutionContext context) {
    	// calculate the current totals
    	int min=0, curr=0, max=0;
    	for (Trial trial : context.getExecutionData().getTrials()) {
    		min += Points.getMinPointsProperty().getValue(trial, this, ZERO_POINTS);
    		curr += Points.getPointsProperty().getValue(trial, this, ZERO_POINTS);
    		max += Points.getMaxPointsProperty().getValue(trial, this, ZERO_POINTS);
    	}
    	
    	// update the session data with the current execution totals
    	ModuleSession session = context.getExecutionData().getModuleSession();
    	IntegerProperty totalMinPointsProperty = Points.getTotalMinPointsProperty();
    	IntegerProperty totalPointsProperty = Points.getTotalPointsProperty();
    	IntegerProperty totalMaxPointsProperty = Points.getTotalMaxPointsProperty();
    	
    	// fetch the session total - we are sure it is initialized as we used ensureValue in session start
    	int minSession = totalMinPointsProperty.getValue(session, this);
		int currSession = totalPointsProperty.getValue(session, this);
		int maxSession = totalMaxPointsProperty.getValue(session, this);
    	minSession += min;
    	currSession += curr;
    	maxSession += max;
    	totalMinPointsProperty.setValue(session, this, minSession);
    	totalPointsProperty.setValue(session, this, currSession);
    	totalMaxPointsProperty.setValue(session, this, maxSession);
    }
    
    /**
     * Checks for level changes.
     *
     * The actual level check is done by checkLevelChange. This method merely takes care of informing
     * the LevelChangeListeners in case a level change happened.
     * 
     */
    protected void processLevel(ExecutionContext context) {
        // check for level changes
    	System.out.println("APALH Level.getLevelProperty().getValue(this): " + Level.getLevelProperty().getValue(this));
        int currentLevel = Level.getLevelProperty().getValue(this);
        int newLevel = currentLevel;
        
        // check whether we got a result property in the first trial, otherwise return
        List<Trial> trials = context.getExecutionData().getTrials(); 

        // skip the check if no trials are available
        if (trials.isEmpty()) return;

        // check for a level change
        newLevel = checkLevelChange(context, currentLevel);
  
        // inform listeners if level changed
        if (currentLevel != newLevel) {
        	// store the level change in the last trial
        	int levelChangeDelta = newLevel - currentLevel;
        	Level.getLevelChangeDeltaProperty().setValue(this, levelChangeDelta);
        	Level.getLevelChangeDeltaProperty().setValue(context.getExecutionData().getCreateLastTrial(), this);
        	
        	// inform the listeners about the change
        	informLevelChangeListeners(context);
        	
        	// cleanup, remove newLevel, set level as new level
        	Level.getLevelProperty().setValue(this, newLevel);
        	Level.getLevelChangeDeltaProperty().setValue(this, null);
        	
        	 // update the level value in the module
        	Level.getLevelProperty().setValue(context.getExecutionData().getModule(), this);
        } 
    }
    
    /** Informs all listeners in the execution stack about a level change. */
    protected void informLevelChangeListeners(ExecutionContext context) {
    	// inform the complete stack about the level change
    	List<Element> elementStack = context.getElementStack(); 
        for (int x = 0; x < elementStack.size(); x++) {
        	for (Object o : elementStack.get(x).getHandlers()) {
                if (o instanceof PointsAndLevelHandler.LevelListener) {
                    ((PointsAndLevelHandler.LevelListener) o).levelChanged(this, context);
                }
            }
        }
    }

    private void sessionFinish(ExecutionContext context) {
    	// shortcut to property objects
    	IntegerProperty totalMinPointsProperty = Points.getTotalMinPointsProperty();
    	IntegerProperty totalPointsProperty = Points.getTotalPointsProperty();
    	IntegerProperty totalMaxPointsProperty = Points.getTotalMaxPointsProperty();
    	
    	// Set the current level onto the session
    	ModuleSession session = context.getExecutionData().getModuleSession();
    	Level.getLevelProperty().setValue(session, this);
    	
    	// roll up the  the total points to zero
    	Module module = context.getExecutionData().getModule();
    	
    	// load current module totals
    	int minModule = totalMinPointsProperty.getValue(module, this, ZERO_POINTS);
		int currModule = totalPointsProperty.getValue(module, this, ZERO_POINTS);
		int maxModule = totalMaxPointsProperty.getValue(module, this, ZERO_POINTS);
    	
    	// add values from current session
    	minModule += totalMinPointsProperty.getValue(session, this);
    	currModule += totalPointsProperty.getValue(session, this);
		maxModule += totalMaxPointsProperty.getValue(session, this);
		
		// store it back into the module
		totalMinPointsProperty.setValue(module, this, minModule);
    	totalPointsProperty.setValue(module, this, currModule);
    	totalMaxPointsProperty.setValue(module, this, maxModule);
    }
    
    /**
     * Overwrite to implement a different level/points logic.
     * Default logic simply uses += level = > +- 100.
     * 
     * @return the new level. This can be the old level value
     */ 
    protected abstract int checkLevelChange(ExecutionContext context, int currentLevel);
    
    protected abstract void initializeHandler(ExecutionContext context);

}
