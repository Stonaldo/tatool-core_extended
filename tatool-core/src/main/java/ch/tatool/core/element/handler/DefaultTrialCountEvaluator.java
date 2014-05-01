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

import java.util.List;

import ch.tatool.core.data.Result;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.data.Trial;
import ch.tatool.element.Executable;
import ch.tatool.exec.ExecutionContext;

/**
 * Default trial count adaptor. Either adds 1 or 0, depending on whether a Result
 * property has been set or not
 * 
 * @author Michael Ruflin
 */
public class DefaultTrialCountEvaluator  extends NodeImpl implements TrialCountEvaluator {

	public int getTrialCount(ExecutionContext context) {
		Executable executable = context.getActiveExecutable();
		List<Trial> trials = context.getExecutionData().getTrials();
		if (!trials.isEmpty() && Result.getResultProperty().isSet(trials.get(0), executable)) {
			return 1;
		} else {
			return 0;
		}
	}

}
