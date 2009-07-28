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

package org.apache.wookie.manifestmodel;
/**
 * @author Paul Sharples
 * @version $Id: IContentEntity.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public interface IContentEntity extends IManifestModelJDOM {

	public String getSrc();

	public void setSrc(String src);

	public String getCharSet();

	public void setCharSet(String charSet);

	public String getType();

	public void setType(String type);

}
