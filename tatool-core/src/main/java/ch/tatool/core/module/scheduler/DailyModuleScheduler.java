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

import java.util.Calendar;
import java.util.GregorianCalendar;

import ch.tatool.data.Module;
import ch.tatool.data.ModuleSession;
import ch.tatool.module.ModuleSchedulerMessage;

/**
 * The DailyModuleScheduler only allows to execute one session a day starting from midnight. 
 * The start date of a session counts and not the end date when comparing the date of the last session with the current date.
 * Only completed sessions are compared allowing for an incomplete session to be repeated the same day.
 * 
 * Example:
 * - If a session is finished at 6 pm and the user would want to start another one at 8 pm a notice is displayed that the user has to wait
 * 4 hours until he can execute the next session since that would be 0 am of the next day.
 * 
 * @author Michael Ruflin
 */
public class DailyModuleScheduler extends AbstractModuleScheduler {

    /** Give the scheduler a chance to initialize. */
    public void initialize() {

    }

    public String getName() {
        return "DailyExecutionScheduler";
    }
    
    public ModuleSchedulerMessage isSessionStartAllowed(Module module) {
        Calendar todayDate = new GregorianCalendar();
        todayDate.set(Calendar.HOUR_OF_DAY, 0);
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);
        Calendar lastSessionDate = new GregorianCalendar();

        long numSessions = getDataService().getSessionCount(module, false);
        ModuleSession lastSession = getDataService().getLastSession(module, false);
        if (lastSession != null) {
            lastSessionDate.setTime(lastSession.getStartTime());
        } else {
            lastSessionDate.set(Calendar.YEAR, 1900);
        }
 
        ModuleSchedulerMessage message = new ModuleSchedulerMessageImpl();
        
        if (getMaxSessions() > 0 && numSessions >= getMaxSessions()) {
        	message.setSessionStartAllowed(false);
        	message.setMessageTitle("General.title");
        	message.setMessageText(messages.getString("ModuleScheduler.DailyModuleScheduler.errorMessage.maxSessions"));
        	return message;
        } else if (!lastSessionDate.before(todayDate)) {
        	message.setSessionStartAllowed(false);
        	Calendar currentDate = new GregorianCalendar();
        	int hours = (23 - currentDate.get(Calendar.HOUR_OF_DAY));
        	int minutes = (60 - currentDate.get(Calendar.MINUTE));
        	String errorString = messages.getString("ModuleScheduler.DailyModuleScheduler.errorMessage.sameDay");
        	errorString = errorString.replace("%hours", String.valueOf(hours));
        	errorString = errorString.replace("%minutes", String.valueOf(minutes));
        	message.setMessageTitle("General.title");
        	message.setMessageText(errorString);
            return message;
        } else {
        	message.setSessionStartAllowed(true);
            return message;
        }
    }
 
}
