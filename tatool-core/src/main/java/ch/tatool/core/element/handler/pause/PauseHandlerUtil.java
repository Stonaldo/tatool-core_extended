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

import ch.tatool.core.element.ElementUtils;
import ch.tatool.exec.ExecutionContext;

public class PauseHandlerUtil {
	
	/** 
	 * Finds the deepest ExecutionpauseHandler attached to the current context.
	 * 
	 * @param context the context to search through
	 * @return a handler or null if none is available
	 */
	public static ExecutionPauseHandler findExecutionPauseHandler(ExecutionContext context) {
		return (ExecutionPauseHandler) ElementUtils.findHandlerInStackByType(context, ExecutionPauseHandler.class);
	}

	/**
	 * Sets the current inter pause duration if an ExecutionHandler can be found,
	 * does nothing otherwise.
	 * @return whether the duration was successfully set
	 */
	public static boolean setCurrentInterElementPauseDuration(ExecutionContext context, long duration) {
		ExecutionPauseHandler handler = findExecutionPauseHandler(context);
		if (handler != null) {
			handler.setCurrentInterElementPauseDuration(duration);
			return true;
		} else {
			return false;
		}
	}
	
}
