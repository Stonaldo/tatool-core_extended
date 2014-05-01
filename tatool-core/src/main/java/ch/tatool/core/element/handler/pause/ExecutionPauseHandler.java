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
package ch.tatool.core.element.handler.pause;

import ch.tatool.element.Node;

/**
 * Handler that pauses the execution between two ExecutableElements.
 * 
 * @author Michael Ruflin
 */
public interface ExecutionPauseHandler extends Node {

	/**
	 * Set the duration for which the module is paused before executing the next element.
	 */
	public void setCurrentInterElementPauseDuration(long currentInterElementPauseDuration);

	/** Get the duration of the pause after the current element execution.
	 * return the pause duration after the current  
	 */
	public long getCurrentInterElementPauseDuration();
	
	/** Set the default duration for which the execution is paused in between two elements.
	 * @param duration the duration in milliseconds
	 */
	public void setDefaultInterElementPauseDuration(long defaultInterElementPauseDuration);
	
	/** Get the default pause duration between two element executions.
	 * @return the duration in milliseconds
	 */
	public long getDefaultInterElementPauseDuration();
}
