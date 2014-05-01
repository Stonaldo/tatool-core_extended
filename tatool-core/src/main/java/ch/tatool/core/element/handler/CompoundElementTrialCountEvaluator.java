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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.element.CompoundElement;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.element.Element;
import ch.tatool.exec.ExecutionContext;

/**
 * Only counts the trials of the primary element.
 * 
 * @author Michael Ruflin
 */
public class CompoundElementTrialCountEvaluator extends NodeImpl implements TrialCountEvaluator {

	Logger logger = LoggerFactory.getLogger(CompoundElementTrialCountEvaluator.class);
	
	/**
	 * Holds the evaluator to use on the primary element.
	 */
	TrialCountEvaluator baseEvaluator;
	
	/** Returns the trial count of the baseEvaluator in case the primary element
	 * is in the execution stack, 0 otherwise.
	 */
	public int getTrialCount(ExecutionContext context) {
		List<Element> elements = context.getElementStack();
		for (int x=elements.size() - 2; x >= 0; x--) {
			if (elements.get(x) instanceof CompoundElement) {
				CompoundElement ce = (CompoundElement) elements.get(x);
				
				// check that the stack element succeeding the compound element is the primary element
				if (elements.get(x + 1) == ce.getPrimary()) {
					if (baseEvaluator != null) {
						// return whatever the base evaluator returns, e.g. would return 0 for skip states
						return baseEvaluator.getTrialCount(context);
					} else {
						logger.warn("No base TrialCountEvaluator specified for CompoundElementTrialCountEvaluator.");
					}
				}
			}
		}
	
		// return 0 otherwise
		return 0;
	}

	public TrialCountEvaluator getBaseEvaluator() {
		return baseEvaluator;
	}

	public void setBaseEvaluator(TrialCountEvaluator baseEvaluator) {
		this.baseEvaluator = baseEvaluator;
	}	
}
