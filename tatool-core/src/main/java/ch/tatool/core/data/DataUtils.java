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

import java.io.UnsupportedEncodingException;

import ch.tatool.data.DataContainer;
import ch.tatool.data.DescriptivePropertyHolder;
import ch.tatool.data.Property;
import ch.tatool.data.PropertyHolder;
import ch.tatool.data.Module;

public class DataUtils {
	
    /** Get a String from the binary module properties. */
    public static String getStringBinaryModuleProperty(Module module, String propertyName) {
    	try {
			byte[] bytes = module.getBinaryModuleProperty(propertyName);
			String value = new String(bytes, "UTF-8");
			return value;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }
    
    /** Put a String into the binary module properties. */
    public static void putStringBinaryModuleProperty(Module module, String propertyName, String propertyValue) {
    	try {
			byte[] bytes = propertyValue.getBytes("UTF-8");
			module.putBinaryModuleProperty(propertyName, bytes);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
    }
    
    
    /** Removes all properties from the properties holder. */
    public static void clearPropertyHolder(PropertyHolder propertyHolder, Property<?>[] properties) {
		for (Property<?> property : properties) {
			property.clearValue(propertyHolder);
		}
    }
    
    public static void clearDescriptivePropertyHolder(DescriptivePropertyHolder propertyHolder) {
    	clearPropertyHolder(propertyHolder, propertyHolder.getPropertyObjects());
    }
    
    /**
     * Copies all properties from the holder onto a PropertiesContainer object.
     */
    public static void storeProperties(DataContainer dataContainer, PropertyHolder propertyHolder, Property<?>[] properties) {
		for (Property<?> property : properties) {
			property.setValue(dataContainer, propertyHolder);
		}
    }
    
    public static void storeProperties(DataContainer dataContainer, DescriptivePropertyHolder propertyHolder) {
    	storeProperties(dataContainer, propertyHolder, propertyHolder.getPropertyObjects());
    }
}
