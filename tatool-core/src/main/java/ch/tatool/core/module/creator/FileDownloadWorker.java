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
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import ch.tatool.data.Messages;

public class FileDownloadWorker implements Worker {
	private File file;
	private String errorTitle;
	private String errorText;
	private Messages messages;
	
	/** Loads the module file from the data server using the provided code. */
	public void loadFile(String url, int timeout) {
		// give it a timeout to ensure the user does not wait forever in case of connectivity problems
		HttpParams params = new BasicHttpParams();
		if (timeout > 0) {
			HttpConnectionParams.setConnectionTimeout(params, timeout);
			HttpConnectionParams.setSoTimeout(params, timeout);
		}
		
		// remove whitespaces since they are not allowed
		url = url.replaceAll("\\s+", "").trim();
		
		// create a http client
		DefaultHttpClient httpclient = new DefaultHttpClient(params);
		HttpGet httpGet = new HttpGet(url);

		errorTitle = null;
		errorText = null;
		file = null;
		try {
			HttpResponse response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				errorTitle = messages.getString("General.errorMessage.windowTitle.error");
				errorText  = messages.getString("DataExportError.online.http");
				errorText += "\n" + response.getStatusLine().getReasonPhrase()+ " (" + response.getStatusLine().getStatusCode() + ")";
			} else {
				// copy the response into a temporary file
				HttpEntity entity = response.getEntity();
				byte[] data = EntityUtils.toByteArray(entity);
				String returnString = new String(data);
				if (!returnString.startsWith("General")) {
					File tmpFile = File.createTempFile("tatool_module", "tmp");
					FileUtils.writeByteArrayToFile(tmpFile, data);
					tmpFile.deleteOnExit();
					this.file = tmpFile;
				} else {
					errorTitle = messages.getString("General.errorMessage.windowTitle.error");
					errorText = messages.getString(returnString);
				}
			}
		} catch (IOException ioe) {
			errorTitle = messages.getString("General.errorMessage.windowTitle.error");
			errorText = messages.getString("DataExportError.online.http");
		} finally {
			// make sure we close the connection manager
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	public boolean hasFinished() {
		return file != null;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	
	public void setMessages(Messages messages) {
    	this.messages = messages;
    }
    
    public Messages getMessages() {
		return messages;
	}
}
