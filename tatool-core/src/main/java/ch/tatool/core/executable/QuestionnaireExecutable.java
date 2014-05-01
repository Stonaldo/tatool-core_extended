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

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import ch.tatool.core.data.DataUtils;
import ch.tatool.core.data.Level;
import ch.tatool.core.data.Misc;
import ch.tatool.core.data.Points;
import ch.tatool.core.data.Question;
import ch.tatool.core.data.Result;
import ch.tatool.core.data.Timing;
import ch.tatool.core.display.swing.ExecutionDisplayUtils;
import ch.tatool.core.display.swing.SwingExecutionDisplay;
import ch.tatool.core.display.swing.action.ActionPanel;
import ch.tatool.core.display.swing.action.ActionPanelListener;
import ch.tatool.core.display.swing.action.InputActionPanel;
import ch.tatool.core.display.swing.action.KeyActionPanel;
import ch.tatool.core.display.swing.container.ContainerUtils;
import ch.tatool.core.display.swing.container.RegionsContainer;
import ch.tatool.core.display.swing.container.RegionsContainer.Region;
import ch.tatool.core.display.swing.panel.CenteredTextPanel;
import ch.tatool.core.element.handler.timeout.DefaultTimeoutHandler;
import ch.tatool.data.DescriptivePropertyHolder;
import ch.tatool.data.Property;
import ch.tatool.data.Trial;
import ch.tatool.exec.ExecutionContext;
import ch.tatool.exec.ExecutionOutcome;
import ch.tatool.exec.ExecutionPhaseListener;

/**
 * Questionnaire
 * 
 * Displays questions and allows the user to answer.
 * Make sure you take a look at the questionnaire_stimuli_template.csv to see which columns are available.
 * 
 * @author Andre Locher
 */
