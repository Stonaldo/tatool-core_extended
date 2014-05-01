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
 * Helper Class with getter and setter methods for properties concerning levels.
 * 
 * @author Michael Ruflin
 */
public class Level {

	/** Property name used to set the level value. */
    public static final String PROPERTY_LEVEL = "level";
    
    /** Property name used to store the delta between the current and the future level. */
    public static final String PROPERTY_LEVEL_CHANGE_DELTA = "levelChangeDelta";
    
    private static IntegerProperty levelProperty = new IntegerProperty(PROPERTY_LEVEL, 1);
    private static IntegerProperty levelChangeDeltaProperty = new IntegerProperty(PROPERTY_LEVEL_CHANGE_DELTA, 0);
    
	/**
	 * Gets the levelProperty as an IntegerProperty.
	 * 
	 * @return levelProperty
	 */
	public static IntegerProperty getLevelProperty() {
		return levelProperty;
	}

	/**
	 * Sets the levelPropery as an IntegerProperty.
	 * 
	 * @param levelProperty the IntegerProperty that will be set
	 */
	public static void setLevelProperty(IntegerProperty levelProperty) {
		Level.levelProperty = levelProperty;
	}

	/**
	 * Gets the levelChangeDeltaProperty as an IntegerProperty.
	 * 
	 * @return levelChangeDeltaProperty
	 */
	public static IntegerProperty getLevelChangeDeltaProperty() {
		return levelChangeDeltaProperty;
	}

	/**
	 * Sets the levelChangeDeltaProperty as an IntegerProperty.
	 * 
	 * @param levelChangeDeltaProperty the IntegerProperty that will be set
	 */
	public static void setLevelChangeDeltaProperty(IntegerProperty levelChangeDeltaProperty) {
		Level.levelChangeDeltaProperty = levelChangeDeltaProperty;
	}
	
}
