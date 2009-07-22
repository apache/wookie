/**
 * 
 */
package org.apache.wookie.tests.functional;

import junit.framework.TestCase;

/**
 * @author scott
 *
 */
public abstract class AbstractControllerTest extends TestCase {
	
	protected static final String TEST_INSTANCES_SERVICE_URL_VALID = "http://localhost:8080/wookie/widgetinstances";
	protected static final String TEST_PROPERTIES_SERVICE_URL_VALID = "http://localhost:8080/wookie/properties";
	protected static final String TEST_PARTICIPANTS_SERVICE_URL_VALID = "http://localhost:8080/wookie/participants";
	
	protected static final String API_KEY_VALID = "test";
	protected static final String API_KEY_INVALID = "rubbish";
	protected static final String WIDGET_ID_VALID = "http://www.getwookie.org/widgets/natter";
	protected static final String WIDGET_ID_INVALID = "http://www.getwookie.org/widgets/nosuchwidget";

}
