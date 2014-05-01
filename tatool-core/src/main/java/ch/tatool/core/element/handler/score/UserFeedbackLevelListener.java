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

import ch.tatool.core.data.IntegerProperty;
import ch.tatool.core.data.Level;
import ch.tatool.core.data.Misc;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;
import ch.tatool.core.display.swing.panel.CenteredTextPanel;
import ch.tatool.core.element.ExecutableElement;
import ch.tatool.core.element.NodeImpl;
import ch.tatool.core.element.TemporaryElementSupportUtil;
import ch.tatool.core.element.handler.pause.PauseHandlerUtil;
import ch.tatool.core.executable.GenericContentExecutable;
import ch.tatool.data.Messages;
import ch.tatool.element.TemporaryElementSupport;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionOutcome;

public class UserFeedbackLevelListener extends NodeImpl implements PointsAndLevelHandler.LevelListener {

	public static final String PROPERTY_DISPLAY_DURATION = "displayDuration";
	private static final int DEFAULT_DISPLAY_DURATION = 3000;
	
	public String levelUpText;
	public String levelDownText;
	
	/** Property that encapsulates the display duration. */
	private IntegerProperty durationProperty = new IntegerProperty(PROPERTY_DISPLAY_DURATION);
	
	public UserFeedbackLevelListener() {
		// set a default duration
		setDisplayDuration(DEFAULT_DISPLAY_DURATION);
	}
	
	public void levelChanged(PointsAndLevelHandler handler, ExecutionContext context) {
		int levelChange = Level.getLevelChangeDeltaProperty().getValue(handler, 0); 
		insertLevelChangeInformationElement(context, levelChange);
	}

    /** Displays a visual level change. */
    public void insertLevelChangeInformationElement(ExecutionContext context, int levelChange) {
    	// fetch the duration we should display the level change.
    	int duration = durationProperty.getValue(this);
    	if (duration <= 0) {
    		return;
    	}
    	
    	// set default values for level-up/down text if not set manually
    	if (levelUpText == null || levelDownText == null) {
    		Messages messages = context.getExecutionData().getModule().getMessages();
    		if (levelUpText == null) {
    			levelUpText = messages.getString("Handler.UserFeedbackLevelListener.levelUp");
    		}
    		if (levelDownText == null) {
    			levelDownText = messages.getString("Handler.UserFeedbackLevelListener.levelDown");
    		}
    	}
    	
    	CenteredTextPanel textPanel = new CenteredTextPanel();
    	if (levelChange > 0) {
    		textPanel.setText(levelUpText);
    	} else {
    		textPanel.setText(levelDownText);
    	}
    	GenericContentExecutable changeLevelExecutable = new GenericContentExecutable();
    	changeLevelExecutable.addContent(Region.CENTER, textPanel);
    	changeLevelExecutable.setDisplayDuration(duration);
    	Misc.getOutcomeProperty().setValue(changeLevelExecutable, ExecutionOutcome.SKIP);
    	TemporaryElementSupport support = TemporaryElementSupportUtil.getInstance().findTemporaryElementSupport(context);
    	if (support != null) {
    		ExecutableElement element = new ExecutableElement(changeLevelExecutable);
    		support.addTemporaryElement(element);
    	}
    	
    	// don't pause before executing the temporary element
    	PauseHandlerUtil.setCurrentInterElementPauseDuration(context, 0L);
    }
    
    public void setDisplayDuration(int displayDuration) {
    	durationProperty.setValue(this, displayDuration);
    }
    
    public int getDisplayDuration() {
    	return durationProperty.getValue(this);
    }
	
	public String getLevelUpText() {
		return levelUpText;
	}

	public void setLevelUpText(String levelUpText) {
		this.levelUpText = levelUpText;
	}

	public String getLevelDownText() {
		return levelDownText;
	}

	public void setLevelDownText(String levelDownText) {
		this.levelDownText = levelDownText;
	}
}
