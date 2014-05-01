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

/**
 * TaskDisplayUtils for rotated labels
 * 
 * @author Administrator
 */
public class RotatedLabelUtil {

	public static String getLabelValue(String text, boolean mirrorHorizontally, boolean mirrorVertically, boolean rotateByNinty) {	
		if (mirrorHorizontally) {
			text += "[H]";
		}
		if (mirrorVertically) {
			text += "[V]";
		}
		if (rotateByNinty) {
			text += "[R]";
		}
		return text;
	}
	
	public static String getLeftRightTextValue(String text) {
		return text += "[V]";
	}
}
