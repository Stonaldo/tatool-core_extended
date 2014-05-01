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
 * Helper Class with getter and setter methods for properties concerning trial outcome.
 * 
 * @author Michael Ruflin
 */
public class Result {
	
	/** Property name used to set a result value. */
	public static final String PROPERTY_RESULT = "result";
	
	private static ResultProperty resultProperty = new ResultProperty();

	/**
	 * Gets the resultProperty as a ResultProperty.
	 * 
	 * @return resultProperty
	 */
	public static ResultProperty getResultProperty() {
		return resultProperty;
	}

	/**
	 * Sets the resultProperty as a ResultProperty.
	 * 
	 * @param resultProperty the ResultProperty that will be set
	 */
	public static void setResultProperty(ResultProperty resultProperty) {
		Result.resultProperty = resultProperty;
	}

	/**
	 * Adds support for setting the result as a boolean to a standard StringProperty object.
	 */
	public static class ResultProperty extends StringProperty {
		
		public static final String SUCCESS_RESULT = "success";
		public static final String FAILURE_RESULT = "failure";
		
		public ResultProperty() {
			super(Result.PROPERTY_RESULT);
		}
		
		/**
		 * Set the result, either success or failure.
		 */
		public void setValue(Node node, boolean success) {
			setValue(node, success ? SUCCESS_RESULT : FAILURE_RESULT);
		}
		
		/**
		 * Find out if the executable result was successful.
		 * 
		 * @param node
		 * @return
		 */
		public boolean isSuccess(Node node) {
			return SUCCESS_RESULT.equals(getValue(node));
		}
		
		/**
		 * Find out if the executable result was a failure.
		 *  
		 * @param node
		 * @return
		 */
		public boolean isFailure(Node node) {
			return FAILURE_RESULT.equals(getValue(node));
		}
	}
}
