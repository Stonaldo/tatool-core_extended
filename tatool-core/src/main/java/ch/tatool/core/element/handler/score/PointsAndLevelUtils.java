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
package ch.tatool.core.element.handler.score;

import ch.tatool.core.data.Misc;
import ch.tatool.data.Module;

/**
 * Utility methods for points and level handlers.
 * @author Michael Ruflin
 */
public class PointsAndLevelUtils {
	
	/** Property name used to store the registered PointsAndLevelHandlers in the module with "" as element name.
	 */
	public static final String PROPERTY_REGISTERED_POINTS_AND_LEVEL_HANDLER = "registeredPointsAndLevelHandler";
	
	private static final String REGISTERED_MARKER = "PointsAndLevelHandlerRegistered";

	/**
	 * Register a point and level handler onto the module.
	 * 
	 * This method can be called by PointsAndLevelHandlers as often as they wish.
	 * Upon first call for a handler, its id is registered in the module. If the handler is
	 * already registered, then this method won't have any effect.
	 */
	public static void registerPointAndLevelHandler(Module module, PointsAndLevelHandler handler) {
		// make sure we don't do this unnecessarily
		if (handler.getProperty(REGISTERED_MARKER) != null) {
			return;
		}
		
		String id = handler.getId();
		
		if (module.containsValue(id, PROPERTY_REGISTERED_POINTS_AND_LEVEL_HANDLER)) {
			return;
		} else {
			module.putValue(id, PROPERTY_REGISTERED_POINTS_AND_LEVEL_HANDLER, "");
		}

		// Also store the description of the handler
		Misc.getDescriptionProperty().setValue(module, handler);
		
		// mark the handler as registered (with the effect that this method is run for each handler
		// at most once per module instance
		handler.setProperty(REGISTERED_MARKER, "registered");
	}
    
}
