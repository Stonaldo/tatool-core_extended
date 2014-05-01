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

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.data.DataContainer;
import ch.tatool.data.Property;
import ch.tatool.data.PropertyHolder;
import ch.tatool.element.Node;

/**
 * Generic property data object.
 * 
 * You can use this class to define your own property objects without having to write too much.
 * 
 * @author Michael Ruflin
 *
 * @param <T> the type of the property value
 */
public abstract class GenericProperty<T> implements Property<T> {

	private static Logger logger = LoggerFactory.getLogger(ObjectProperty.class);
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	
	private String propertyName;
	private Class<T> propertyType;

	/** Creates a new property object. */
	public GenericProperty(String propertyName, Class<T> propertyType) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;

	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Class<?> getPropertyType() {
		return propertyType;
	}
	
	// Checking whether set
	
	public boolean isSet(PropertyHolder holder) {
		return holder.getProperty(propertyName) != null;
	}
	
	public boolean isSet(DataContainer dataContainer, PropertyHolder propertyHolder) {
		return isSet(dataContainer, propertyHolder.getId());
	}
	
	public boolean isSet(DataContainer dataContainer, String nodeId) {
		return dataContainer.getValue(nodeId, propertyName) != null;
	}
	
	// Getting data
	
	public T getValue(PropertyHolder holder) {
		return getValue(holder, null);
	}
	
	@SuppressWarnings("unchecked")
	public T getValue(PropertyHolder holder, T defaultValue) {
		Object value = holder.getProperty(propertyName);
		if (value != null) {
			if (propertyType.isInstance(value)) {
				return (T) value;
			} else {
				logger.warn("Invalid property value for property " + propertyName + ". Type " + propertyType.getName() +
						" expected, but passed value of type " + value.getClass());
			}
		}
		return defaultValue;
	}
	
	public String getStringValue(DataContainer dataContainer, PropertyHolder holder) {
		return getStringValue(dataContainer, holder.getId(), null);
	}
	
	public String getStringValue(DataContainer dataContainer, String nodeId) {
		return getStringValue(dataContainer, nodeId, null);
	}
	
	public String getStringValue(DataContainer dataContainer, PropertyHolder holder, String defaultValue) {
		return getStringValue(dataContainer, holder.getId(), defaultValue);
	}
	
	public String getStringValue(DataContainer dataContainer, String nodeId, String defaultValue) {
		String value = dataContainer.getValue(nodeId, propertyName);
		return (value != null) ? value : defaultValue;
	}
	
	// Setting data
	
	public void setValue(PropertyHolder holder, T value) {
		holder.setProperty(propertyName, value);
	}
	
	public void setValue(DataContainer dataContainer, PropertyHolder holder) {
    	T value = getValue(holder);
    	setValue(dataContainer, holder, value);
    }
	
	public void setValue(DataContainer dataContainer, PropertyHolder holder, T value) {
		setValue(dataContainer, holder.getId(), value);
	}
	
	public void setValue(DataContainer dataContainer, String nodeId, T value) {
		if (value != null) {
			if (value instanceof java.util.Date) {
				setStringValue(dataContainer, nodeId, dateFormat.format(value));
			} else {
				setStringValue(dataContainer, nodeId, value.toString());
			}
		} else {
			setStringValue(dataContainer, nodeId, (String) null);
		}
	}
	
	public void setStringValue(DataContainer dataContainer, PropertyHolder holder, String value) {
		setStringValue(dataContainer, holder.getId(), value);
	}
	
	public void setStringValue(DataContainer dataContainer, String nodeId, String value) {
		dataContainer.putValue(nodeId, propertyName, value);
	}
	
	// Clearing data
	
	public void clearValue(PropertyHolder holder) {
		holder.setProperty(propertyName, null);
	}
	
	public void clearValue(DataContainer dataContainer, PropertyHolder holder) {
		clearValue(dataContainer, holder.getId());
	}
	
	public void clearValue(DataContainer dataContainer, String nodeId) {
		dataContainer.putValue(nodeId, propertyName, null);
	}
	
	// restoring

	/** Default implementation throws an Exception. */
	public T getValue(DataContainer dataContainer, PropertyHolder holder) {
		return getValue(dataContainer, holder.getId(), null);
	}
	
	/** Default implementation throws an Exception. */
	public T getValue(DataContainer dataContainer, String nodeId) {
		return getValue(dataContainer, nodeId, null);
	}
	
	public T getValue(DataContainer dataContainer, PropertyHolder holder, T defaultValue) {
		return getValue(dataContainer, holder.getId(), defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public T getValue(DataContainer dataContainer, String nodeId, T defaultValue) {
		// check whether we have a converter
		if (! StringConverter.containsConverter(propertyType)) {
			throw new UnsupportedOperationException("No converter for class " + propertyType.getName() + " available.");
		}
		String value = getStringValue(dataContainer, nodeId);
		if (value != null) {
			return (T) StringConverter.convert(value, propertyType);
		} else {
			return defaultValue;
		}
	}
	
	public void restoreValue(DataContainer dataContainer, PropertyHolder holder) {
		restoreValue(dataContainer, holder, null);
	}

	/** Restore a value from the dataContainer to the element. */
	public void restoreValue(DataContainer dataContainer, PropertyHolder holder, T defaultValue) {
		T value = getValue(dataContainer, holder, defaultValue);
		setValue(holder, value);
	}


	/** Ensure a value, using whatever default should be used for this property. */
	public T ensureValue(DataContainer dataContainer, PropertyHolder holder) {
		return ensureValue(dataContainer, holder.getId());
	}
	/** Ensures a value, using the default value defined by this property.
	 * Note: by default this method throws an UnsupportedOperationException
	 * @throws UnsupportedOperationException
	 */
	public T ensureValue(DataContainer dataContainer, String nodeId) {
		throw new UnsupportedOperationException();
	}
	
	public T ensureValue(DataContainer dataContainer, PropertyHolder holder, T defaultValue) {
		return ensureValue(dataContainer, holder.getId(), defaultValue);
	}
	
	/** Ensure a value is set. Sets the default value if not already available.
	 * @return the value currently set, will be the passed in defaultValue if it wasn't set previously 
	 */
	public T ensureValue(DataContainer dataContainer, String nodeId, T defaultValue) {
		T value = getValue(dataContainer, nodeId);
		if (value == null) {
			setValue(dataContainer, nodeId, defaultValue);
			return defaultValue;
		} else {
			return value;
		}
	}
	
	/** Copy a property from one to another nodeId.
	 * @param defaultValue the value to use if fromNode does not contain a value for given property.
	 *                     If defaultValue is null and fromNode does not contain a value then nothing
	 *                     will be copied. 
	 */
	public boolean copyValue(DataContainer dataContainer, Node fromNode, Node toNode, T defaultValue) {
		return copyValue(dataContainer, fromNode.getId(), toNode.getId(), defaultValue);
	}
	
	public boolean copyValue(DataContainer dataContainer, String fromId, String toId, T defaultValue) {
		String value = getStringValue(dataContainer, fromId, null);
    	if (value != null) {
    		setStringValue(dataContainer, toId, value);
    		return true;
    	}
    	else if (defaultValue != null) {
    		setValue(dataContainer, toId, defaultValue);
    		return true;
    	}
    	else {
    		return false;
    	}
	}
}
