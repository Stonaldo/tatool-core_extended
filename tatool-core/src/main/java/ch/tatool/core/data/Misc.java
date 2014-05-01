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
 * Helper Class with getter and setter methods for general miscellaneous properties.
 * 
 * @author Michael Ruflin
 */
public class Misc {

	/** Property name used to set a description value. */
	public static final String PROPERTY_DESCRIPTION = "description";
	
	public static final String PROPERTY_OUTCOME = "outcome";
	
	private static StringProperty descriptionProperty = new StringProperty(PROPERTY_DESCRIPTION);
	
	private static StringProperty outcomeProperty = new StringProperty(PROPERTY_OUTCOME);
	
	public static StringProperty getOutcomeProperty() {
		return outcomeProperty;
	}

	public static void setOutcomeProperty(StringProperty outcomeProperty) {
		Misc.outcomeProperty = outcomeProperty;
	}

	/*public static String getLastOutcome(ExecutionContext executionContext) {
		List<Trial> trials = executionContext.getExecutionData().getTrials();
		if (trials.isEmpty()) return null;
		return getOutcomeProperty().getValue(trials.get(trials.size()-1);
	}*/
	
	/**
	 * Gets the descriptionProperty as a StringProperty.
	 * 
	 * @return descriptionProperty
	 */
	public static StringProperty getDescriptionProperty() {
		return descriptionProperty;
	}

	/**
	 * Sets the descriptionProperty as a StringProperty
	 * 
	 * @param descriptionProperty the StringProperty that will be set
	 */
	public static void setDescriptionProperty(StringProperty descriptionProperty) {
		Misc.descriptionProperty = descriptionProperty;
	}
}
