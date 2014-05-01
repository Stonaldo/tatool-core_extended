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
 * Helper Class with getter and setter methods for properties concerning ratings.
 * 
 * @author Andre Locher
 */

public class Rating {

	/** Property name used to set a ratings value. */
    public static final String PROPERTY_RATING_VALUE = "ratingValue";
	
	private static IntegerProperty ratingValueProperty = new IntegerProperty(PROPERTY_RATING_VALUE, 0);

	public static IntegerProperty getRatingValueProperty() {
		return ratingValueProperty;
	}

	public static void setRatingValueProperty(IntegerProperty ratingValueProperty) {
		Rating.ratingValueProperty = ratingValueProperty;
	}
	
}
