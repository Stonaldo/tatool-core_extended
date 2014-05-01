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
package ch.tatool.core.module.creator;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import ch.tatool.data.Messages;
import ch.tatool.data.Module;
import ch.tatool.data.UserAccount;
import ch.tatool.module.ModuleCreator;
import ch.tatool.module.ModuleService;

public class LocalFileModuleCreator implements ModuleCreator {

    /** Last folder opened during module opening. */
    public String PROPERTY_ACCOUNT_LAST_OPENED_FOLDER = "LastOpenedFolder";
	
	private ModuleService moduleService;
	private JFrame parent;
	private Callback callback;
	private UserAccount userAccount;
	private ProgressDialog progressDialog;

	private Messages messages;
	
	public void executeCreator(JFrame parent, UserAccount userAccount, ModuleService moduleService, Callback callback) {
		this.parent = parent;
		this.callback = callback;
		this.userAccount = userAccount;
		this.moduleService = moduleService;
		
		// fetch the module file
		final File file = getModuleFile();
		if (file == null) {
			callback.closeDialog(null);
			return;
		}
		
		// do the rest in a separate thread
		progressDialog = new ProgressDialog(parent, true);
		progressDialog.setLabel(messages.getString("General.progress.label.openingModule"));
		
		// call setVisible() from another thread, as this thread would get blocked otherwise
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialog.setVisible(true);
			}
		});
		
		// Do the work in a separate thread
		new Thread() {
			public void run() {
				load(file);
			}
		}.start();
	}
	
	// local thread
	private void load(File file) {
		// create a worker to create the module
		final ModuleCreationWorker worker = new ModuleCreationWorker();
		worker.createModule(moduleService, userAccount, file, null, null, false);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				finish(worker); 
			}
		});
	}
	
	// EDT thread
	private void finish(ModuleCreationWorker worker) {
		// hide the progress bar
		progressDialog.setVisible(false);
		
		// check whether we got a module
		Module t = worker.getModule();
		if (t == null) {
			JOptionPane.showMessageDialog(parent, worker.getErrorText(), worker.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
		}

		// close the creator
		callback.closeDialog(t);
	}
	
	private File getModuleFile() {
		File folder;
		// get last opened folder
		String lastOpenedFolder = userAccount.getProperties().get(PROPERTY_ACCOUNT_LAST_OPENED_FOLDER);
		if (lastOpenedFolder != null) {
			folder = new File(lastOpenedFolder);
		} else {
			folder = null;
		}
		JFileChooser chooser = new JFileChooser(folder);
		int result = chooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION) {
			// set last opened folder
			userAccount.getProperties().put(PROPERTY_ACCOUNT_LAST_OPENED_FOLDER, chooser.getSelectedFile().getAbsolutePath());
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	public String getCreatorId() {
		return "local-file-module-creator";
	}

	public String getCreatorName() {
		return messages.getString("General.creator.localFileCreator.title");
	}
	
	public void hideCreator() {
		// nothing to do
	}
	
	public void setMessages(Messages messages) {
    	this.messages = messages;
    }
    
    public Messages getMessages() {
		return messages;
	}
}
