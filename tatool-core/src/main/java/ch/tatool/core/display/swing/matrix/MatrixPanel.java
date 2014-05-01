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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import ch.tatool.core.display.swing.action.AbstractActionPanel;

/**
 * Simple matrix panel implementation
 * 
 * This class acts as view and action panel.
 * 
 * @author Michael Ruflin
 */
public class MatrixPanel extends AbstractActionPanel {

	private static final long serialVersionUID = -3436219123680641651L;
	
	private FixedSizeTable fixedSizeTable;
	private DefaultTableModel dataModel;
	private TableMouseListener tableMouseListener;
	
	public MatrixPanel() {
		initComponents();
	}
	
	private void initComponents() {
		// initialize table and model
		fixedSizeTable = new FixedSizeTable();
		dataModel = new NonEditableTableModel();
		
		fixedSizeTable.getTable().setModel(dataModel);
		fixedSizeTable.getTable().setEnabled(false);
	
		// initialize selection listener
		tableMouseListener = new TableMouseListener();
		
		// add it to the panel so that it is centered left right and top down
		setOpaque(false);
		setLayout(new GridBagLayout());
		add(fixedSizeTable, new GridBagConstraints());
	}
	
	// Matrix configuration methods
	
	public void setMatrixDimensions(int rowCount, int columnCount) {
		dataModel.setRowCount(rowCount);
		dataModel.setColumnCount(columnCount);
	}
	
	public void setCellDimensions(int width, int height) {
		fixedSizeTable.setColumnWidth(width);
		fixedSizeTable.setRowHeight(height);
	}
	
	public void setCellRenderer(TableCellRenderer cellRenderer) {
		fixedSizeTable.setTableCellRenderer(cellRenderer);
	}
	
	public void setValueAt(Object value, int row, int column) {
		dataModel.setValueAt(value, row, column);
	}
	
	public void setValue(MatrixValue matrixValue) {
		setValueAt(matrixValue.value, matrixValue.row, matrixValue.column);
	}
	
	public void removeValue(MatrixValue matrixValue) {
	    setValueAt(null, matrixValue.row, matrixValue.column);
	}
	
	public void clearMatrix() {
		for (int y=0, yM=dataModel.getRowCount(); y < yM; y++) {
			for (int x=0, xM=dataModel.getColumnCount(); x < xM; x++) {
				dataModel.setValueAt(null, y, x);
			}
		}
	}
	
	
	// ActionPanel functionality
	
	public void enableActionPanel() {
		/*
		fixedSizeTable.getTable().getSelectionModel().clearSelection();
		fixedSizeTable.getTable().getSelectionModel().addListSelectionListener(tableSelectionListener);
		fixedSizeTable.getTable().setEnabled(true);
		*/
	    fixedSizeTable.getTable().addMouseListener(tableMouseListener);
	}
	
	public void disableActionPanel() {
		/*fixedSizeTable.getTable().getSelectionModel().removeListSelectionListener(tableSelectionListener);
		fixedSizeTable.getTable().setEnabled(false);*/
	    fixedSizeTable.getTable().removeMouseListener(tableMouseListener);
	}

	// Some getters

	public FixedSizeTable getFixedSizeTable() {
		return fixedSizeTable;
	}

	public DefaultTableModel getDataModel() {
		return dataModel;
	}
	
	
	class TableMouseListener extends MouseAdapter {

	    private Point pressedPoint;
	    
        @Override
        public void mousePressed(MouseEvent e) {
            pressedPoint = e.getPoint();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point releasedPoint = e.getPoint();

            // fire if difference less than 3 pixels
            if (pressedPoint != null && pressedPoint.distance(releasedPoint) < 3.0f) {
                triggerAtPoint(pressedPoint, e.getButton());
            }
            
            // remove pressedPoint
            pressedPoint = null;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //triggerAtPoint(e.getPoint());
        }

        private void triggerAtPoint(Point point, int button) {
            // find the cell that got clicked
            JTable table = fixedSizeTable.getTable();
            int row = table.rowAtPoint(point);
            int column = table.columnAtPoint(point);
            if (row > -1 && column > -1) {
                // build up a matrix value object we then trigger
                MatrixValue matrixValue = new MatrixValue();
                matrixValue.row = row;
                matrixValue.column = column;
                matrixValue.value = table.getValueAt(row, column);
                matrixValue.action = button;
                fireActionTriggered(matrixValue);
            }
        }
	}
	
	/**
	 * Table selection listener that fires actionTriggered events for table cell selections
	 */
	class TableSelectionListener implements ListSelectionListener {
		
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) return;
			
			// find the selected index
			JTable table = fixedSizeTable.getTable();
			int row = table.getSelectedRow();
			int column = table.getSelectedColumn();
			if (row > -1 && column > -1) {
				// build up a matrix value object we then trigger
				MatrixValue matrixValue = new MatrixValue();
				matrixValue.row = row;
				matrixValue.column = column;
				matrixValue.value = table.getValueAt(row, column);
				fireActionTriggered(matrixValue);
			}
		}
	}
	
	/**
	 * DefaultTableModel that does not allow cell editing
	 */
	public static class NonEditableTableModel extends DefaultTableModel {

		private static final long serialVersionUID = -4639223081795695018L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}
}
