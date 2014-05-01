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
package ch.tatool.core.display.swing.status;

/**
 * Provides access to a StatusRegionInstance
 * 
 * @author Michael Ruflin
 */
public class StatusRegionUtil {
	private static StatusRegionImpl instance;
	
	/** Get the status region if existing.
	 * @return the StatusRegion object or null if not available.
	 */
	public static StatusRegionImpl getInstance() {
		if (instance == null) {
			StatusRegionImpl statusRegion = new StatusRegionImpl();
			setStatusRegion(statusRegion);
		}
		return instance;
	}
	
	/**
	 * Set the status region to use.
	 * @param statusRegion
	 */
	public static void setStatusRegion(StatusRegionImpl statusRegion) {
		instance = statusRegion;
	}
	
	/**
	 * Get a Panel out of the StatusRegion.
	 * This is a shortcut for getInstance().getStatusPanel(String id)
	 */
	public static StatusPanel getStatusPanel(String statusPanelId) {
		if (instance != null) {
			return instance.getStatusPanel(statusPanelId);
		} else {
			return null;
		}
	}
	
	/** Is a status region configured. */
	public boolean available() {
		return instance != null;
	}
}
