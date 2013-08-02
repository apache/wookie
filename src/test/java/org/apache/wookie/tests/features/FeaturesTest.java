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

package org.apache.wookie.tests.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.wookie.feature.Feature;
import org.apache.wookie.feature.Features;
import org.junit.Test;

/**
 * Tests the Features capability by loading and parsing various
 * test feature.xml files, and checking the mechanism for iterating
 * over feature folders
 */
public class FeaturesTest {
  
  /**
   * The location of the test data folder
   */
  private static final String testDataFolder = "src/test/java/org/apache/wookie/tests/features/";
  
  /**
   * Base path for feature resources
   */
  private static final String basePath = "/test";
  
  /**
   * Load a basic feature, consisting just of a name
   * @throws Exception
   */
  @Test
  public void loadFeature() throws Exception{
    File featureFile = new File(testDataFolder+"feature.xml");
    assertTrue(featureFile.exists());
    Feature feature = Features.loadFeature(featureFile, basePath);
    assertEquals("test:feature", feature.getName());
    assertEquals(0, feature.stylesheets().length);
    assertEquals(0, feature.scripts().length);
  }
  
  /**
   * Load a feature with an invalid name
   * @throws Exception
   */
  @Test(expected = Exception.class)
  public void loadFeatureInvalidName() throws Exception{
    File featureFile = new File(testDataFolder+"feature_invalid_name.xml");
    assertTrue(featureFile.exists());
    try {
      @SuppressWarnings("unused")
      Feature feature = Features.loadFeature(featureFile, basePath);
    } catch (Exception e) {
      assertEquals("Invalid feature: name is not a valid IRI", e.getMessage());
      throw e;
    } 
  }
  
  /**
   * Load a feature with a stylesheet
   * @throws Exception
   */
  @Test
  public void loadFeatureWithResources() throws Exception{
    File featureFile = new File(testDataFolder+"feature_with_resources.xml");
    assertTrue(featureFile.exists());   
    Feature feature = Features.loadFeature(featureFile, basePath);
    assertEquals(1, feature.stylesheets().length);
    assertEquals(0, feature.scripts().length);
    assertEquals("/test/test_styles.css", feature.stylesheets()[0]);
  }
  
  /**
   * Load a feature with scripts, both local and absolute
   * @throws Exception
   */
  @Test
  public void loadFeatureWithScripts() throws Exception{
    File featureFile = new File(testDataFolder+"feature_with_scripts.xml");
    assertTrue(featureFile.exists());   
    Feature feature = Features.loadFeature(featureFile, basePath);
    assertEquals(0, feature.stylesheets().length);
    assertEquals(2, feature.scripts().length);
    assertEquals("/test/test_script.js", feature.scripts()[0]);    
    assertEquals("/absolute_path_to_script.js", feature.scripts()[1]);    
  }
  
  @Test
  public void loadFeatureWithFlattenAttribute() throws Exception{
    File featureFile = new File(testDataFolder+"feature_with_flatten_attr.xml");
    assertTrue(featureFile.exists());   
    Feature feature = Features.loadFeature(featureFile, basePath);    
    assertTrue(feature.flattenOnExport());
  }

  
  /**
   * Loads features from the test folder, ignoring everything
   * that isn't a folder that contains a "features.xml" file
   */
  @Test
  public void loadFeatures(){
    File featureFolder = new File(testDataFolder+"test_features_folder");
    assertTrue(featureFolder.exists());
    assertTrue(featureFolder.isDirectory());
    Features.loadFeatures(featureFolder, basePath);
    assertEquals(1, Features.getFeatures().size());
    assertEquals(1, Features.getFeatureNames().length);
    assertEquals("test:feature", Features.getFeatureNames()[0]);
    //TODO fails on windows    
    //assertEquals(testDataFolder+"test_features_folder/test", Features.getFeatures().get(0).getFolder());
  }
}
