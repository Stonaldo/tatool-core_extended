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

import ch.tatool.element.Node;

/**
 * Helper Class with getter and setter methods for properties concerning points.
 * 
 * @author Michael Ruflin
 */
public class Points {

	// individual points
	/** Property name used to set a minPoints value. */
	public static final String PROPERTY_MIN_POINTS = "minPoints";
	
	/** Property name used to set a points value. */
	public static final String PROPERTY_POINTS = "points";
	
	/** Property name used to set a maxPoints value. */
	public static final String PROPERTY_MAX_POINTS = "maxPoints";
	
	// total points (accumulated)
	/** Property name used to set a totalMinPoints value. */
    public static final String PROPERTY_TOTAL_MIN_POINTS = "totalMinPoints";
    
    /** Property name used to set a totalPoints value. */
    public static final String PROPERTY_TOTAL_POINTS = "totalPoints";
    
    /** Property name used to set a totalMaxPoints value. */
    public static final String PROPERTY_TOTAL_MAX_POINTS = "totalMaxPoints";
	
	private static IntegerProperty minPointsProperty = new IntegerProperty(PROPERTY_MIN_POINTS, 0);
	private static IntegerProperty pointsProperty = new IntegerProperty(PROPERTY_POINTS, 0);
	private static IntegerProperty maxPointsProperty = new IntegerProperty(PROPERTY_MAX_POINTS, 0);
	
	private static IntegerProperty totalMinPointsProperty = new IntegerProperty(PROPERTY_TOTAL_MIN_POINTS, 0);
	private static IntegerProperty totalPointsProperty = new IntegerProperty(PROPERTY_TOTAL_POINTS, 0);
	private static IntegerProperty totalMaxPointsProperty = new IntegerProperty(PROPERTY_TOTAL_MAX_POINTS, 0);
	
	private Points() {
		// don't allow instantiation
	}
	
	/**
	 * Gets the minPointsProperty as a IntegerProperty.
	 * 
	 * @return minPointsProperty
	 */
	public static IntegerProperty getMinPointsProperty() {
		return minPointsProperty;
	}

	/**
	 * Gets the pointsProperty as a IntegerProperty.
	 * 
	 * @return pointsProperty
	 */
	public static IntegerProperty getPointsProperty() {
		return pointsProperty;
	}

	/**
	 * Gets the maxPointsProperty as a IntegerProperty.
	 * 
	 * @return maxPointsProperty
	 */
	public static IntegerProperty getMaxPointsProperty() {
		return maxPointsProperty;
	}
	
	/**
	 * Sets the minPointsProperty as an IntegerProperty.
	 * 
	 * @param minPointsProperty the IntegerProperty that will be set
	 */
	public static void setMinPointsProperty(IntegerProperty minPointsProperty) {
		Points.minPointsProperty = minPointsProperty;
	}

	/**
	 * Sets the pointsProperty as an IntegerProperty.
	 * 
	 * @param pointsProperty the IntegerProperty that will be set
	 */
	public static void setPointsProperty(IntegerProperty pointsProperty) {
		Points.pointsProperty = pointsProperty;
	}

	/**
	 * Sets the maxPointsProperty as an IntegerProperty.
	 * 
	 * @param maxPointsProperty the IntegerProperty that will be set
	 */
	public static void setMaxPointsProperty(IntegerProperty maxPointsProperty) {
		Points.maxPointsProperty = maxPointsProperty;
	}
	
	/**
	 * Gets the totalMinPointsProperty as a IntegerProperty.
	 * 
	 * @return totalMinPointsProperty
	 */
	public static IntegerProperty getTotalMinPointsProperty() {
		return totalMinPointsProperty;
	}

	/**
	 * Gets the totalPointsProperty as a IntegerProperty.
	 * 
	 * @return totalPointsProperty
	 */
	public static IntegerProperty getTotalPointsProperty() {
		return totalPointsProperty;
	}

	/**
	 * Gets the totalMaxPointsProperty as a IntegerProperty.
	 * 
	 * @return totalMaxPointsProperty
	 */
	public static IntegerProperty getTotalMaxPointsProperty() {
		return totalMaxPointsProperty;
	}

	/**
	 * Sets the totalMinPointsProperty as an IntegerProperty.
	 * 
	 * @param totalMinPointsProperty the IntegerProperty that will be set
	 */
	public static void setTotalMinPointsProperty(
			IntegerProperty totalMinPointsProperty) {
		Points.totalMinPointsProperty = totalMinPointsProperty;
	}

	/**
	 * Sets the totalPointsProperty as an IntegerProperty.
	 * 
	 * @param totalPointsProperty the IntegerProperty that will be set
	 */
	public static void setTotalPointsProperty(IntegerProperty totalPointsProperty) {
		Points.totalPointsProperty = totalPointsProperty;
	}

	/**
	 * Sets the totalMaxPointsProperty as an IntegerProperty.
	 * 
	 * @param totalMaxPointsProperty the IntegerProperty that will be set
	 */
	public static void setTotalMaxPointsProperty(
			IntegerProperty totalMaxPointsProperty) {
		Points.totalMaxPointsProperty = totalMaxPointsProperty;
	}

	/**
	 * Helper method to set the min and max points of a element at once.
	 * 
	 * @param node
	 * @param minPoints
	 * @param maxPoints
	 */
	public static void setMinMaxPoints(Node node, int minPoints, int maxPoints) {
		getMinPointsProperty().setValue(node, minPoints);
		getMaxPointsProperty().setValue(node, maxPoints);
    }
	
    /**
     * Helper method to set the min and max points of a element at once to (0, 1).
     * 
     * @param node
     */
    public static void setZeroOneMinMaxPoints(Node node) {
    	setMinMaxPoints(node, 0, 1);
    }
  
    /**
     * Helper Method which sets the pointsProperty according to a boolean to 0=false or 1=true.
     * 
     * @param node
     * @param outcome
     */
    public static void setZeroOnePoints(Node node, boolean outcome) {
    	getPointsProperty().setValue(node, outcome ? 1 : 0);
    }
}
