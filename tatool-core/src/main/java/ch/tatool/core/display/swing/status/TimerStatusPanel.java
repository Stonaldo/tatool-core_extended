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

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * Status panel that displays a progress bar state.
 *
 * @author Michael Ruflin
 */
public class TimerStatusPanel extends TextStatusPanel implements StatusPanel {

	private static final long serialVersionUID = -34450340965975200L;
	
	public static final String PROPERTY_MIN_VALUE = "minValue";
	public static final String PROPERTY_MAX_VALUE = "maxValue";
	
	// images to load for this panel
	private static final String IMAGE_PREFIX = "/ch/tatool/core/ui/status/timer0";
	private static final String IMAGE_SUFFIX = ".png";
	private static final int IMAGE_COUNT = 7;
	
	private Map<Integer, Icon> imageMap;
	private int minValue = 0;
	private int maxValue = 100;
	private int currentValue;
	
	/** Creates new form TextStatusPanel */
    public TimerStatusPanel() {
    	super();
        loadImages();
    }
    
    // StatusPanel interface
    
    @Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	if (! enabled) {
    		getValueLabel().setIcon(null);
    	}
    }
    
    @Override
    public Object getProperty(String key) {
    	if (PROPERTY_VALUE.equals(key)) {
    		return currentValue;
    	}
    	else if (PROPERTY_MIN_VALUE.equals(key)) {
    		return minValue;
    	}
    	else if (PROPERTY_MAX_VALUE.equals(key)) {
    		return maxValue;
    	}
    	else {
    		return super.getProperty(key);
    	}
	}

	public void setProperty(String key, Object value) {
		if (PROPERTY_VALUE.equals(key)) {
    		if (value instanceof Number) {
    			currentValue = ((Number) value).intValue();
    			updateValueLabel();
    		}
    	}
    	else if (PROPERTY_MIN_VALUE.equals(key)) {
    		if (value instanceof Number) {
    			minValue = ((Number) value).intValue();
    		}
    	}
    	else if (PROPERTY_MAX_VALUE.equals(key)) {
    		if (value instanceof Number) {
    			maxValue = ((Number) value).intValue();
    		}
    	}
    	else {
    		super.setProperty(key, value);
    	}
	}

	private void loadImages() {
        imageMap = new HashMap<Integer, Icon>();
        for (int x=0; x < IMAGE_COUNT; x++) {
        	String url = IMAGE_PREFIX + x + IMAGE_SUFFIX;
        	Icon icon = new ImageIcon(getClass().getResource(url));
        	imageMap.put(x, icon);
        }
	}
	
	private void updateValueLabel() {
		// find out where on the scale from minValue to maxValue the currentValue is.
		// this implementation only shows minValue if currentValue = minValue
		int range = maxValue - minValue;
		int discreteValuesCount = IMAGE_COUNT - 1;
		// find the index to display
		int imageIndex = 0;
		for (int x=0; x <= discreteValuesCount; x++) {
			if (currentValue <= (minValue + (range * x / discreteValuesCount))) {
				imageIndex = x;
				break;
			}
		}
		
		getValueLabel().setIcon(imageMap.get(imageIndex));
	}
	
	/** Reset the panel to its default state. */
	@Override
	public void reset() {
		super.reset();
		setProperty(StatusPanel.PROPERTY_VALUE, minValue);
	}
}
