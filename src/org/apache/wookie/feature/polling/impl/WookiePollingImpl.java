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

package org.apache.wookie.feature.polling.impl;

import org.apache.wookie.feature.polling.IWookiePolling;
/**
 * Polling Support - turned on by using a 
 * <feature name="http://www.getwookie.org/usefeature/polling"> tag in the manifest file
 *   
 * @author Paul Sharples
 * @version $Id: WookiePollingImpl.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $ 
 */
public class WookiePollingImpl implements IWookiePolling {

	public String getJavaScriptImpl() {		
		return null;
	}

	public String getJavaScriptWrapper() {
		return "/wookie/shared/js/wookie-polling-feature.js";
	}

}
