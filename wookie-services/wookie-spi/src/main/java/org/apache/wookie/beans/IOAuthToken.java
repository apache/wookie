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
 *  limitations under the License.
 */
package org.apache.wookie.beans;

public interface IOAuthToken extends IBean {

  String getAuthzUrl();

  void setAuthzUrl(String authzUrl);

  String getAccessToken();

  void setAccessToken(String accessToken);

  public String getClientId();

  public void setClientId(String clientId);

  public long getExpires();

  public void setExpires(long expires);

  public boolean isExpires();

}
