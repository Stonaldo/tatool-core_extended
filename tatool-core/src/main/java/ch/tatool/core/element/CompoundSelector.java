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

import ch.tatool.core.data.Misc;
import ch.tatool.element.Element;
import ch.tatool.element.ElementSelector;
import ch.tatool.element.ElementTree;
import ch.tatool.element.Initializable;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionOutcome;
import ch.tatool.exec.ExecutionPhaseListener;

/** Selects the next execution element to execute. */
public class CompoundSelector implements ExecutionPhaseListener, ElementSelector, Initializable {

	private static final String EXECUTE_PRIMARY = "executePrimary";
	private static final String EXECUTE_SECONDARY = "executeSecondary";
	private static final String DONE = "done";

	private CompoundElement compoundElement;
    
    public CompoundSelector() {
    }
    
    protected boolean isExecutePrimary() {
		return (Boolean) compoundElement.getProperty(EXECUTE_PRIMARY);
	}
    
    protected boolean isExecuteSecondary() {
		return (Boolean) compoundElement.getProperty(EXECUTE_SECONDARY);
	}

	protected void setExecutePrimary(boolean executePrimary) {
		compoundElement.setProperty(EXECUTE_PRIMARY, executePrimary);
	}
	
	protected void setExecuteSecondary(boolean executeSecondary) {
		compoundElement.setProperty(EXECUTE_SECONDARY, executeSecondary);
	}

	public boolean isDone() {
		return (Boolean) compoundElement.getProperty(DONE);
	}

	protected void setDone(boolean done) {
		compoundElement.setProperty(DONE, done);
	}
	
	public void initialize(Element element) {
		if (! (element instanceof CompoundElement)) {
			throw new RuntimeException("CompoundSelector only supports Elements of type CompoundElement");
		}
		compoundElement = (CompoundElement) element;
		
		setExecutePrimary(true);
		setExecuteSecondary(false);
		setDone(false);
		
        // initialize the primary element on selector initialization as we only run the primary once
		ElementUtils.initialize(compoundElement, compoundElement.getPrimary());
    }

	public boolean selectNextElement(ExecutionContext context) {

		// handle done case or if we haven't got a primary element
		if (compoundElement.getPrimary() == null || isDone()) {
			return false;
		}
		
		// handle the switch back to the primary element if secondary is done
		String lastOutcome = Misc.getOutcomeProperty().getValue(context);

		if (isExecuteSecondary() && ! ExecutionOutcome.SUSPENDED.equals(lastOutcome)) {
			compoundElement.getPrimary().setProperty(Element.EXECUTED, null);
            setExecutePrimary(true);
		}
		
		// handle primary case
		ElementTree tree = context.getExecutor().getExecutionTree();
		if (isExecutePrimary() || compoundElement.getSecondary() == null) {
			tree.pushElement(compoundElement.getPrimary());
			ElementUtils.initialize(compoundElement.getPrimary());
			return true;
		} else {
            // we also initialize the secondary element on each run
			ElementUtils.initialize(compoundElement.getSecondary());
        	tree.pushElement(compoundElement.getSecondary());
        	setExecuteSecondary(true);
            return true;
        }
	}
	
	/** Called on phase events. */
	public void processExecutionPhase(ExecutionContext event) {
		switch (event.getPhase()) {
		case POST_PROCESS:
			executionPostProcess(event);
		}
	}
	
    /**
     * Analyze the trial outcome, switch between primary and secondary depending on the outcome
     */
    private void executionPostProcess(ExecutionContext context) {

    	String lastOutcome = Misc.getOutcomeProperty().getValue(context);
        if (isExecutePrimary()) {
            if (ExecutionOutcome.SUSPENDED.equals(lastOutcome)) {
                setExecutePrimary(false);
            } else {
            	setDone(true);
            }
        }
    }
}
