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
package ch.tatool.core.display.swing.action;

import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The KeyActionPanel allows to define the actions that get triggered when keys are pressed.
 *
 * @author Andre Locher
 */
public class KeyActionPanel extends AbstractActionPanel {

	private static final long serialVersionUID = -7997375035513018220L;

	private static final String IMAGE_LOCATION = "/ch/tatool/core/ui/general/";
	private static final String KEY = IMAGE_LOCATION + "key.png";
	private static final String KEY_SPACE = IMAGE_LOCATION + "key_space.png";
	private static final String KEY_UP = IMAGE_LOCATION + "key_arrow_U.png";
	private static final String KEY_DOWN = IMAGE_LOCATION + "key_arrow_D.png";
	private static final String KEY_LEFT = IMAGE_LOCATION + "key_arrow_L.png";
	private static final String KEY_RIGHT = IMAGE_LOCATION + "key_arrow_R.png";
	
	private SelectChoiceKeyEventDispatcher keyEventDispatcher;
    private List<ActionKey> actionKeys;
    private GridLayout gridLayout;

    /** Creates new form KeyActionPanel */
    public KeyActionPanel() {
        initComponents();
        gridLayout = new GridLayout();
        gridLayout.setHgap(15);
        setLayout(gridLayout);

        keyEventDispatcher = new SelectChoiceKeyEventDispatcher();
        actionKeys = new ArrayList<ActionKey>();
    }

    /**
     * @param key The key to use
     * @param label The description next to the key
     * @param actionValue The actionValue to be passed by the key
     */
    public void addKey(int key, String label, Object actionValue) {
    	for (int i = 0; i<actionKeys.size(); i++) {
    		if (actionKeys.get(i).key == key) {
    			ActionKey k = actionKeys.get(i);
    			this.remove(k.keyPanel);
    			actionKeys.remove(i);
    		}
    	}
        JPanel keyPanel = addKeyPanel(key, label);
    	actionKeys.add(new ActionKey(key, label, actionValue, keyPanel));
    	this.add(keyPanel);
    }

	private JPanel addKeyPanel(int key, String label) {
		JPanel keyPanel = new JPanel();

        ImageIcon icon = null;
        JLabel keyCode = new JLabel();
        
        switch (key) {
        case KeyEvent.VK_UP:
        	icon = new ImageIcon(getClass().getResource(KEY_UP));
        	break;
        case KeyEvent.VK_DOWN:
        	icon = new ImageIcon(getClass().getResource(KEY_DOWN));
        	break;
        case KeyEvent.VK_LEFT:
        	icon = new ImageIcon(getClass().getResource(KEY_LEFT));
        	break;
        case KeyEvent.VK_RIGHT:
        	icon = new ImageIcon(getClass().getResource(KEY_RIGHT));
        	break;
        case KeyEvent.VK_SPACE:
        	icon = new ImageIcon(getClass().getResource(KEY_SPACE));
        	break;
        case KeyEvent.VK_NUMPAD0:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("0");
        	break;
        case KeyEvent.VK_NUMPAD1:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("1");
        	break;
        case KeyEvent.VK_NUMPAD2:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("2");
        	break;
        case KeyEvent.VK_NUMPAD3:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("3");
        	break;
        case KeyEvent.VK_NUMPAD4:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("4");
        	break;
        case KeyEvent.VK_NUMPAD5:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("5");
        	break;
        case KeyEvent.VK_NUMPAD6:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("6");
        	break;
        case KeyEvent.VK_NUMPAD7:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("7");
        	break;
        case KeyEvent.VK_NUMPAD8:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("8");
        	break;
        case KeyEvent.VK_NUMPAD9:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText("9");
        	break;
        default:
        	icon = new ImageIcon(getClass().getResource(KEY));
        	keyCode.setText(KeyEvent.getKeyText(key));
        	break;
        }
        
        keyCode.setIcon(icon);
        keyCode.setFont(keyCode.getFont().deriveFont(keyCode.getFont().getSize()+22f));
        keyCode.setVerticalTextPosition(JLabel.CENTER);
        keyCode.setHorizontalTextPosition(JLabel.CENTER);
        keyCode.setOpaque(false);
        
        JLabel keyLabel = new JLabel();
        keyLabel.setText(label);
        keyLabel.setFont(keyLabel.getFont().deriveFont(keyLabel.getFont().getSize()+22f));
        keyLabel.setOpaque(false);
        
        keyPanel.add(keyCode);
        keyPanel.add(keyLabel);
        keyPanel.setOpaque(false);
        return keyPanel;
	}

    public void removeKeys() {
        actionKeys.clear();
        this.removeAll();
    }

    /** Call this method when starting the task. */
    public void enableActionPanel() {
        super.enableActionPanel();
        // register the key dispatcher
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    /** Call this method when stopping the task. */
    public void disableActionPanel() {
        super.disableActionPanel();
        // unregister the key dispatcher
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
    }

    /**
     * ActionKey
     */
    class ActionKey {
        public int key;
        public String label;
        public Object actionValue;
        public JPanel keyPanel;

        public ActionKey(int key, String label, Object actionValue, JPanel keyPanel) {
            this.key = key;
            this.label = label;
            this.actionValue = actionValue;
            this.keyPanel = keyPanel;
        }
    }

    /**
     * KeyListener that links some of the keyboard keys.
     */
    class SelectChoiceKeyEventDispatcher implements KeyEventDispatcher {
/*
        /** Called when a key has been typed. */
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                for (int i = 0; i < actionKeys.size(); i++) {
                    if (e.getKeyCode() == actionKeys.get(i).key) {
                        fireActionTriggered(actionKeys.get(i).actionValue);
                        break;
                    }
                }
            }
            // never consume the event...
            return false;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(246, 246, 246));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setMinimumSize(new java.awt.Dimension(400, 76));
        setOpaque(true);
        setPreferredSize(new java.awt.Dimension(400, 76));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 76, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
