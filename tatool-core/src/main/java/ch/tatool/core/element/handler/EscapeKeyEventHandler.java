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
package ch.tatool.core.element.handler;

import java.awt.event.KeyEvent;

import ch.tatool.core.element.NodeImpl;
import ch.tatool.exec.ExecutionContext;

/**
 * Finishes the execution of the currently running module by catching
 * the key event of the Escape key.
 */
public class EscapeKeyEventHandler extends NodeImpl implements KeyEventHandler {

	private static final int SESSION_COMPLETED = 1;
	
	private boolean allowEscapeKey = true;

	public EscapeKeyEventHandler() {
		super("escape-key-event-handler");
	}
	
	public boolean isAllowEscapeKey() {
		return allowEscapeKey;
	}

	public void setAllowEscapeKey(boolean allowEscapeKey) {
		this.allowEscapeKey = allowEscapeKey;
	}
	
	public int getKeyEvent() {
		return KeyEvent.VK_ESCAPE;
	}
	
	public void triggerKeyEvent(int keyCode, ExecutionContext context) {
		if (keyCode == KeyEvent.VK_ESCAPE) {
			if (allowEscapeKey) {
				// make sure the session will be marked as completed as we allow to cancel it.
				if (context.getExecutionData().getModuleSession() != null) {
					context.getExecutionData().setSessionCompleted(SESSION_COMPLETED);
					context.getExecutor().stopExecution();
				}
			}
		}
	}

}
