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
package ch.tatool.core.element.handler.score;

import ch.tatool.element.Node;
import ch.tatool.exec.ExecutionContext;

/**
 * Interface to be implemented by score and level handlers.
 * 
 * @author Michael Ruflin
 */
public interface PointsAndLevelHandler extends Node {
    
    /**
     * Point adaptor.
     * 
     * Aspects implementing this interface can adapt the points provided by the task prior to
     * being processed by the ScoreAndLevel handler. The ScoreAndLevel handler checks all aspects
     * of the task in question and gives each aspect implementing this interface the chance to adapt
     * the points. That way points of tasks can be adapted without having to alter the task in any way.
     */
    static interface PointAdaptor {
        /**
         * Adapt the passed in points.
         * 
         * @param point an array of points, where [0] is min, [1] is max, [2] is actual points
         * @return the adapted points
         */
        public void adaptPoints(PointsAndLevelHandler handler, ExecutionContext context);
    }
    
    /**
     * Listener for level changes.
     *
     * Aspects interested in level changes should implement this interface
     */
    static interface LevelListener {
        /**
         * Implementors get called by the PointsAndLevelHandler whenever the level changes.
         * 
         * @param interceptor the interceptor that calls the method
         * @param context the ExecutionContext
         * @param oldLevel the old level value
         * @param newLevel the new level value
         */
        public void levelChanged(PointsAndLevelHandler handler, ExecutionContext context);
    }
}
