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

import ch.tatool.core.element.ExecutableElement;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.core.element.TemporaryElementSupportUtil;
import ch.tatool.core.element.handler.pause.PauseHandlerUtil;
import ch.tatool.core.executable.ConfidenceRatingExecutable;
import ch.tatool.element.TemporaryElementSupport;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionPhaseListener;

public class ConfidenceRatingHandler extends NodeImpl implements ExecutionPhaseListener {

	public void processExecutionPhase(ExecutionContext context) {
		switch(context.getPhase()) {
    	case POST_PROCESS:
    		startRating(context);
    		break;
    	default:
    		// do nothing
    	}
	}

	/**
	 * Adds a confidence rating executable after the parent element
	 * @param context
	 */
	private void startRating(ExecutionContext context) {
		 // only set if we are applied to the current task
		if (this.getParent() == context.getActiveElement()) {
			ConfidenceRatingExecutable conf = new ConfidenceRatingExecutable();
			conf.setLocalId("confidence-rating");
			
			TemporaryElementSupport support = TemporaryElementSupportUtil.getInstance().findTemporaryElementSupport(context);
	    	if (support != null) {
	    		ExecutableElement element = new ExecutableElement(conf);
	    		support.addTemporaryElement(element);
	    	}

	    	PauseHandlerUtil.setCurrentInterElementPauseDuration(context, 0L);
		}
	}
}
