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
package org.apache.wookie.ajaxmodel;
/**
 * IWidgetRuntimeHelper - some static strings needed for configuring whether or not to use queues
 * 
 * @author Paul Sharples
 * @version $Id$ 
 *
 */
public interface IWidgetRuntimeHelper {
	static final String USE_PREFERENCE_INSTANCE_QUEUES = "widget.preferences.useinstancequeues";
	static final String USE_SHAREDDATA_INSTANCE_QUEUES = "widget.shareddata.useinstancequeues";
	static final String DWR_SET_PREFERENCE_CALL = "/wookie/dwr/call/plaincall/WidgetImpl.setPreferenceForKey.dwr";
	static final String DWR_SET_SHAREDDATA_CALL = "/wookie/dwr/call/plaincall/WidgetImpl.setSharedDataForKey.dwr";	
	static final String DWR_APPEND_SHAREDDATA_CALL = "/wookie/dwr/call/plaincall/WidgetImpl.appendSharedDataForKey.dwr";	
}
