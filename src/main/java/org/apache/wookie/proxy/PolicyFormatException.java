/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.proxy;

/**
 * An Exception thrown when a Policy is incorrectly formatted
 */
public class PolicyFormatException extends Exception {

  private static final long serialVersionUID = 1273238260784592379L;
  
  public PolicyFormatException(String reason){
    super(reason);
  }

}
