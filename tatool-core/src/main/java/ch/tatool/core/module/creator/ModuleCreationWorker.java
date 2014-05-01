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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import ch.tatool.core.data.StringProperty;
import ch.tatool.core.module.initializer.SpringExecutorInitializer;
import ch.tatool.data.Messages;
import ch.tatool.data.Module;
import ch.tatool.data.UserAccount;
import ch.tatool.export.DataExporter;
import ch.tatool.module.ModuleService;

public class ModuleCreationWorker implements Worker {
	
	Logger logger = LoggerFactory.getLogger(ModuleCreationWorker.class);

	private String errorTitle;
	private String errorText;
	private Module module;
	private Messages messages;
	
	/** Create a module given a configuration file
	 * 
	 * @param moduleService
	 * @param configurationFile
	 * @param defaultModuleName a name to use for the module if the name property
	 *        has not been set. If null and not set, this will cause an error to be thrown
	 */
	public void createModule(ModuleService moduleService, UserAccount userAccount, File configurationFile, File moduleDataFile, String defaultModuleName, boolean failIfNameExists) {
		module = null;
		errorTitle = null;
		errorText = null;
		
		try {
			Map<String, String> properties = new HashMap<String, String>();
			Map<String, byte[]> binaryProperties = new HashMap<String, byte[]>();
			Map<String, DataExporter> moduleExporters = new HashMap<String, DataExporter>();
			
			// load the module from file
			SpringExecutorInitializer configuration = new SpringExecutorInitializer();
			configuration.loadModuleConfiguration(configurationFile, properties, binaryProperties, moduleExporters);

			// check whether the configuration specifies a name
			String moduleName = properties.get(Module.PROPERTY_MODULE_NAME);

			if (moduleName == null) {
				if (defaultModuleName != null) {
					moduleName = defaultModuleName;
				} else {
					// fail
					errorTitle = messages.getString("General.errorMessage.windowTitle.error");
					errorText = messages.getString("General.creator.dataServerCreator.errorMessage.missingModuleName");
					return;
				}
			}			

			// make sure the module name is unique
			Set<Module.Info> modules = moduleService.getModules(userAccount);
			boolean uniqueNameFound = false;
			String proposedName = moduleName;

			int x = 1;
			outer: do {
				for (Module.Info info : modules) {
					String name = info.getName();
					if (name.equals(proposedName)) {
						// name already exists
						if (failIfNameExists) {
							errorTitle = messages.getString("General.errorMessage.windowTitle.error");
							errorText = messages.getString("General.creator.dataServerCreator.errorMessage.alreadyExists");
							return;
						} else {
							proposedName = moduleName + " (" + x + ")";
							x++;
							continue outer;
						}
					}
				}
				// name not found - accept
				uniqueNameFound = true;
			} while (! uniqueNameFound);

			// set the final name 
			properties.put(Module.PROPERTY_MODULE_NAME, proposedName);
			
			// create the module
			module = moduleService.createModule(userAccount,properties, binaryProperties, moduleExporters);
			
			loadModuleData(moduleService, moduleDataFile);

		} catch (CreationException ce) {
			logger.warn(ce.getMessage());
			errorTitle = "Error";
			errorText = "The following error occurred while trying to open the module file:\n" + ce.errorMessage;
		} catch (RuntimeException e) {
			logger.warn(e.getMessage());
			errorTitle = "Error";
			errorText = "An unknown error has occurred. Make sure the module file is correct.";
		}
	}

	/**
	 * Add module data from tatool online
	 * 
	 * @param moduleService
	 * @param moduleDataFile
	 */
	private void loadModuleData(ModuleService moduleService, File moduleDataFile) {
		// add module data if available
		if (moduleDataFile != null) {
			
			List<String[]> moduleData = new ArrayList<String[]>();
			CSVReader reader = null;
			try {
				reader = new CSVReader(new InputStreamReader(new FileInputStream(moduleDataFile),
						"ISO-8859-1"), ';');
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				moduleData = reader.readAll();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
			
			// add module data to module
			for (int i = 0; i < moduleData.size(); i++) {
				String[] data = moduleData.get(i);

				String nodeID = "";
				String propertyName = "";
				String propertyValue = "";
				if (data[0] == null) {
					nodeID = "";
				} else {
					nodeID = data[0];
				}
				if (data[1] == null) {
					propertyName = "";
				} else {
					propertyName = data[1];
				}
				if (data[2] == null) {
					propertyValue = "";
				} else {
					propertyValue = data[2];
				}
				StringProperty dataProperty = new StringProperty(propertyName);
				dataProperty.setValue(module, nodeID, propertyValue);
			}
			moduleService.saveModule(module);
		}
	}

	public boolean hasFinished() {
		return module != null;
	}
	
	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
	public void setMessages(Messages messages) {
    	this.messages = messages;
    }
    
    public Messages getMessages() {
		return messages;
	}
}
