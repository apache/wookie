package org.apache.wookie.w3c.util;

import org.apache.commons.lang.CharSetUtils;
import org.apache.commons.lang.StringUtils;

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

/**
 * A utility class for sanitizing unicode strings in accordance with the rules for 
 * handling normalized text content and attributes set out in the W3C Widgets
 * specification
 */
public class UnicodeUtils {
	
	/**
	 * Normalizes all whitespace and space characters in the given string to 
	 * U+0020, then collapses multiple adjacent spaces to a single space, and
	 * removes any leading and trailing spaces. If the input string is null,
	 * the method returns an empty string ("")
	 * @param in the string to normalize
	 * @return the normalized string
	 */
	public static String normalizeWhitespace(String in){
		return normalize(in, true);
	}
	
	/**
	 * Normalizes all space characters in the given string to 
	 * U+0020, then collapses multiple adjacent spaces to a single space, and
	 * removes any leading and trailing spaces. If the input string is null,
	 * the method returns an empty string ("")
	 * @param in the string to normalize
	 * @return the normalized string
	 */
	public static String normalizeSpaces(String in){
		return normalize(in, false);
	}
	
	private static String normalize(String in, boolean includeWhitespace){
		if (in == null) return "";
		String out = "";
		for (int x=0;x<in.length();x++){
			String s = in.substring(x, x+1);
			char ch = s.charAt(0);
			if (Character.isSpaceChar(ch) || (Character.isWhitespace(ch) && includeWhitespace)){
				s = " ";
			}
			out = out + s;
		}
		out = CharSetUtils.squeeze(out, " ");
		out = StringUtils.strip(out);
		return out;
	}

}
