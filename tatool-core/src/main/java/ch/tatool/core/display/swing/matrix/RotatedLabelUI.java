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

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

import sun.swing.SwingUtilities2;

/**
 * Implementation of a rotated label UI.
 * 
 * Currently the UI allows mirroring the content horizonally and vertically as well as rotating it by 90 degrees.
 *  
 * Note: The code has been inspired by http://www.codeguru.com/java/articles/199.shtml
 *  
 * @author Michael Ruflin
 */
public class RotatedLabelUI extends BasicLabelUI
{
	/*
	static {
		labelUI = new RotatedLabelUI(false);
	}
	*/
	
	protected boolean mirrorHorizontally;
	protected boolean mirrorVertically;
	protected boolean rotateByNinty;
	
	public RotatedLabelUI () {
		super();
	}
	
	public RotatedLabelUI (boolean mirrorHorizontally, boolean mirrorVertically, boolean rotateByNinty) {
		this();
		this.mirrorHorizontally = mirrorHorizontally;
		this.mirrorVertically = mirrorVertically;
		this.rotateByNinty = rotateByNinty;
	}

	/** Get the preferred size for this component. */
    public Dimension getPreferredSize(JComponent c) 
    {
    	// get parent dimension
    	Dimension dim = super.getPreferredSize(c);
    	
    	if (rotateByNinty) {
    		// flip height and width
    		return new Dimension( dim.height, dim.width );
    	} else {
    		return dim;
    	}
    }
    
    public boolean isMirrorHorizontally() {
		return mirrorHorizontally;
	}

	public void setMirrorHorizontally(boolean mirrorHorizontally) {
		this.mirrorHorizontally = mirrorHorizontally;
	}

	public boolean isMirrorVertically() {
		return mirrorVertically;
	}

	public void setMirrorVertically(boolean mirrorVertically) {
		this.mirrorVertically = mirrorVertically;
	}

	public boolean isRotateByNinty() {
		return rotateByNinty;
	}

	public void setRotateByNinty(boolean rotateByNinty) {
		this.rotateByNinty = rotateByNinty;
	}



	private static Rectangle paintIconR = new Rectangle();
    private static Rectangle paintTextR = new Rectangle();
    private static Rectangle paintViewR = new Rectangle();
    private static Insets paintViewInsets = new Insets(0, 0, 0, 0);

    /**
     * Paint the component
     */
	public void paint(Graphics g, JComponent c) 
    {    	
        JLabel label = (JLabel)c;
        String text = label.getText();
        Icon icon = (label.isEnabled()) ? label.getIcon() : label.getDisabledIcon();

        // return if nothing to paint
        if ((icon == null) && (text == null)) {
            return;
        }

        // fetch insets values for the label
        FontMetrics fm = SwingUtilities2.getFontMetrics(label, g);
        paintViewInsets = c.getInsets(paintViewInsets);
        paintViewR.x = paintViewInsets.left;
        paintViewR.y = paintViewInsets.top;
    	
    	// Use inverted height & width if we rotate, otherwise leave as is
        if (rotateByNinty) {
        	paintViewR.height = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        	paintViewR.width = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
        } else {
        	paintViewR.width = c.getWidth() - (paintViewInsets.left + paintViewInsets.right);
        	paintViewR.height = c.getHeight() - (paintViewInsets.top + paintViewInsets.bottom);
        }
        
        // initialize painting rectangles with zeros
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        String clippedText = 
            layoutCL(label, fm, text, icon, paintViewR, paintIconR, paintTextR);

        // fetch original transform
    	Graphics2D g2 = (Graphics2D) g;
    	AffineTransform tr = g2.getTransform();
    	
    	// add clockwise rotation
    	if (rotateByNinty) {
	    	g2.rotate( Math.PI / 2 ); 
    		g2.translate( 0, - c.getWidth() );
    	}

    	// add horizontal and vertical mirror if applicable
    	if (mirrorHorizontally) {
    		g2.scale(1.0, -1.0);
    		g2.translate(0, - c.getHeight());
    	}
    	if (mirrorVertically) {
    		g2.scale(-1.0, 1.0);
    		g2.translate(- c.getWidth(), 0);
    	}
    	
    	// paint icon
    	if (icon != null) {
            icon.paintIcon(c, g, paintIconR.x, paintIconR.y);
        }

    	// paint text
        if (text != null) {
            int textX = paintTextR.x;
            int textY = paintTextR.y + fm.getAscent();

            if (label.isEnabled()) {
                paintEnabledText(label, g, clippedText, textX, textY);
            }
            else {
                paintDisabledText(label, g, clippedText, textX, textY);
            }
        }
    	
    	// restore original transform
    	g2.setTransform( tr );
    }
	
    public static void main(String[] args) {
    	FixedSizeTable.main(args);
    }
}

