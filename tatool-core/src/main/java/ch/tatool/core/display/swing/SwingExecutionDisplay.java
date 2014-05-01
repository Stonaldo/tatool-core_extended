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
package ch.tatool.core.display.swing;

import java.awt.Color;
import java.awt.Component;

import ch.tatool.display.ExecutionDisplay;


/**
 * Swing container provided to elements to interact with the user
 * 
 * Components can be added to the Display in a "Card style view" manner, where each component
 * is displayed in full at once.
 * 
 * In addition to the displayed card, an overlay can be added to the display, which will be displayed
 * above the displayed card.  
 * 
 * Note: All methods in this interface need to be called from the AWT Event Dispatch thread only.
 * 
 * @author Michael Ruflin
 */
public interface SwingExecutionDisplay extends ExecutionDisplay {
	
	/** ID of the empty card. */
	public static final String EMPTY_CARD_ID = "emptyCard";
	
	/**
	 * Add a card to this display
	 */
	public void addCard(String cardId, Component component);
	
	/** Display a previously added card. */
	public void showCard(String cardId);
	
	/** Removes a card from the display. */
	public void removeCard(String cardId);
	
	/** Remove all added cards. */
	public void removeAllCards();
	
	/** Display an empty card. */
	public void displayEmptyCard();
	
	/** Get the currently displayed card. */
	public String getDisplayedCardId();
	
	/** Set the overlay component to be displayed.
	 * @param component the overlay component to display, null to remove any displayed component
	 */
	public void setOverlay(Component component);
	
    /**
     * Get the background color of the display.
     * @return the background color, with white being default
     */
    public Color getBackgroundColor();
    
    /**
     * Set the background color of the display.
     * @param color the background color to set.
     */
    public void setBackgroundColor(Color color);
}
