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

import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;

/**
 * Simply sets all points to zero, effectively removing the trial outcome from the
 * score and level handler results.
 * 
 * This adaptor can be used in cases where an exercise is solely used to distract the
 * user while the actual results are not important for the exercise.
 * 
 * @author Michael Ruflin
 */
public class NullablePointAdaptor extends AbstractPointAdaptor implements PointsAndLevelHandler.PointAdaptor {

	private static final int NULL_POINTS_VALUE = 0;
	
	public void adaptPoints(PointsAndLevelHandler handler, ExecutionContext context) {
		for (Trial trial : context.getExecutionData().getTrials()) {
			setAdaptedMinPoints(trial, handler, NULL_POINTS_VALUE);
			setAdaptedPoints(trial, handler, NULL_POINTS_VALUE);
			setAdaptedMaxPoints(trial, handler, NULL_POINTS_VALUE);
		}
	}
}
