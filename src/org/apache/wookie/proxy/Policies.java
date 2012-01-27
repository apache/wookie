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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

/**
 * Policies management class
 * 
 * <p>This class provides access to policies for the proxy service, 
 * including both global "whitelist" policies and widget-specific
 * policies derived from the &lt;access&gt; element in config.xml</p>
 * 
 * <p>The Policies class is a singleton and maps directly onto the
 * <code>policies</code> configuration file in the server.</p>
 * 
 * <p>Note that policies are loaded dynamically from the <code>
 * policies</code> file whenever it changes on the file system</p>
 */
public class Policies {
  
  static Logger _logger = Logger.getLogger(Policies.class.getName());  
  private  MultiValueMap policies;
  private  PropertiesConfiguration properties;
  private  FileChangedReloadingStrategy reloader = new FileChangedReloadingStrategy();
  
  private static Policies instance;
  
  private Policies() throws ConfigurationException{
    policies = new MultiValueMap();
    properties = new PropertiesConfiguration("policies");
    properties.setReloadingStrategy(reloader);
    properties.getLayout().setGlobalSeparator(" ");
    loadPolicies();
  }
  
  public static Policies getInstance() throws ConfigurationException{
    if (instance == null)
    instance = new Policies();
    return instance;
  }
  
  /**
   * Refresh policies, loading from file if the file
   * has been modified since it was last reloaded.
   * @throws ConfigurationException
   */
  private void refresh() throws ConfigurationException{
    if (reloader.reloadingRequired()){
      policies.clear();
      loadPolicies();
      reloader.reloadingPerformed();
    }
  }
  
  /**
   * Load policies from the policies file
   * @throws ConfigurationException
   */
  private void loadPolicies() throws ConfigurationException{
    properties.clear();
    properties.load();
       
    @SuppressWarnings("rawtypes")
    Iterator keys = properties.getKeys();
    while(keys.hasNext()){
      String key = (String)keys.next();
      String[] entries = properties.getStringArray(key);
      for(int i=0;i<entries.length;i++){
        try {
          addPolicyToCollection(key+" "+entries[i]);
        } catch (Exception e) {
          _logger.error("Error loading Policies from file:", e);
        }
      }
    }
  }
  
  /**
   * Validate a request for a resource with a particular scope
   * @param location the location requested
   * @param scope the scope of the request
   * @return true if the location is allowed in the scope, otherwise false
   * @throws URISyntaxException if the request is not a valid URI
   * @throws ConfigurationException
   */
  public boolean validate(String location, String scope) throws URISyntaxException, ConfigurationException{
    URI uri = new URI(location);
    return validate(uri, scope);
  }
  
  /**
   * Validate a request for a resource with a particular scope
   * @param uri the URI requested
   * @param scope the scope of the request
   * @return true if the location is allowed in the scope, otherwise false
   * @throws URISyntaxException if the request is not a valid URI
   * @throws ConfigurationException
   */
  public boolean validate(URI uri, String scope) throws URISyntaxException, ConfigurationException{
    
    refresh();
    
    //
    // Obtain matching policies
    //
    Policy[] matching = getPolicies(scope);
    
    //
    // Mix in globals
    //
    if (!scope.equals("*")){
        matching = (Policy[]) ArrayUtils.addAll(getPolicies("*"), matching);
    }    
    
    //
    // Check if allowed
    // 
    boolean allowed = false;
    boolean denied = false;
    for (Policy policy:matching){
      if (policy.allows(uri) == 1) allowed = true;
      if (policy.allows(uri) == -1) denied = true;      
    }
    if (allowed && !denied) return true;
    return false;
  }

  /**
   * Add a new policy formatted using a policy string
   * @param policyString
   * @throws PolicyFormatException 
   * @throws ConfigurationException 
   * @throws Exception 
   */
  public boolean addPolicy(String policyString) throws PolicyFormatException, ConfigurationException{
    Policy policy = new Policy(policyString);
    return addPolicy(policy);
  }
  
  /**
   * Add a new policy object
   * @param policy
   * @throws ConfigurationException 
   */
  public boolean addPolicy(Policy policy) throws ConfigurationException{
    boolean added = addPolicyToCollection(policy);
    if(added){
      properties.addProperty(policy.getScope(), policy.getOrigin()+" "+policy.getDirective());
      properties.save();
    }
    return added;
  }
  
