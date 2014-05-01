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
package ch.tatool.core.element;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.tatool.data.PropertyHolder;

/**
 * Default implementation of a property holder.
 */
public abstract class AbstractPropertyHolder implements PropertyHolder {

    /** Properties. */
    private Map<String, Object> properties;
	
    public AbstractPropertyHolder() {
    	properties = new HashMap<String, Object>();
    }
    
    public Object getProperty(String name) {
        return properties.get(name);
    }

    public void setProperty(String name, Object value) {
		properties.put(name, value);
    }
    
    public void removeProperty(String name) {
    	properties.remove(name);
    }
    
    /**
     * Get all properties contained in this holder
     */
    public Set<String> getKeys() {
    	return properties.keySet();
    }
    
    /**
     * Clear all properties
     */
    public void clearProperties() {
    	properties.clear();
    }
}
