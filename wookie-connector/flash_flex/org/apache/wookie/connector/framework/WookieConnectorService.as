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
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;

	/**
	 * Implementation of the interface IWookieConnectorService.
	 */
	public class WookieConnectorService implements IWookieConnectorService {
		private var conn:WookieServerConnection;
		private var instances:WidgetInstances;

		/**
		 * Create a new WookieConnectorService object.
		 * 
		 * @param conn Connection data to a wookie server are otional. 
		 */
		public function WookieConnectorService(conn:WookieServerConnection=null) {
			setConnection(conn);
			this.instances = new WidgetInstances();
		}

		/**
		 * Setup the connection to the Wookie service. This must be called before any
		 * other method.
		 * 
		 * @param conn A connection to the Wookie server
		 * @throws WookieConnectorException
		 *           if there is a problem setting up the connection
		 */
		public function setConnection(conn:WookieServerConnection):void {
			this.conn = conn;
		}

		/**
		 * Get the currently active connection to the Wookie server.
		 * 
		 * @return WookieServerConnection
		 */
		public function getConnection():WookieServerConnection {
			return conn;
		}
		
		/**
		 * Set a property for a widget instance provided in parameters.
		 * 
		 * @param instance Widget instance receiving a property.
		 * @param f_name Name of the property to assign to the widget instance. 
		 * @param f_value Value of the property to assign to the widget instance.
		 * @param property_type Type of the property to assign to the widget instance.
		 * This parameter is optional. It accepts values available in WidgetInstancePropertyType.
		 * @return The loader that set a new property. This allows to check operation results by listening to its events. 
		 * @throws WookieConnectorException
		 */
		public function setPropertyForInstance(instance:WidgetInstance, f_name:String, f_value:String, property_type:String = null):URLLoader {
			// set a default parameter value for property_type, if there is not already one.
			// this workaround has been requiered, as WidgetInstancePropertyType.PUBLIC is a get function
			// and functions are not allowed as default parameter value
			if (null == property_type) {
				property_type = WidgetInstancePropertyType.PUBLIC;
			}
			
			// check value of the property_type parameter
			var is_valid_property_type:Boolean = false;
			for each(var valid_property_type:String in WidgetInstancePropertyType.ALL) {
				if (valid_property_type == property_type) {
					is_valid_property_type = true;
				}
			}
			if (false == is_valid_property_type) {
				throw new Error() // TODO complete and enhance
			}
			
			// compute the request and its parameters
			var query_string:URLVariables = new URLVariables();
			query_string.requestid = property_type;
			query_string.api_key = getConnection().getApiKey();
			query_string.shareddatakey = getConnection().getSharedDataKey();
			query_string.userid = getCurrentUser().getLoginName();
			query_string.widgetid = instance.getId();
			query_string.propertyname = f_name;
			query_string.propertyvalue = f_value;
			
			var widget_service_servlet_url:String = conn.getURL().concat("/WidgetServiceServlet");
			var widget_service_servlet_request:URLRequest = new URLRequest(widget_service_servlet_url);
			
			widget_service_servlet_request.method = URLRequestMethod.GET;
			widget_service_servlet_request.data = query_string;
			
			// get the response corresponding to the computed request
			var widget_service_servlet_loader:URLLoader = new URLLoader();
			try {
				widget_service_servlet_loader.load(widget_service_servlet_request);
			} catch(error:Error) {
				throw new WookieConnectorException(); // TODO complete
			}
			
			// return the loader so that user can catch events
			return widget_service_servlet_loader;
		}

		/**
		 * Get or create an instance of a widget. The current user will be added as a participant.
		 * 
		 * @param widget Data for widget to be instanciated. Only the id is useful.
		 * @return The widget instance corresponding to the parameter widget.
		 * @throws IOError
		 * @throws SimalRepositoryException
		 * @throws WookieConnectorException
		 */
		public function getOrCreateInstance(widget:Widget):WidgetInstanceLoader {
			return this.getOrCreateInstanceWithId(widget.getIdentifier());
		}

		/**
		 * Get or create an instance of a widget. The current user will be added as a participant.
		 * 
		 * @param guid Global unique identifier for widget to be instantiated
		 * @return The widget instance corresponding to the parameter guid.
		 * @throws IOError
		 * @throws SimalRepositoryException
		 * @throws WookieConnectorException
		 */
		public function getOrCreateInstanceWithId(guid:String):WidgetInstanceLoader {
			// compute the request and its parameters
			var postData:URLVariables = new URLVariables();
			// TODO check if escape is requiered or implicite
			postData.api_key = getConnection().getApiKey();
			postData.shareddatakey = getConnection().getSharedDataKey();
			postData.userid = getCurrentUser().getLoginName();
			postData.widgetid = guid;
			
			var widgetInstancesUrl:String = conn.getURL().concat("/widgetinstances");
			var widgetInstancesRequest:URLRequest = new URLRequest(widgetInstancesUrl);
			
			widgetInstancesRequest.method = URLRequestMethod.POST;
			widgetInstancesRequest.data = postData;
			
			// initialise the loader to process the response and load the computed request
			var widgetInstancesLoader:WidgetInstanceLoader = new WidgetInstanceLoader(guid);
			widgetInstancesLoader.addEventListener(Event.COMPLETE, _getOrCreateWidgetInstanceHandler, false, int.MAX_VALUE);
			widgetInstancesLoader.dataFormat = URLLoaderDataFormat.TEXT;
			try {
				widgetInstancesLoader.load(widgetInstancesRequest);
			} catch (err:Error) {
				throw new WookieConnectorException(); // TODO complete
			}
			
			// return the loader so that user can catch events
			return widgetInstancesLoader;
		}

		/**
		 * Add the current user as participant to a new created widget instance
		 * after having saved this instance.
		 * 
		 * @param event Event containing the new widget instance.
		 */
		private function _getOrCreateWidgetInstanceHandler(event:Event):void {
			var widgetInstancesLoader:WidgetInstanceLoader = WidgetInstanceLoader(event.target);
			var widgetInstance:WidgetInstance = WidgetInstance(widgetInstancesLoader.data);
			this.instances.replaceOrAddInstance(widgetInstance);
			this.addParticipant(widgetInstance, this.getCurrentUser());
		}

		/**
		 * Add a participant to a widget.
		 * 
		 * @param instance The WidgetInstance to add the participant to.
		 * @param user The user to add as a participant.
		 * 
		 * @throws WookieConnectorexception
		 */
		public function addParticipant(widget:WidgetInstance, user:User):URLLoader {
			// compute the request and its parameters
			var postData:URLVariables = new URLVariables();
			// TODO check if escape is requiered or implicite
			postData.api_key = getConnection().getApiKey();
			postData.shareddatakey = getConnection().getSharedDataKey();
			postData.userid = getCurrentUser().getLoginName();
			postData.widgetid = widget.getId();
			postData.participant_id = user.getLoginName();
			postData.participant_display_name = user.getScreenName();
			postData.participant_thumbnail_url = user.getThumbnailUrl();
			
			var participantsUrl:String = conn.getURL().concat("/participants");
			var participantsRequest:URLRequest = new URLRequest(participantsUrl);
			
			// TODO participantsRequest.method = URLRequestMethod.POST;
			participantsRequest.method = URLRequestMethod.GET;
			participantsRequest.data = postData;
			
			// get the response corresponding to the computed request
			var participantsLoader:URLLoader = new URLLoader();
			try {
				participantsLoader.load(participantsRequest);
				// TODO what do we have to do if it returns a wrong http status ?
			} catch (err:Error) {
				throw new WookieConnectorException(); // TODO complete
			}
			
			// return the loader so that user can catch events
			return participantsLoader;
		}

		/**
		 * Get a set of all the available widgets in the server. If there is an error
		 * communicating with the server return an empty set, or the set received so
		 * far in order to allow the application to proceed. The application should
		 * display an appropriate message in this case.
		 * 
		 * @return The loader that requested available widgets. This allows to
		 * gather available widgets in an asynchronous manner.
		 * @throws SimalException 
		 * @throws WookieConnectorException 
		 */
		public function getAvailableWidgets():URLLoader {
			// compute the request and its parameters
			var availableWidgetsUrl:String = conn.getURL().concat("/widgets?all=true");
			var availableWidgetsRequest:URLRequest = new URLRequest(availableWidgetsUrl);
			
			// initialise the loader to process the response and load the computed request
			var availableWidgetsLoader:URLLoader = new URLLoader();
			availableWidgetsLoader.addEventListener(Event.COMPLETE, _getAvailableWidgetsHandler, false, int.MAX_VALUE);
			try {
				availableWidgetsLoader.load(availableWidgetsRequest);
			} catch (err:Error) {
				throw new WookieConnectorException(); // TODO complete
			}
			
			// return the loader so that user can catch events
			return availableWidgetsLoader;
		}
		
		/**
		 * Extract data about available widgets as  Widget objects.
		 * 
		 * @param event Listing of available widgets as an XML file.
		 * @throws WookieConnectorException if there is a syntax error in the XML content.
		 */
		private function _getAvailableWidgetsHandler(event:Event):void {
			try {
				var avlWidgets:Dictionary = new Dictionary();
	
				var availableWidgetsLoader:URLLoader = URLLoader(event.target);
				var xmlAvailableWidgets:XML = XML(availableWidgetsLoader.data);
				for each(var avlWidgetEl:XML in xmlAvailableWidgets.widget) {
					var avlWidgetId:String = avlWidgetEl.@identifier;
					if (null == avlWidgets[avlWidgetId]) {
						var avlWidgetTitle:String = avlWidgetEl.title;
						var avlWidgetDesc:String = avlWidgetEl.description;
						var avlWidgetIco:String = avlWidgetEl.icon;
						var avlWidget:Widget = new Widget(avlWidgetId, avlWidgetTitle, avlWidgetDesc, avlWidgetIco);
						avlWidgets[avlWidgetId] = avlWidget;
					}
				}
				availableWidgetsLoader.data = avlWidgets;
			} catch(err:Error) {
				throw new WookieConnectorException(); // TODO complete and enhance
			}
		}

		/**
		 * Get all the instances of widgets that are currently managed by this service.
		 *
		 * @return Instances of widgets managed by this service.
		 */
		public function getInstances():WidgetInstances {
			return this.instances;
		}
		
		/***************************************************************************************************/
		/* user management functions */
		/* this part should be enhanced */
		/***************************************************************************************************/
		private var users:Array;
		private var currentUser:User;
		private var userLogin:String;
		
		/**
		 * Retrieve the details of a specific user, identified with their user_id.
		 * Return null if no user is identified with user_id.
		 * 
		 * @return Data of the user identified with user_id or null.
		 */
		public function getUser(user_id:String):User {
			for each(var user:User in users) {
				if (user.getLoginName() == user_id) {
					return user;
				}
			}
			return null;
		}
		
		/**
		 * [Deprecated]
		 * @deprecated
		 */
		public function setUsers(new_users:Array):void {
			users = new_users;
		}
		
		/**
		 * [Deprecated]
		 * @deprecated
		 */
		public function getUsers():Array {
			return users;
		}
		
		/**
		 * Retrieve the details of the current user.
		 * 
		 * @return Data of the current user.
		 */
		public function getCurrentUser():User {
			return this.currentUser;
		}
		
		/**
		 * Set the current user with the user passed as parameter.
		 * 
		 * @param user The next current user.
		 */
		public function setCurrentUser(user:User):void {
			this.currentUser = user;
		}
		
		/**
		 * Retrieve the user identified with the parameter user_id
		 * and set it as the current user.
		 * 
		 * @param user_id Id of the next current user. Usually this would be the login
		 * name of the user, but it need not be, it simply needs to be a unique identifier
		 * among all users.
		 * @throws WookieConnectorException if no user is identified with user_id.
		 */
		public function setCurrentUserWithId(userId:String):void {
			var user:User = this.getUser(userId);
			if(null != user) {
				setCurrentUser(user);
			} else {
				throw new WookieConnectorException() // TODO complete enhance
			}
		}
		
	}

}
