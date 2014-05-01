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
package ch.tatool.core.module.initializer;

import ch.tatool.core.element.DefaultExecutionStrategy;
import ch.tatool.data.Module;
import ch.tatool.element.Element;
import ch.tatool.element.ElementTree;
import ch.tatool.element.ExecutionStrategy;
import ch.tatool.exec.Executor;
import ch.tatool.module.ExecutorInitializer;

/**
 * Sets the root element
 * @author Michi
 *
 */
public class MemoryExecutorInitializer implements ExecutorInitializer {

	/** Root execution element. */
	private Element rootElement;
	
	/** Execution strategy. */
	private ExecutionStrategy strategy;
	
	public MemoryExecutorInitializer() {
		this(null, new DefaultExecutionStrategy());
	}
	
	public MemoryExecutorInitializer(Element rootElement) {
		this(rootElement, new DefaultExecutionStrategy());
	}
	
	public MemoryExecutorInitializer(Element rootElement, ExecutionStrategy strategy) {
		this.rootElement = rootElement;
		this.strategy = strategy;
	}
	
	public void initialize(Executor executor, Module module) {
		executor.setExecutionStrategy(strategy);
		ElementTree tree = executor.getExecutionTree();
		tree.setRootElement(rootElement);
	}

	public Element getRootElement() {
		return rootElement;
	}

	public void setRootElement(Element rootElement) {
		this.rootElement = rootElement;
	}

	public ExecutionStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ExecutionStrategy strategy) {
		this.strategy = strategy;
	}

}
