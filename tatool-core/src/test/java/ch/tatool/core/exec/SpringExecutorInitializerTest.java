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
package ch.tatool.core.exec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ch.tatool.core.module.initializer.SpringExecutorInitializer;
import ch.tatool.export.DataExporter;

public class SpringExecutorInitializerTest {

	/**
	 * Tests whether the Spring configuration file can be loaded ok
	 */
    @Test
    public void testLoadConfigurationFromFile() throws IOException {
		// load configuration file
		String testConfiguration = IOUtils.toString(getClass().getResourceAsStream("/configurations/test-module-configuration.xml"));
		
		// load the configuration properties
		Map<String, String> moduleProps = new HashMap<String,String>();
		Map<String, byte[]> binaryModuleProps = new HashMap<String, byte[]>();
		Map<String, DataExporter> moduleExporters = new HashMap<String, DataExporter>();
		SpringExecutorInitializer configuration = new SpringExecutorInitializer();
		configuration.loadModuleConfiguration(testConfiguration, moduleProps, binaryModuleProps, moduleExporters);
    }
}
