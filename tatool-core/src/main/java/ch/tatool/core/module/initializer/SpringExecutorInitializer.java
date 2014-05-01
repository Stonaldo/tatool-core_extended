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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;

import ch.tatool.core.data.DataUtils;
import ch.tatool.core.element.DefaultExecutionStrategy;
import ch.tatool.core.module.creator.CreationException;
import ch.tatool.data.Module;
import ch.tatool.element.Element;
import ch.tatool.exec.Executor;
import ch.tatool.export.DataExporter;
import ch.tatool.module.ExecutorInitializer;

/**
 * ExecutorConfiguration which loads the elements from a Spring context file. 
 * 
 * @author Michael Ruflin
 */
public class SpringExecutorInitializer implements ExecutorInitializer {

	private static final String ROOT_ELEMENT = "moduleHierarchy";
	
	private ApplicationContext moduleContext;

	/** Logger used by the service. */
    Logger logger = LoggerFactory.getLogger(SpringExecutorInitializer.class);
	
    public static final String SPRING_ELEMENT_CONFIGURATION_XML_PROPERTY = "SpringXmlElementConfig";

    /** Configures the executor with the given module. */
	public void initialize(Executor executor, Module module) {
		// load the root execution element
		Element element = fetchRootElement(module);
		executor.getExecutionTree().setRootElement(element);
		
		// instantiate the execution strategy
		setupExecutionStrategy(executor, module);
	}
	
	protected void setupExecutionStrategy(Executor executor, Module module) {
		// TODO: make this configurable through a module property
		executor.setExecutionStrategy(new DefaultExecutionStrategy());
	}
	
	/**
	 * Loads the data contained in the configuration file into the module and binary module properties.
	 * 
	 * This method can be used to get data required for a module creation.
	 */
	public boolean loadModuleConfiguration(File configurationXmlFile, Map<String, String> moduleProperties, Map<String, byte[]> binaryModuleProperties,  Map<String, DataExporter> moduleExporters) {
		// load the file into a String
        String configurationXML = null;
        try {
            configurationXML = FileUtils.readFileToString(configurationXmlFile, "UTF-8");
            return loadModuleConfiguration(configurationXML, moduleProperties, binaryModuleProperties, moduleExporters);
        } catch (IOException ioe) {
            logger.error("Unable to load Tatool file.", ioe);
            return false;
        }
	}
	
	public boolean loadModuleConfiguration(String configurationXML, Map<String, String> moduleProperties, Map<String, byte[]> binaryModuleProperties, Map<String, DataExporter> moduleExporters) {
		// test-wise load configuration to ensure all classes are valid
		loadRootElementFromSpringXML(configurationXML);
		
		// fetch the module properties
		Map<String, String> setupProperties = getModuleSetupProperties(configurationXML);
		moduleProperties.putAll(setupProperties);
		
		// fetch the module exporters
		Map<String, DataExporter> exporterList = getModuleExporters(configurationXML);
		moduleExporters.putAll(exporterList);
		
		// write the configuration file and configuration class into the properties too
		try {
			moduleProperties.put(Module.PROPERTY_MODULE_EXECUTION_INITIALIZER_CLASS, this.getClass().getName());
			binaryModuleProperties.put(SPRING_ELEMENT_CONFIGURATION_XML_PROPERTY, configurationXML.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("Unable to load Tatool file.", e);
			throw new RuntimeException(e);
		}
		
		return true;
	}
	
    @SuppressWarnings("unchecked")
	protected Map<String, String> getModuleSetupProperties(String configurationXML) {
        // see whether we have module properties
        if (moduleContext.containsBean("moduleProperties")) {
            Map<String, String> properties = (Map<String, String>) moduleContext.getBean("moduleProperties");
            return properties;
        } else {
            return Collections.emptyMap();
        }
    }
    
    @SuppressWarnings("unchecked")
    protected Map<String, DataExporter> getModuleExporters(String configurationXML) {
        // see whether we have module exporters
        if (moduleContext != null && moduleContext.containsBean("moduleExporters")) {
        	Map<String, DataExporter> exporters = (Map<String, DataExporter>) moduleContext.getBean("moduleExporters");
            return exporters;
        } else {
            return Collections.emptyMap();
        }
    }

    /**
     * Loads the persisted module configuration
     */
    protected Element fetchRootElement(Module module) {
    	// fetch the configuration
    	String configXML = DataUtils.getStringBinaryModuleProperty(module, SPRING_ELEMENT_CONFIGURATION_XML_PROPERTY);
        if (configXML == null) {
            // TODO: inform user that module is broken
            throw new RuntimeException("Unable to load Tatool file. Configuration is missing!");
        }
        
        // load the configuration
        return loadRootElementFromSpringXML(configXML);
    }
    
    /**
     * Checks whether a configuration xml is valid or not.
     */
    protected Element loadRootElementFromSpringXML(String configXML) {
        // try loading the configuration
    	moduleContext = null;
        try {
        	moduleContext = new GenericXmlApplicationContext( new ByteArrayResource( configXML.getBytes() ) );
        } catch (BeansException be) {
        	logger.error("Unable to load Tatool file.", be);
            throw new RuntimeException("Unable to load Tatool file.");
        }
        
        // check whether we have the mandatory beans (rootElement)
        if (! moduleContext.containsBean(ROOT_ELEMENT)) {
        	logger.error("Unable to load Tatool file. Root element missing!");
        	throw new CreationException("Unable to load Tatool file. Root element missing!");       
        }
        
        // fetch the rootElement
        try {
        	Element root = (Element) moduleContext.getBean(ROOT_ELEMENT);
        	return root;
        } catch (RuntimeException e) {
        	String[] errors = e.getMessage().split(";");
        	throw new CreationException(errors[errors.length-1]);
        }
    }
}
