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

package org.apache.wookie.tests.functional;

/**
 * @author scott
 *
 */
public abstract class AbstractControllerTest{
	
	protected static final String TEST_INSTANCES_SERVICE_URL_VALID = "http://localhost:8080/wookie/widgetinstances";
	protected static final String TEST_PROPERTIES_SERVICE_URL_VALID = "http://localhost:8080/wookie/properties";
	protected static final String TEST_PARTICIPANTS_SERVICE_URL_VALID = "http://localhost:8080/wookie/participants";
	protected static final String TEST_WIDGETS_SERVICE_URL_VALID = "http://localhost:8080/wookie/widgets";
	
	protected static final String API_KEY_VALID = "test";
	protected static final String API_KEY_INVALID = "rubbish";
	protected static final String WIDGET_ID_VALID = "http://www.getwookie.org/widgets/natter";
	protected static final String WIDGET_ID_INVALID = "http://www.getwookie.org/widgets/nosuchwidget";

}
