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

package org.apache.wookie.w3c;

/**
 * an Author of a Widget
 */
public interface IAuthorEntity extends ILocalizedEntity {

	public String getAuthorName();

	public void setAuthorName(String authorName);

	public String getHref();

	public void setHref(String href);

	public String getEmail();

	public void setEmail(String email);

}
