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

package org.apache.wookie.feature;

/**
 * Default IFeature implementation
 */
public class Feature implements IFeature {
  
  private String name;
  private String[] scripts;
  private String[] stylesheets;
  private boolean flattenOnExport;
  private String folder;
  
  
  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#getFolder()
   */
  public String getFolder() {
    return folder;
  }

  /**
   * @param folderName the folderName to set
   */
  public void setFolder(String folder) {
    this.folder = folder;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#flattenOnExport()
   */
  public boolean flattenOnExport() {
    return flattenOnExport;
  }

  /**
   * @param flattenOnExport the flattenOnExport to set
   */
  public void setFlattenOnExport(boolean flattenOnExport) {
    this.flattenOnExport = flattenOnExport;
  }

  public Feature(String name, String[] scripts, String[] stylesheets){
    this.name = name;
    this.scripts = scripts;
    this.stylesheets = stylesheets;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#getName()
   */
  public String getName() {
    return name;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#scripts()
   */
  public String[] scripts() {
    return scripts;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#stylesheets()
   */
  public String[] stylesheets() {
    return stylesheets;
  }

}
