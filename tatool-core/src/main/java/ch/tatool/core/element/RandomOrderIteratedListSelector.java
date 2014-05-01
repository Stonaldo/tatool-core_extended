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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ch.tatool.element.Element;
import ch.tatool.exec.ExecutionContext;


/**
 * Serves randomly a new element from the group.
 * 
 * The amount of elements served can be specified.
 * 
 * @author Michael Ruflin
 */
public class RandomOrderIteratedListSelector extends IteratedListSelector {

    /**
     * Creates an iterator that returns the elements in the group in a random order
     * compared to the standard order the elements have been added to the list
     */
    protected Iterator<Element> createIterator(ExecutionContext context) {
        List<Element> elements = new ArrayList<Element>(getExecutionElement().getChildren());
        Collections.shuffle(elements);
        return elements.iterator();
    }
}
