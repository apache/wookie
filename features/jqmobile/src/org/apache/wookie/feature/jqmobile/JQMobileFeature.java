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
package org.apache.wookie.feature.jqmobile;

import org.apache.wookie.feature.IFeature;

public class JQMobileFeature implements IFeature {

	public String getName() {
		return "http://jquerymobile.com";
	}

	public String[] scripts() {
		return new String[]{"http://code.jquery.com/jquery-1.5.min.js","/wookie/shared/feature/jqmobile/jquery.mobile-1.0a3.min.js"};
	}

	public String[] stylesheets() {
		return new String[]{"http://code.jquery.com/mobile/1.0a3/jquery.mobile-1.0a3.min.css"};
	}

}
