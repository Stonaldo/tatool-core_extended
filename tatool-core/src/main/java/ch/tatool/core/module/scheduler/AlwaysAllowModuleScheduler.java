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
package ch.tatool.core.module.scheduler;

import ch.tatool.data.Module;
import ch.tatool.data.ModuleSession;

/**
 * Always allows creating new sessions and does not impose any limits on the session duration.
 * The user can terminate a session any time.
 * 
 * @author Michael Ruflin
 */
public class AlwaysAllowModuleScheduler extends AbstractModuleScheduler {
    
    /** Give the scheduler a chance to initialize. */
    public void initialize() {
        
    }
    
    public String getName() {
        return "AlwaysAllowModuleScheduler";
    }

    public boolean canUserTerminateSession(ModuleSession moduleSession) {
        // user can terminate session at will
        return true;
    }

    public ModuleSchedulerMessageImpl isSessionStartAllowed(Module module) {
        // always allow session creation
    	ModuleSchedulerMessageImpl message = new ModuleSchedulerMessageImpl(true);
        return message;
    }
}
