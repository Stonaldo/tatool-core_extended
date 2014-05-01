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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.tatool.data.Messages;
import ch.tatool.data.Module;
import ch.tatool.data.DataService;
import ch.tatool.data.ModuleSession;
import ch.tatool.module.ModuleScheduler;
import ch.tatool.module.ModuleSchedulerMessage;

/**
 * Abstract module scheduler. Implements module and dataservice properties.
 * 
 * @author Michael Ruflin
 */
public abstract class AbstractModuleScheduler implements ModuleScheduler {

    /** Module associated with this scheduler. */
    private Module module;
    
    /** Session service to obtain session information from a module object. */
    private DataService dataService;
    
    /** The i18n messages object used to get the correct text values. */
    public Messages messages;
    
    /** The maximum amount of sessions allowed for a module controlled by this scheduler */
    private int maxSessions = 0;
    
    /** Set the module this scheduler should manage. */
    public void setModule(Module module) {
        this.module = module;
    }
    
    /** Set the dataService it might need. */
    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
    
    /** Set the messages object used for i18n of text values */
    public void setMessages(Messages messages) {
    	this.messages = messages;
    }
    
    /** Sets the maximum of sessions allowed for a module controlled by this scheduler */
    public void setMaxSessions(Integer maxSessions) {
    	this.maxSessions = maxSessions;
    }
    
    /** Get the maximum of sessions allowed for a module controlled by this scheduler */
    public int getMaxSessions() {
    	return maxSessions;
    }

    /**
     * Gets the module this scheduler is used for.
     * 
     * @return module
     */
    public Module getModule() {
        return module;
    }

    /**
     * Gets the ModuleDataService this scheduler is using.
     * 
     * @return dataService
     */
    public DataService getDataService() {
        return dataService;
    }
 
    public String getSchedulerNumSessions(Module module) {
    	 long sessionCount = dataService.getSessionCount(module, false);
    	 return String.valueOf(sessionCount);
    }
    
    public String getSchedulerLastSessionDate(Module module) {
    	ModuleSession lastSession = dataService.getLastSession(module, false);
    	String lastSessionString = "";
    	if (lastSession != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date d = lastSession.getStartTime();
            lastSessionString = dateFormat.format(d);
        } else {
        	 lastSessionString = "-";
        }
    	return lastSessionString;
    }
    
    

    public class ModuleSchedulerMessageImpl implements ModuleSchedulerMessage {
    	private boolean isSessionStartAllowed;
    	private String messageTitle;
    	private String messageText;
    	
    	public ModuleSchedulerMessageImpl() {
    	}
    	
    	public ModuleSchedulerMessageImpl(boolean isSessionStartAllowed) {
    		this.isSessionStartAllowed = isSessionStartAllowed;
    	} 
    	
    	public boolean isSessionStartAllowed() {
			return isSessionStartAllowed;
		}

		public void setSessionStartAllowed(boolean isSessionStartAllowed) {
			this.isSessionStartAllowed = isSessionStartAllowed;
		}
 
    	public String getMessageTitle() {
			return messageTitle;
		}

		public void setMessageTitle(String messageTitle) {
			this.messageTitle = messageTitle;
		}
	   	
    	public String getMessageText() {
			return messageText;
		}

		public void setMessageText(String messageText) {
			this.messageText = messageText;
		}	
    }

}
