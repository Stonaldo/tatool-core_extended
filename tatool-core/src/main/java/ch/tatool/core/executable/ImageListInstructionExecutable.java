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
package ch.tatool.core.executable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.tatool.core.display.swing.panel.CenteredTextPanel;
import ch.tatool.element.Element;
import ch.tatool.element.Initializable;

/**
 * This executable displays a set of images as instructions.
 * The images need to be provided as complete (classpath) paths for each image.
 * 
 * @author Andre Locher
 */
public class ImageListInstructionExecutable extends ListInstructionExecutable implements Initializable {

	private Logger logger = LoggerFactory.getLogger(ImageListInstructionExecutable.class);
	
	/** Holds the paths to the images to display as instructions. */
	private List<String> images;

	/** Default constructor. */
	public ImageListInstructionExecutable() {
		super("image-instruction");
	}

	/**
	 * This method is called before the element is executed.
	 * We use it to initialize the panels 
	 */
    public void initialize(Element element) {
    	// load the images
    	loadImages();
    }

    /** Loads the images provided as classpath paths.
     * @see getImages, setImages 
     */
	private void loadImages() {
		List<JPanel> panels = new ArrayList<JPanel>();
		for (String path : images) {
			// fetch the image url
			if (logger.isDebugEnabled()) {
				logger.debug("Searching resource " + path);
			}
			URL imageURL = getClass().getResource(path);
			if (imageURL == null) {
				logger.warn("Resource file not found: " + path);
				continue;
			}
		
			// Create a panel displaying the image
			ImageIcon icon = new ImageIcon(imageURL);
			CenteredTextPanel panel = new CenteredTextPanel();
			panel.setIcon(icon);
			panels.add(panel);
		}

		// set the loaded panels to the ListInstructionsExecutable
		setPanels(panels);
	}
	
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
