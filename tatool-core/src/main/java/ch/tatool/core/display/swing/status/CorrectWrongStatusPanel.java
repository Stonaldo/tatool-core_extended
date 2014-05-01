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
package ch.tatool.core.display.swing.status;

import javax.swing.ImageIcon;

import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;

/**
 * Status panel that provides either displays a green tick or a red cross for correct or wrong.
 *
 * @author Michael Ruflin
 */
public class CorrectWrongStatusPanel extends TextStatusPanel {
	
	private static final long serialVersionUID = -3813959762302660429L;
	
	private static final String IMAGE_LOCATION = "/ch/tatool/core/ui/status/";
	private static final String CORRECT_IMAGE_LOCATION = IMAGE_LOCATION  + "Correct.png";
	private static final String WRONG_IMAGE_LOCATION = IMAGE_LOCATION + "Wrong.png";
	
	private ImageIcon correctIcon;
	private ImageIcon wrongIcon;
	
	private Boolean currentValue;
	
	/** Creates new form TextStatusPanel */
    public CorrectWrongStatusPanel() {
        super();
        
        correctIcon = new ImageIcon(getClass().getResource(CORRECT_IMAGE_LOCATION));
        wrongIcon = new ImageIcon(getClass().getResource(WRONG_IMAGE_LOCATION));
    }
    
    // StatusPanel interface
    
    public Object getProperty(String key) {
    	if (PROPERTY_VALUE.equals(key)) {
    		return currentValue;
    	}
    	else {
    		return super.getProperty(key);
    	}
	}

	public void setProperty(String key, Object value) {
		if (PROPERTY_VALUE.equals(key)) {
    		if (value instanceof Boolean) {
    			currentValue = (Boolean) value;
    		} else {
    			String text = String.valueOf(value);
    			currentValue = Boolean.parseBoolean(text);
    		}
    		updateValueLabel();
    	} else {
    		super.setProperty(key, value);
    	}
	}

	private void updateValueLabel() {
		if (currentValue == null) {
			getValueLabel().setIcon(null);
		} else if (currentValue == true) {
			getValueLabel().setIcon(correctIcon);
		} else {
			getValueLabel().setIcon(wrongIcon);
		}
	}
	
	/** Reset the panel to its default state. */
	public void reset() {
		super.reset();
		getValueLabel().setIcon(null);
	}
}
