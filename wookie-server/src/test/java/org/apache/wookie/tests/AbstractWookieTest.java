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
package org.apache.wookie.tests;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class AbstractWookieTest {
  
  // Use the boolean variable below to turn 
  // on/off url encoding in all the tests
  private static boolean useEncoding = false;
  // an easier way to change the encoding for 
  // all of the tests
  private static String encodingType = "UTF-8";
  
  public static String encodeString(String path) throws UnsupportedEncodingException{
    if (useEncoding){
      return URLEncoder.encode(path, encodingType);
    }
    else{     
      path = path.replaceAll(" ", "%20");       
     // path = path.replaceAll(" ", "+");
    }
    return path;
  }

}
