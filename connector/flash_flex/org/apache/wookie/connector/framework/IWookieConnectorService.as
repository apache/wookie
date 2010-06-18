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
 
﻿package org.apache.wookie.connector.framework 
{
	import flash.net.URLLoader;
	
	/**
	 * This service needs to be implemented on each platform. It provides methods
	 * for interfacing with the host environment. In order to use the connection
	 * service the Wookie Connection must have been initialised by calling
	 * connect(...), usually from within the constructor.
	 */
	public interface IWookieConnectorService {
		/**
		 * Setup the connection to the Wookie service. This must be called before any
		 * other method.
		 * 
		 * @param conn A connection to the Wookie server.
		 * @throws WookieConnectorException if there is a problem setting up the connection
		 */
		function setConnection(conn:WookieServerConnection):void;

		/**
		 * Get the currently active connection to the Wookie server.
		 * 
		 * @return WookieServerConnection Current connection to the server.
		 */
		function getConnection():WookieServerConnection;

		/**
		 * Retrieve the details of a specific user, identified with their user_id.
		 * Return null if no user is identified with user_id.
		 * 
		 * @return Data of the user identified with user_id or null.
		 */
		function getUser(user_id:String):User;

		/**
		 * Retrieve the details of the current user.
		 * 
		 * @return Data of the current user.
		 */
		function getCurrentUser():User;

		/**
		 * Set the current user with the user passed as parameter.
		 * 
		 * @param user The next current user.
		 */
		function setCurrentUser(user:User):void;

		/**
		 * Retrieve the user identified with the parameter user_id
		 * and set it as the current user.
		 * 
		 * @param user_id Id of the next current user. Usually this would be the login
		 * name of the user, but it need not be, it simply needs to be a unique identifier
		 * among all users.
		 * @throws WookieConnectorException if no user is identified with user_id.
		 */
		function setCurrentUserWithId(user_id:String):void;

		/**
		 * Get or create an instance of a widget. The current user will be added as a participant.
		 * 
		 * @param widget
		 * @return the widget instance
		 * @throws IOError
		 * @throws SimalRepositoryException
		 */
		function getOrCreateInstance(widget:Widget):WidgetInstanceLoader;

		/**
		 * Get or create an instance of a widget. The current user will be added as a participant.
		 * 
		 * @param guid global identifier for widget to be instantiated
		 * @return The widget instance loader that allows to get the widget instance in an asynchronous manner.
		 * @throws IOError 
		 * @throws SimalRepositoryException 
		 * @throws WookieConnectorexception if no widget is identified with guid.
		 */
		function getOrCreateInstanceWithId(guid:String):WidgetInstanceLoader;

		/**
		 * Add a participant to a widget.
		 * 
		 * @param instance the Widget Instance to add the participant to
		 * @param user the user to add as a participant
		 * @return The loader used to add a participant, which allows to know if operation succeed or failed. 
		 * @throws WookieConnectorexception 
		 */
		function addParticipant(widget:WidgetInstance, user:User):URLLoader;

		/**
		 * Get a set of all the available widgets in the server. If there is an error
		 * communicating with the server return an empty set, or the set received so
		 * far in order to allow the application to proceed. The application should
		 * display an appropriate message in this case.
		 * 
		 * @return The loader having requested the listing of widgets.
		 * This allows to get result in an asynchronous manner.
		 * @throws SimalException
		 */
		function getAvailableWidgets():URLLoader;

		/**
		 * Get all the instances of widgets that are currently managed by this service.
		 *
		 * @return Instance of widgets managed by this service.
		 */
		function getInstances():WidgetInstances;

	}
	
}