  /**
   * Private method for adding a policy to the internal policy collection
   * @param policyString
   * @throws PolicyFormatException 
   * @throws Exception 
   */
  private void addPolicyToCollection(String policyString) throws ConfigurationException, PolicyFormatException{
    Policy policy = new Policy(policyString);
    addPolicyToCollection(policy);
  }
  
  /**
   * Private method for adding a policy to the internal policy collection
   * @param policy
   */
  private boolean addPolicyToCollection(Policy policy){
    @SuppressWarnings("unchecked")
    Collection<Policy> existingPolicies = (Collection<Policy>) policies.get(policy.getScope());
    if (existingPolicies == null){
      policies.put(policy.getScope(), policy);
      return true;
    }
    
    if(!existingPolicies.contains(policy)){        
      //
      // Add the policy
      //
      policies.put(policy.getScope(), policy);
      return true;
      
    } else {
      // Update the policy
      existingPolicies.remove(policy);
      policies.remove(policy.getScope());
      for (Policy match: existingPolicies){
        policies.put(match.getScope(), match);
      }
      policies.put(policy.getScope(), policy);      
      properties.clearProperty(policy.getScope());
      try {
        for (Policy match: getPolicies(policy.getScope())){
          properties.addProperty(match.getScope(), match.getOrigin()+" "+match.getDirective());
        }
        properties.save();
      } catch (ConfigurationException e) {
        _logger.error("Error synchronizing back to Policies file:", e);
      }
      return false;
    }
  }
  
  
  /**
   * Remove a policy
   * @param policyString
   * @throws PolicyFormatException 
   * @throws Exception 
   */
  public void removePolicy(String policyString) throws ConfigurationException, PolicyFormatException{
    Policy policy = new Policy(policyString);
    removePolicy(policy);
  }
  
  /**
   * Remove a policy
   * @param policy
   * @throws ConfigurationException
   */
  public void removePolicy(Policy policy) throws ConfigurationException{

    //
    // Remove from local collection
    //
    @SuppressWarnings("unchecked")
    Collection<Policy> matchingPolicies = (Collection<Policy>) policies.get(policy.getScope());
    matchingPolicies.remove(policy);
    policies.remove(policy.getScope());
    for (Policy match: matchingPolicies) policies.put(match.getScope(), match);
    
    //
    // Remove from properties file
    //
    properties.clearProperty(policy.getScope());
    for (Policy match: getPolicies(policy.getScope())){
      properties.addProperty(match.getScope(), match.getOrigin()+" "+match.getDirective());
    }
    properties.save();

  }
  
  /**
   * Clear all policies relating to the scope provided (typically a widget uri)
   * @param scope
   * @throws ConfigurationException 
   */
  public void clearPolicies(String scope) throws ConfigurationException{
    refresh();
    policies.remove(scope);
    properties.clearProperty(scope);
    properties.save();
  }
  
  /**
   * Clear all policies
   * @throws ConfigurationException
   */
  public void clear() throws ConfigurationException{
    refresh();
    policies.clear();
    properties.clear();
    properties.save();
  }
  
  /**
   * Get all policies within a specific scope
   * @param scope
   * @return an array of Policy objects
   * @throws ConfigurationException
   */
  public Policy[] getPolicies(String scope) throws ConfigurationException{
    refresh();
    @SuppressWarnings("unchecked")
    Collection<Policy> matchedPolicies = (Collection<Policy>) policies.get(scope);
    if (matchedPolicies == null) return new Policy[0];
    return matchedPolicies.toArray(new Policy[matchedPolicies.size()]);
  }
  
  /**
   * Get all policies
   * @return an array of Policy objects
   * @throws ConfigurationException
   */
  @SuppressWarnings("unchecked")
  public Policy[] getPolicies() throws ConfigurationException{
    refresh();
    ArrayList<Policy> allPolicies = new ArrayList<Policy>();
    for (Object key:policies.keySet()){
      allPolicies.addAll(policies.getCollection(key));
    }
    return allPolicies.toArray(new Policy[allPolicies.size()]);
  }
}
