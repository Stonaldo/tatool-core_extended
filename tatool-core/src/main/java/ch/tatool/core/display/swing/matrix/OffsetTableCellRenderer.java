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

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell renderer that...
 */
public class OffsetTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -4661685482832800216L;

	private Font labelFont;

	private static final String IMAGE_LOCATION = "/ch/tatool/core/ui/general/";
	private static final String ARROW_UP = IMAGE_LOCATION + "arrow_U.png";
	private static final String ARROW_DOWN = IMAGE_LOCATION + "arrow_D.png";
	private static final String ARROW_LEFT = IMAGE_LOCATION + "arrow_L.png";
	private static final String ARROW_RIGHT = IMAGE_LOCATION + "arrow_R.png";

	public OffsetTableCellRenderer() {
		super();

		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);

		labelFont = getFont();

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
			boolean isSelected, boolean hasFocus, int row, int column) {

		String iconURL = "";
		ImageIcon icon = null;
		JLabel label = new JLabel();
		String v = null;

		if (value != null) {
			v = value.toString();
			while (v.endsWith("]")) {
				if (v.endsWith("[R]")) {
					v = v.substring(0, v.length() - 3);
					iconURL = ARROW_RIGHT;
					label.setVerticalTextPosition(JLabel.CENTER);
					label.setHorizontalTextPosition(JLabel.LEFT);
				} else if (v.endsWith("[L]")) {
					v = v.substring(0, v.length() - 3);
					iconURL = ARROW_LEFT;
					label.setVerticalTextPosition(JLabel.CENTER);
					label.setHorizontalTextPosition(JLabel.RIGHT);
				} else if (v.endsWith("[U]")) {
					v = v.substring(0, v.length() - 3);
					iconURL = ARROW_UP;
					label.setVerticalTextPosition(JLabel.BOTTOM);
					label.setHorizontalTextPosition(JLabel.CENTER);
				} else if (v.endsWith("[D]")) {
					v = v.substring(0, v.length() - 3);
					iconURL = ARROW_DOWN;
					label.setVerticalTextPosition(JLabel.TOP);
					label.setHorizontalTextPosition(JLabel.CENTER);
				} else {
					v = v.substring(1, v.length() - 1);
				}
			}
			if (!iconURL.isEmpty()) {
				icon = new ImageIcon(getClass().getResource(iconURL));
			}
		}

		label.setIcon(icon);
		
		if (v != null) {
			label.setBackground(new java.awt.Color(102, 102, 102));
			label.setForeground(new java.awt.Color(255, 255, 255));
			label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			label.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
			label.setFont(labelFont);
			label.setOpaque(true);
			label.setText(v);
		}
		return label;
	}

	public static void main(String[] args) {
		FixedSizeTable.main(args);
	}

}
