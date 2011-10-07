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
package org.apache.wookie.util.html;

import org.htmlcleaner.DoctypeToken;

/**
 * 
 * An extended HTML Cleaner DocTypeToken class to deal with HTML5 declarations better then the default, which displays emtpy strings and nulls.
 * 
 * Note: <!DOCTYPE html SYSTEM "about:legacy-compat"> is also a valid HTML5 doctype - but html cleaner only makes the html
 * into uppercase, which although is still not correct, doesn't seem to cause problems in wookie at present.
 * 
 * http://sourceforge.net/tracker/?func=detail&aid=3190583&group_id=183053&atid=903696
 * 
 */
public class Html5DoctypeToken extends DoctypeToken {
	
	public static String BADDOCTYPE = "<!DOCTYPE HTML null \"\">";
	public static String GOODDOCTYPE = "<!DOCTYPE html>";

	public Html5DoctypeToken(String part1, String part2, String part3,
			String part4) {
		super(part1, part2, part3, part4);		
	}
		
	public String getContent(){
		return GOODDOCTYPE;
	}

}
