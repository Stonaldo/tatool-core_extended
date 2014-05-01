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

import ch.tatool.element.Element;
import ch.tatool.element.Initializable;

/**
 * Iterates through the list of elements, executing each element in order until timeout.
 * 
 * @author Andre Locher
 */
public class TemporalListSelector extends IteratedListSelector implements Initializable {

    private static long convertRateToMilliseconds = 1000000;
    private long startTime = 0;
    private long timeout = 10000;
    
    public void initialize(Element element) {
    	super.initialize(element);
    	startTime = getNanoTime();
    }
	
	/** Can a next element be executed.
	 * Checks whether the time is up already
	 */
	protected boolean canExecuteNext() {
		long duration = (getNanoTime() - startTime) / convertRateToMilliseconds;
		return (duration < timeout);
	}
	
    private long getNanoTime() {
    	return System.nanoTime();
    }
        
    public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
