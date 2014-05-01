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

import ch.tatool.data.DataContainer;
import ch.tatool.element.Node;

/**
 * Integer value property object
 * 
 * @author Michael Ruflin
 */
public class IntegerProperty extends GenericProperty<Integer> {

	/** Default value to return in getValueOrDefault. */
	private Integer defaultValue = null;
	
	public IntegerProperty(String propertyName) {
		this(propertyName, null);
	}
	
	public IntegerProperty(String propertyName, Integer defaultValue) {
		super(propertyName, Integer.class);
		this.defaultValue = defaultValue;
	}
	
	public Integer ensureValue(DataContainer dataContainer, String nodeId) {
		if (defaultValue != null) {
			return ensureValue(dataContainer, nodeId, defaultValue);
		} else {
			throw new UnsupportedOperationException("A default value needs to be defined prior to be able to use this method");
		}
	}
	
	/**
	 * Returns the stored value of the defaultValue stored in this class.
	 */
	public Integer getValueOrDefault(Node node) {
		return getValue(node, defaultValue);
	}
}
