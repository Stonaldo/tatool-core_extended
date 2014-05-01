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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encapsulates a value and the associated location in the matrix.
 * 
 * @author Michael Ruflin
 */
public class MatrixValue {
	public int row, column;
	public Object value;
	public int action; // add or remove
	
	public String toString() {
		return "" + row + "," + column + ":" + value;
	}
	
	public MatrixValue copy() {
	    MatrixValue mv = new MatrixValue();
	    mv.row = this.row;
	    mv.column = this.column;
	    mv.value = this.value;
	    return mv;
	}
	
	public boolean comparePositionTo(MatrixValue other) {
	    return this.row == other.row &&
	           this.column == other.column; 
	}
	
    /** Converts a string into a matrix value. */
    public static MatrixValue fromString(String string) {
        Pattern pattern = Pattern.compile("(\\d+)\\,(\\d+)\\:(.*)");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            MatrixValue value = new MatrixValue();
            value.row = Integer.parseInt(matcher.group(1));
            value.column = Integer.parseInt(matcher.group(2));
            value.value = matcher.group(3);
            return value;
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println(MatrixValue.fromString("10,20:test"));
    }
}
