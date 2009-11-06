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
package org.apache.wookie.util;

import antlr.StringUtils;

/**
 * Utility for handling numbers according to W3C processing rules
 * @author scott
 *
 */
public class NumberUtils {
	
	public static int processNonNegativeInteger(String in) throws NumberFormatException {
		int result = 0;
		in = UnicodeUtils.normalizeSpaces(in);
		StringUtils.stripFront(in, " ");
		
		if (in.length() == 0) throw new NumberFormatException("no non-space characters");

		for (int pos=0;pos<in.length();pos++){
			String nextchar = in.substring(pos, pos+1);
			// If the nextchar is not one of U+0030 (0) .. U+0039 (9), then return result.
			try {
				int i = Integer.parseInt(nextchar);
				result = result * 10 + i;
			} catch (Exception e) {
				return result;
			}
		}
		
		return result;
		
	}

}
