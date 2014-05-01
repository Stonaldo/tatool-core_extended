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
 * Allows adapting the points by a factor.
 * 
 * @author Michael Ruflin
 */
public class FactorPointAdaptor extends AbstractPointAdaptor {

	/** Property that stores the factor. */
	public static final String PROPERTY_FACTOR = "factor";
	
	/** Default constructor. Initializes the adaptor with factor 1 */
	public FactorPointAdaptor() {
		// set factor 1 as default.
		this(1);
	}
	
	/** Constructor initializing the adaptor with factor x. */
	public FactorPointAdaptor(int factor) {
		setFactor(factor);
	} 

	public void setFactor(int factor) {
		setProperty(PROPERTY_FACTOR, factor);
	}
	
	public int getFactor() {
		return (Integer) getProperty(PROPERTY_FACTOR);
	}
	
	/** Adapts the points according to a factor. */
	public void adaptPoints(PointsAndLevelHandler handler, ExecutionContext context) {
		int factor = getFactor();
		for (Trial trial : context.getExecutionData().getTrials()) {
			Integer minPoints = getMinPointsToAdapt(trial, handler);
			if (minPoints != null) {
				setAdaptedMinPoints(trial, handler, minPoints * factor);
			}
			Integer points = getPointsToAdapt(trial, handler);
			if (points != null) {
				setAdaptedPoints(trial, handler, points * factor);
			}
			Integer maxPoints = getMaxPointsToAdapt(trial, handler);
			if (maxPoints != null) {
				setAdaptedMaxPoints(trial, handler, maxPoints * factor);
			}
		}
	}
}
