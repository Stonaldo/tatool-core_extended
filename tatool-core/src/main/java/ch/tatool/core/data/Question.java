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
package ch.tatool.core.data;

import ch.tatool.element.Node;

/**
 * Helper Class with getter and setter methods for properties concerning trial questions and answers.
 * 
 * @author Michael Ruflin
 */
public class Question {
	
	/** Property name used to set the question value. */
	public static final String PROPERTY_QUESTION = "question";
	
	/** Property name used to set the answer value. */
	public static final String PROPERTY_ANSWER = "answer";
	
	/** Property name used to set the response value. */
	public static final String PROPERTY_RESPONSE = "response";
	
	private static StringProperty questionProperty = new StringProperty(PROPERTY_QUESTION);
	private static ObjectProperty answerProperty = new ObjectProperty(PROPERTY_ANSWER);
	private static ObjectProperty responseProperty = new ObjectProperty(PROPERTY_RESPONSE);
	
	private Question() {
		// don't allow instantiation
	}

	/**
	 * Gets the questionProperty as a StringProperty.
	 * 
	 * @return questionProperty
	 */
	public static StringProperty getQuestionProperty() {
		return questionProperty;
	}
	
	/**
	 * Gets the answerProperty, which represents the correct answer, as a ObjectProperty.
	 * 
	 * @return answerProperty
	 */
	public static ObjectProperty getAnswerProperty() {
		return answerProperty;
	}
	
	/**
	 * Gets the responseProperty, which represents the given response of a user, as an ObjectProperty.
	 * 
	 * @return responseProperty
	 */
	public static ObjectProperty getResponseProperty() {
		return responseProperty;
	}

	/**
	 * Sets the questionProperty as a StringProperty.
	 * 
	 * @param questionProperty the StringProperty that will be set
	 */
	public static void setQuestionProperty(StringProperty questionProperty) {
		Question.questionProperty = questionProperty;
	}

	/**
	 * Sets the answerProperty, which represents the correct answer, as an ObjectProperty.
	 * 
	 * @param answerProperty the ObjectProperty that will be set
	 */
	public static void setAnswerProperty(ObjectProperty answerProperty) {
		Question.answerProperty = answerProperty;
	}

	/**
	 * Sets the responseProperty, which represents the given response of a user, as an ObjectProperty.
	 * 
	 * @param responseProperty the ObjectProperty that will be set
	 */
	public static void setResponseProperty(ObjectProperty responseProperty) {
		Question.responseProperty = responseProperty;
	}

	/**
	 * Helper Method to set the question and answer of a element trial.
	 * 
	 * @param node
	 * @param question
	 * @param answer
	 */
	public static void setQuestionAnswer(Node node, String question, Object answer) {
		getQuestionProperty().setValue(node, question);
		getAnswerProperty().setValue(node, answer);
	}
	
	/**
	 * Helper Method to set the answer of a element trial.
	 * 
	 * @param node
	 * @param answer
	 */
	public static void setAnswer(Node node, Object answer) {
		getAnswerProperty().setValue(node, answer);
	}
}
