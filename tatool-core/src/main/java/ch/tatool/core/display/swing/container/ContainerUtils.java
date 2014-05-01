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
package ch.tatool.core.display.swing.container;

import ch.tatool.core.display.swing.SwingExecutionDisplay;

/**
 * Provides access to different containers provided to the elements.
 * @author Michael Ruflin
 */
public class ContainerUtils {
	
	public static final String REGIONS_CONTAINER_KEY = "regionsContainer";
	
	private static RegionsContainer regionsContainer;
	
	public static void initialize(SwingExecutionDisplay executionDisplay) {
		regionsContainer = new RegionsContainer();
		executionDisplay.addCard(REGIONS_CONTAINER_KEY, regionsContainer);
	}
	
	public static void showRegionsContainer(SwingExecutionDisplay executionDisplay) {
		executionDisplay.showCard(REGIONS_CONTAINER_KEY);
	}

	public static RegionsContainer getRegionsContainer() {
		return regionsContainer;
	}
	
	public static void setRegionsContainer(RegionsContainer regionsContainer) {
		ContainerUtils.regionsContainer = regionsContainer;
	}

}
