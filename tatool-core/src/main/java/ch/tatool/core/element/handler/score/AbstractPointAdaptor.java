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

import ch.tatool.core.data.Misc;
import ch.tatool.core.data.Points;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;

/**
 * Abstract implementation of a point adapter. Ensures all point adapters work with the correct data.
 * 
 * @author Michael Ruflin
 */
public abstract class AbstractPointAdaptor extends NodeImpl implements PointsAndLevelHandler.PointAdaptor {

	/** To be implemented by subclasses. */
	public abstract void adaptPoints(PointsAndLevelHandler handler, ExecutionContext context);
	
	public Integer getMinPointsToAdapt(Trial trial, PointsAndLevelHandler handler) {
		return Points.getMinPointsProperty().getValue(trial, handler);
	}
	
	public Integer getPointsToAdapt(Trial trial, PointsAndLevelHandler handler) {
		return Points.getPointsProperty().getValue(trial, handler);
	}
	
	public Integer getMaxPointsToAdapt(Trial trial, PointsAndLevelHandler handler) {
		return Points.getMaxPointsProperty().getValue(trial, handler);
	}
	
	public void setAdaptedMinPoints(Trial trial, PointsAndLevelHandler handler, Integer value) {
		Points.getMinPointsProperty().setValue(trial, handler, value);
	}
	
	public void setAdaptedPoints(Trial trial, PointsAndLevelHandler handler, Integer value) {
		Points.getPointsProperty().setValue(trial, handler, value);
	}
	
	public void setAdaptedMaxPoints(Trial trial, PointsAndLevelHandler handler, Integer value) {
		Points.getMaxPointsProperty().setValue(trial, handler, value);
	}
	
	public String getDescription() {
		return Misc.getDescriptionProperty().getValue(this);
	}

	public void setDescription(String description) {
		Misc.getDescriptionProperty().setValue(this, description);
	}
}
