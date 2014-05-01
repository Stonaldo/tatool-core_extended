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
package ch.tatool.core.data;

/**
 * Helper Class with getter and setter methods for properties concerning timing.
 * 
 * @author Andre Locher
 */

public class Timing {

    public static final String PROPERTY_TIMING_START = "startTime";
    public static final String PROPERTY_TIMING_END = "endTime";
    public static final String PROPERTY_TIMING_DURATION = "durationTime";
	
	private static DateProperty startTimeProperty = new DateProperty(PROPERTY_TIMING_START);
	private static DateProperty endTimeProperty = new DateProperty(PROPERTY_TIMING_END);
	private static LongProperty durationTimeProperty = new LongProperty(PROPERTY_TIMING_DURATION);

	public static LongProperty getDurationTimeProperty() {
		return durationTimeProperty;
	}

	public static void setDurationTimeProperty(LongProperty durationTimeProperty) {
		Timing.durationTimeProperty = durationTimeProperty;
	}

	public static DateProperty getEndTimeProperty() {
		return endTimeProperty;
	}

	public static void setEndTimeProperty(DateProperty endTimeProperty) {
		Timing.endTimeProperty = endTimeProperty;
	}

	public static DateProperty getStartTimeProperty() {
		return startTimeProperty;
	}

	public static void setStartTimeProperty(DateProperty startTimeProperty) {
		Timing.startTimeProperty = startTimeProperty;
	}
	
}
