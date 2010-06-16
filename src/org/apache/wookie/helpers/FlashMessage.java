/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wookie.helpers;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

/**
 * A singleton class that collects messages sent by other processes and appends them to session messages
 * displayed in admin views. For example, this is used to notify the user that widgets have been dropped into the deploy
 * folder - either reporting they were added successfully, or reporting the error.
 */
public class FlashMessage {

	private static FlashMessage _instance;
	private ArrayList<Message> _messages;
	
	public static FlashMessage getInstance(){
		if (_instance == null) _instance = new FlashMessage();
		return _instance;
	}
	
	private FlashMessage(){
		_messages = new ArrayList<Message>();
	}
	
	/**
	 * Add an information (success) message to the flash message buffer
	 * @param message the message to add
	 */
	public void message(String message){
		_messages.add(new Message("message", message));
	}
	
	/**
	 * Add an error message to the flash message buffer.
	 * @param message the message to add
	 */
	public void error(String message){
		_messages.add(new Message("error", message));
	}

	private Message[] getMessages(){
		return (Message[]) _messages.toArray(new Message[_messages.size()]);
	}
	
	private String showErrors(){
		String messages = "";
		ArrayList<Message> toRemove = new ArrayList<Message>();
		for (Message message:getMessages()){
			if (message.type.equals("error")){
				messages += "<p>"+message.text+"</p>";
				toRemove.add(message);
			}
		}
		_messages.removeAll(toRemove);
		return messages;
	}
	
	private String showMessages(){
		String messages = "";
		ArrayList<Message> toRemove = new ArrayList<Message>();
		for (Message message:getMessages()){
			if (message.type.equals("message")){
				messages += "<br/>"+message.text;
				toRemove.add(message);
			}
		}
		_messages.removeAll(toRemove);
		return messages;
	}
	
	/**
	 * Append any outstanding messages to the session message attributes;
	 * messages are then removed from the buffer and so will not appear again
	 */
	public void appendFlashMessages(HttpSession session){
		String errors = (String) session.getAttribute("error_value");
		if (errors == null) errors = "";
		errors += FlashMessage.getInstance().showErrors();
		if (errors.equals("")) errors = null;
		session.setAttribute("error_value", errors);
		
		String messages = (String) session.getAttribute("message_value");
		if (messages == null) messages = "";
		messages += FlashMessage.getInstance().showMessages();
		if (messages.equals("")) messages = null;
		session.setAttribute("message_value", messages);
	}
	
	class Message {
		String type;
		String text;
		public Message(String type, String message){
			this.type = type;
			this.text = message;
			
		}
	}
	
}
