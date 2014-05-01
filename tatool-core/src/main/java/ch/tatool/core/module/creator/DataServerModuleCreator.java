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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import ch.tatool.data.Messages;
import ch.tatool.data.UserAccount;
import ch.tatool.module.ModuleCreator;
import ch.tatool.module.ModuleService;

/**
 * Module creator that checks on a data server for a module configuration file
 * given a generic code 
 * 
 * @author Michael Ruflin
 */
public class DataServerModuleCreator implements ModuleCreator {


	private static final int DOWNLOAD_TIMEOUT = 5000;
	
	private ModuleService moduleService;
	private String serverUrl = "";
	private JFrame parent;
	private Callback callback;
	private UserAccount userAccount;
	private ProgressDialog progressDialog;
	private Messages messages;

	private TatoolOnlineDialog dialog;

	public DataServerModuleCreator() {
		
	}
	
	public void executeCreator(JFrame parent, UserAccount userAccount, ModuleService moduleService, Callback callback) {
		this.parent = parent;
		this.callback = callback;
		this.userAccount = userAccount;
		this.moduleService = moduleService;
		
		dialog = new TatoolOnlineDialog(parent, true, getMessages());
		dialog.setCreator(this);
	    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    dialog.setVisible(true);
	}
	
	public void startLoading(final String[] data) {
		// run the rest in a SwingWorker thread, keeping the user updated by a progressdialog
		progressDialog = new ProgressDialog(parent, true);
		progressDialog.setLabel(messages.getString("General.progress.label.pleaseWait"));

		// call setVisible() from another thread, as this thread would get blocked otherwise
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialog.setVisible(true);
			}
		});
		
		// Do the work in a separate thread
		new Thread() {
			public void run() {
				loadModule(data);
			}
		}.start();
	}

	/** NOT running on the EDT. */
	private void loadModule(String[] data) {
		// update the information
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialog.setLabel(messages.getString("General.progress.label.loadingData"));
			}
		});
		
		// Load the xml file from tatool online
		final FileDownloadWorker worker = new FileDownloadWorker();
		worker.setMessages(messages);
		StringBuilder stb = new StringBuilder();
		stb.append(serverUrl);
		stb.append("/download.php");
		stb.append("?studyID=" + data[0]);
		stb.append("&moduleNr=" + data[1]);
		stb.append("&subjectCode=" + data[2]);
		
		// add codebase of currently executing Tatool version to request
		if (System.getProperty("codebase") != null) {
			try {
				stb.append("&codebase=" + URLEncoder.encode(System.getProperty("codebase"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		worker.loadFile(stb.toString(), DOWNLOAD_TIMEOUT);
		if (worker.getFile() == null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					errorFinish(worker); 
				}
			});
			return;
		}
		
		// Load the module data file from tatool online
		final FileDownloadWorker workerModule = new FileDownloadWorker();
		workerModule.setMessages(messages);
		StringBuilder stbModule = new StringBuilder();
		stbModule.append(serverUrl);
		stbModule.append("/get_module_data.php");
		stbModule.append("?studyID=" + data[0]);
		stbModule.append("&moduleNr=" + data[1]);
		stbModule.append("&subjectCode=" + data[2]);
		
		workerModule.loadFile(stbModule.toString(), DOWNLOAD_TIMEOUT);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressDialog.setLabel(messages.getString("General.progress.label.openingModule"));
			}
		});
		
		// create a worker to create the module
		final ModuleCreationWorker worker2 = new ModuleCreationWorker();
		worker2.setMessages(messages);
		worker2.createModule(moduleService, userAccount, worker.getFile(), workerModule.getFile(), null, true);
		
		// finish the creator
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (worker2.hasFinished()) {
					finish(worker2);
				} else {
					errorFinish(worker2);;
				}
			}
		});
	}
	
	/** Finish with an error. */
	private void errorFinish(Worker worker) {
		progressDialog.dispose();
		JOptionPane.showMessageDialog(parent, worker.getErrorText(), worker.getErrorTitle(), JOptionPane.ERROR_MESSAGE);
		callback.closeDialog(null);
		return;
	}
	
	private void finish(ModuleCreationWorker worker) {
		progressDialog.dispose();
		// close the creator, passing over the created module
		callback.closeDialog(worker.getModule());
	}
	
	public void hideCreator() {
		dialog.setVisible(false);
		dialog.dispose();
	}

	public String getCreatorId() {
		return "data-server-module-creator";
	}

	public String getCreatorName() {
		return messages.getString("General.creator.dataServerCreator.title");
	}
	
	// Getters / setters

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	
	public void setMessages(Messages messages) {
    	this.messages = messages;
    }
    
    public Messages getMessages() {
		return messages;
	}
}