public class QuestionnaireExecutable extends BlockingAWTExecutable implements
		ActionPanelListener, ExecutionPhaseListener, DescriptivePropertyHolder{

	Logger logger = LoggerFactory.getLogger(QuestionnaireExecutable.class);

	private DefaultTimeoutHandler timeoutHandler;

	private RegionsContainer regionsContainer;
	private SwingExecutionDisplay display;

	public String stimuliFile = "questionnaire_stimuli.csv";
	public String stimuliPath = "/ch/tatool/data/questionnaire/";

	private List<String[]> stimuliData = new ArrayList<String[]>();

	/** Question panel. */
	private CenteredTextPanel imgPanel;
	private KeyActionPanel keyActionPanel;
	private InputActionPanel inputActionPanel;

	/** Stimuli. */
	private String[] stimulusInfo;
	private String item;
	private String correctResponse;
	private String givenResponse;
	private Trial currentTrial;
	private Map<Integer,String> trialResponses;

	/** Default Constructor. */
	public QuestionnaireExecutable() {
		super("questionnaire-task");
		trialResponses = new HashMap<Integer,String>();
		initComponents();
	}

	private void initComponents() {
		// question panel
		imgPanel = new CenteredTextPanel();
	}

	/**
	 * Setup the action panel according to the type needed
	 * 
	 * @param format
	 *            dichot: dichotomous yes/no format 
	 *            open: open format
	 *            proportional: scale format
	 */
	private void setupActionPanel(String format, int scale) {

		if (format.equals("dichot")) {
			initBinaryScale();
			regionsContainer.setRegionContent(Region.SOUTH, keyActionPanel);
		} else if (format.equals("open")) {
			initOpenFormat();
			regionsContainer.setRegionContent(Region.SOUTH, inputActionPanel);
		} else if (format.equals("scale")) {
			initRatingScale(scale);
			regionsContainer.setRegionContent(Region.SOUTH, keyActionPanel);
		} else if (format.equals("none")) {
			initNone();
			regionsContainer.setRegionContent(Region.SOUTH, keyActionPanel);
		}
	}

	private void initOpenFormat() {
		inputActionPanel = new InputActionPanel();
		inputActionPanel.setTextDocument(250, InputActionPanel.FORMAT_ALL);
		inputActionPanel.addActionPanelListener(this);
	}

	private void initBinaryScale() {
		keyActionPanel = new KeyActionPanel();
		keyActionPanel.addKey(KeyEvent.VK_A, stimulusInfo[7], stimulusInfo[8]);
		keyActionPanel.addKey(KeyEvent.VK_L, stimulusInfo[9], stimulusInfo[10]);
		keyActionPanel.addActionPanelListener(this);
	}

	private void initNone() {
		keyActionPanel = new KeyActionPanel();
		keyActionPanel.addKey(KeyEvent.VK_RIGHT, "Weiter", "");
		keyActionPanel.addActionPanelListener(this);
	}

	private void initRatingScale(int scale) {
		keyActionPanel = new KeyActionPanel();

		for (int i = 0; i < (scale * 2); i = i + 2) {
			int j = 7 + i;
			keyActionPanel.addKey(translateToKeyEvent(Integer
					.parseInt(stimulusInfo[j])), "", stimulusInfo[j + 1]);
		}

		keyActionPanel.addActionPanelListener(this);
	}

	private int translateToKeyEvent(int key) {
		switch (key) {
		case 0:
			return KeyEvent.VK_0;
		case 1:
			return KeyEvent.VK_1;
		case 2:
			return KeyEvent.VK_2;
		case 3:
			return KeyEvent.VK_3;
		case 4:
			return KeyEvent.VK_4;
		case 5:
			return KeyEvent.VK_5;
		case 6:
			return KeyEvent.VK_6;
		case 7:
			return KeyEvent.VK_7;
		case 8:
			return KeyEvent.VK_8;
		case 9:
			return KeyEvent.VK_9;
		default:
			return KeyEvent.VK_0;
		}
	}

	public void startExecutionAWT() {
		regionsContainer = ContainerUtils.getRegionsContainer();
		display = ExecutionDisplayUtils.getDisplay(getExecutionContext());
		ContainerUtils.showRegionsContainer(display);
		ExecutionContext context = getExecutionContext();
		
		// get current stimulus
		getCurrentStimulus();
		
		// check for question dependency
		if (!stimulusInfo[1].equals("")) {
			int questionID = Integer.parseInt(stimulusInfo[1]);
			String oldResponse = trialResponses.get(questionID);
			if (oldResponse == null || !oldResponse.equals(stimulusInfo[2])) {
				// skip this question
				if (getFinishExecutionLock()) {
					cancelExecutionAWT();
					finishExecution();
					return;
				}
			}
		}

		// create trial
		currentTrial = context.getExecutionData().getCreateLastTrial();
		currentTrial.setParentId(getId());
		Timing.getStartTimeProperty().setValue(this, new Date());


		item = stimulusInfo[3];
		URL iconURL = getClass()
				.getResource(stimuliPath + item);
		ImageIcon icon = new ImageIcon(iconURL);
		correctResponse = "";
		givenResponse = null;

		// set some properties
		Question.setQuestionAnswer(this, stimulusInfo[4], correctResponse);
		Points.setZeroOneMinMaxPoints(this);

		// setup the views
		regionsContainer.removeRegionContent(Region.CENTER);
		regionsContainer.removeRegionContent(Region.SOUTH);
		imgPanel.setIcon(icon);
		regionsContainer.setRegionContent(Region.CENTER, imgPanel);
		setupActionPanel(stimulusInfo[5], Integer.parseInt(stimulusInfo[6]));
		regionsContainer.setRegionContentVisibility(Region.CENTER, true);

		// enable the actions
		if (stimulusInfo[5].equals("open")) {
			inputActionPanel.enableActionPanel();
		} else {
			keyActionPanel.enableActionPanel();
		}

		// start timer
		if (timeoutHandler != null) {
			timeoutHandler.startTimeout(context);
		}
	}

	private void getCurrentStimulus() {
		// read stimuli list if none available in the list
		if (stimuliData.size() > 0) {
			stimulusInfo = stimuliData.get(0);
			stimuliData.remove(0);
		} else {
			readStimuli();
			stimulusInfo = stimuliData.get(0);
			stimuliData.remove(0);
		}
	}

	public void readStimuli() {
			// read stimuli info
		stimuliData.clear();

		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(this.getClass()
					.getResourceAsStream(stimuliPath + stimuliFile),
					"ISO-8859-1"), ';','"',1);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			stimuliData = reader.readAll();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * Cleanup after element execution
	 */
	@Override
	protected void cancelExecutionAWT() {
		// disable actions
		RegionsContainer regionsContainer = ContainerUtils.getRegionsContainer();
		regionsContainer.removeRegionContent(Region.CENTER);
		regionsContainer.removeRegionContent(Region.SOUTH);
		if (timeoutHandler != null) {
			timeoutHandler.cancelTimeout();
		}
		if (inputActionPanel != null) {
			inputActionPanel.disableActionPanel();
		}
		if (keyActionPanel != null) {
			keyActionPanel.disableActionPanel();
		}
	}

	/** Called when either of the two action panel choices have been selected. */
	public void actionTriggered(ActionPanel source, Object actionValue) {
		givenResponse = (String) actionValue;

		// allow only 255 characters
		if (givenResponse.length() > 255) {
			givenResponse = givenResponse.substring(0, 255);
		}
		
		if (inputActionPanel != null) {
			inputActionPanel.disableActionPanel();
		}
		if (keyActionPanel != null) {
			keyActionPanel.disableActionPanel();
		}
		
		Question.getResponseProperty().setValue(this, givenResponse);
		trialResponses.put(Integer.parseInt(stimulusInfo[0]),givenResponse);
		
		// set the final properties
		if (Integer.parseInt(stimulusInfo[6]) > 0) {
			Points.getPointsProperty().setValue(this,
					Integer.parseInt(givenResponse));
		} else {
			Points.getPointsProperty().setValue(this, 0);
		}

		Result.getResultProperty().setValue(this, true);

		Timing.getEndTimeProperty().setValue(this, new Date());
		long duration = 0;
        if (Timing.getStartTimeProperty().getValue(this) != null && Timing.getEndTimeProperty().getValue(this) != null) {
        	duration = Timing.getEndTimeProperty().getValue(this).getTime() - Timing.getStartTimeProperty().getValue(this).getTime();
        }
        Timing.getDurationTimeProperty().setValue(this, duration);
        Misc.getOutcomeProperty().setValue(this, ExecutionOutcome.FINISHED);
        Misc.getOutcomeProperty().setValue(getExecutionContext(), ExecutionOutcome.FINISHED);
        DataUtils.storeProperties(currentTrial, this);

		if (getFinishExecutionLock()) {
			cancelExecutionAWT();
			finishExecution();
		}
	}
	
	public Property<?>[] getPropertyObjects() {
		return new Property[] { Level.getLevelProperty(),
				Points.getMinPointsProperty(), 
				Points.getPointsProperty(),
				Points.getMaxPointsProperty(), 
				Question.getQuestionProperty(),
				Question.getAnswerProperty(), 
				Question.getResponseProperty(),
				Misc.getOutcomeProperty(),
				Result.getResultProperty(),
				Timing.getStartTimeProperty(),
				Timing.getEndTimeProperty(),
				Timing.getDurationTimeProperty()};
	}

	public void processExecutionPhase(ExecutionContext context) {
		switch (context.getPhase()) {
		case SESSION_START:
			trialResponses.clear();
			break;
		default:
			break;
		}
	}

	public DefaultTimeoutHandler getTimeoutAspect() {
		return timeoutHandler;
	}

	public void setTimeoutAspect(DefaultTimeoutHandler timer) {
		this.timeoutHandler = timer;
		this.timeoutHandler.setParent(this);
		this.timeoutHandler.setId("timer");
	}

	public String getStimuliFile() {
		return stimuliFile;
	}

	public void setStimuliFile(String stimuliFile) {
		this.stimuliFile = stimuliFile;
	}

	public String getStimuliPath() {
		return stimuliPath;
	}

	public void setStimuliPath(String stimuliPath) {
		this.stimuliPath = stimuliPath;
	}

}
