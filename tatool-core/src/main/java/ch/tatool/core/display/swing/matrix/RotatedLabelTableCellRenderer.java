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
package ch.tatool.core.display.swing.matrix;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer that rotates the label depending on the value provided by the model
 * 
 * The text value can have following suffixes that describe the different mirroring options:
 * [H] = horizontal mirror
 * [V] = vertical mirror
 * [R] = rotate by 90 degrees
 * [N] = normal left to right text (has no effect in fact)
 */
public class RotatedLabelTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -4661685482832800216L;
	
	private RotatedLabelUI labelUI;
	private Font labelFont;
	
	public RotatedLabelTableCellRenderer() {
		super();
		
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		
		labelFont = getFont();
		labelUI = new RotatedLabelUI();
		setUI(labelUI);
	}
	
	public Font getLabelFont() {
		return labelFont;
	}

	public void setLabelFont(Font labelFont) {
		this.labelFont = labelFont;
	}

	/** Increase the label font by x points. */
	public void increaseLabelFont(float points) {
		labelFont = labelFont.deriveFont(labelFont.getSize2D() + points);
	}
	
	/** 
	 * Adapts the labelUI prior to letting the parent class do the work.
	 */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
    	boolean mirrorHorizontally = false;
    	boolean mirrorVertically = false;
    	boolean rotateByNinty = false;
    	
    	String v = null;
    	if (value != null) {
    		v = value.toString();
	    	while (v.endsWith("]")) {
	    		if (v.endsWith("[H]")) {
	    			mirrorHorizontally = true;
	    			v = v.substring(0, v.length() - 3);
	    		} else if (v.endsWith("[V]")) {
	    			mirrorVertically = true;
	    			v = v.substring(0, v.length() - 3);
	    		} else if (v.endsWith("[R]")) {
	    			rotateByNinty = true;
	    			v = v.substring(0, v.length() - 3);
	    		} else if (v.endsWith("[N]")) {
	    			// just cut off the label
	    			v = v.substring(0, v.length() - 3);
	    		}
	    	}
    	}
    	
    	// update the ui
    	labelUI.setMirrorHorizontally(mirrorHorizontally);
    	labelUI.setMirrorVertically(mirrorVertically);
    	labelUI.setRotateByNinty(rotateByNinty);

    	// give the parent a chance to further setup the label, replace value by v
    	Component c = super.getTableCellRendererComponent(table, v, isSelected, hasFocus, row, column);
    	
    	// finally set the font (as the superclass fetches the font from the table (doooh)
    	c.setFont(labelFont);
    	
    	return c;
    }
    
    public static void main(String[] args) {
    	FixedSizeTable.main(args);
    }
    
}
