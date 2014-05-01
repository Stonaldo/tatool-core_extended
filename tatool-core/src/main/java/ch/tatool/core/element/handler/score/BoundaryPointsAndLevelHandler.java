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


import ch.tatool.core.data.IntegerProperty;
import ch.tatool.data.Module;
import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;


/**
 * PointsAndLevelHandler that performs a level change once the number of points pass a boundry value.
 * 
 * @author Michael Ruflin
 */
public class BoundaryPointsAndLevelHandler extends AbstractPointsAndLevelHandler {

	public static final String PROPERTY_CURRENT_LEVEL_TOTAL_POINTS = "currentLevelTotal";
	
	private static IntegerProperty levelTotalProperty = new IntegerProperty(PROPERTY_CURRENT_LEVEL_TOTAL_POINTS);
	
	public static final String PROPERTY_BOUNDARY_VALUE = "boundaryValue";
	
	private static IntegerProperty boundaryProperty = new IntegerProperty(PROPERTY_BOUNDARY_VALUE);
	private static final int DEFAULT_BOUNDARY_VALUE = 100;

    public BoundaryPointsAndLevelHandler() {
    	super("boundary-score-and-level-handler");
    	levelTotalProperty.setValue(this, DEFAULT_BOUNDARY_VALUE);
    }
    
    public void setBoundaryValue(int value) {
    	boundaryProperty.setValue(this, value);
    }
    
    /**
     * Overridden to also adapt the current level total
     */
    @Override
    protected void updateTotals(ExecutionContext context) {
    	super.updateTotals(context);
    	
    	// also update the current levels total in the module
    	Module module = context.getExecutionData().getModule();
    	int currentLevelTotal = levelTotalProperty.getValue(module, this, 0);
    	for (Trial trial : context.getExecutionData().getTrials()) {
    		currentLevelTotal += levelTotalProperty.getValue(trial, this, 0);
    	}
    	levelTotalProperty.setValue(module, this, currentLevelTotal);
    }
    
    /**
     * Compares the current level total value to the boundary value, and adapts the level accordingly.
     */
    @Override
    protected int checkLevelChange(ExecutionContext context, int currentLevel) {
    	// get the current total and the boundary value
    	Module module = context.getExecutionData().getModule();
    	int currentTotal = levelTotalProperty.getValue(module, this, 0);
    	int newTotal = currentTotal;
    	int boundryValue = boundaryProperty.getValue(this, DEFAULT_BOUNDARY_VALUE);
    	
    	// check for boundary and adapt level and points
    	int newLevel = currentLevel;
        while (Math.abs(currentTotal) >= boundryValue) {
            int direction = currentTotal > 0 ? 1 : -1;
            newTotal -= direction * boundryValue;
            newLevel += direction;
        }
        
        // update the module if something changed
        if (currentTotal != newTotal) {
        	levelTotalProperty.setValue(module, this, newTotal);
        }
    	
        return newLevel;
    }
	
	@Override
	protected void initializeHandler(ExecutionContext context) {
		// TODO Auto-generated method stub
		
	}
}
