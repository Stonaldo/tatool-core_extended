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
package ch.tatool.core.module.creator;

import java.util.List;

import ch.tatool.module.ModuleCreator;
import ch.tatool.module.ModuleCreatorRegistry;

/**
 * Helper class allowing to register additional creators through Spring configuration independently
 * from the registry bean definition
 */
public class ModuleCreatorRegistryHelper {
	/** Registry to which creators should be added. */
	ModuleCreatorRegistry registry;
	
	/** Creators to be added. */
	List<ModuleCreator> creators;
	
	/** Should existing creators be cleared first? */
	boolean clearExisting = false;
	
	public ModuleCreatorRegistryHelper() {
	}
	
	/** Set the creators to add to the registry. */
	public void setCreators(List<ModuleCreator> creators) {
		this.creators = creators;
	}
	
	/** Set the registry reference. */
	public void setModuleCreatorRegistry(ModuleCreatorRegistry registry) {
		this.registry = registry;
	}
	
	/** Method to be called when all dependencies have been injected. */
	public void initialize() {
		if (registry != null && creators != null) {
			if (clearExisting) {
				registry.removeAll();
			}
			
			for (ModuleCreator creator : creators) {
				registry.addCreator(creator);
			}
		}
	}
}
