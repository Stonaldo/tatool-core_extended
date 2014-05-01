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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 * Provides methods to set width and height of a table.
 * 
 * @author Michael Ruflin
 */
public class FixedSizeTable extends JPanel {

	private static final long serialVersionUID = 8592868130625954697L;
	private int rowHeight = 30;
	private int columnWidth = 30;
	
	private JTable table;
	
	public FixedSizeTable() {
		initComponents();
	}

	private void initComponents() {
		setLayout(new GridBagLayout());
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(rowHeight);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		updateColumnWidths();
		GridBagConstraints gbc = new GridBagConstraints();
		add(table, gbc);

		setBorder(new LineBorder(Color.BLACK, 1));
	}
	
	private void updateColumnWidths() {
		int columnCount = table.getColumnCount();
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(columnWidth);
		}
	}
	
	/** Set the table model underlying the matrix. */
	public void setTableModel(TableModel dataModel) {
		table.setModel(dataModel);
		table.setRowHeight(rowHeight);
		updateColumnWidths();
	}
	
	public void setTableCellRenderer(TableCellRenderer cellRenderer) {
		table.setDefaultRenderer(Object.class, cellRenderer);
	}
	
    public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
		table.setRowHeight(rowHeight);
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
		updateColumnWidths();
	}

	public JTable getTable() {
		return table;
	}

	public static void main(String[] args) {
        JFrame frame = new JFrame("Matrix test");
        FixedSizeTable matrix = new FixedSizeTable();
        
        DefaultTableModel model = new DefaultTableModel(10, 10);
        model.setValueAt("A", 0, 0);
        model.setValueAt("A[N]", 0, 1);
        model.setValueAt("A[R]", 0, 2);
        model.setValueAt("B[H]", 1, 0);
        model.setValueAt("B[H][V]", 1, 1);
        model.setValueAt("B[H][V][R]", 1, 2);
        model.setValueAt("C[V]", 2, 0);
        model.setValueAt("C[V][R]", 2, 1);
        model.setValueAt("C[R]", 2, 2);
        matrix.setTableModel(model);
        
        RotatedLabelTableCellRenderer rotatedLabelTableCellRenderer = new RotatedLabelTableCellRenderer();
        rotatedLabelTableCellRenderer.setLabelFont(rotatedLabelTableCellRenderer.getLabelFont().deriveFont(rotatedLabelTableCellRenderer.getLabelFont().getSize()+6f));
        matrix.setTableCellRenderer(rotatedLabelTableCellRenderer);
        
        matrix.setRowHeight(40);
        matrix.setColumnWidth(40);
        
        //matrix.setBorder(new LineBorder(Color.green, 5));
        JPanel root = new JPanel(new BorderLayout());
        frame.getContentPane().add(root);
        root.add(matrix);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
