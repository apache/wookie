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

package org.apache.wookie.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.ApiKeyHelper;
import org.apache.wookie.server.security.ApiKeys;

/**
 * Admin controller for creating, updating and listing API keys
 * 
 * <ul>
 * <li>GET /keys - index <em>requires authentication</em></li>
 * <li>POST /keys {apikey, email} - create <em>requires authentication</em></li>
 * <li>PUT /keys/{id} {apikey, email} - update <em>requires authentication</em></li>
 * <li>DELETE /keys/{id} - remove <em>requires authentication</em></li>
 * </ul>
 * 
 * Note that PUT support is disabled until a solution is available for migrating
 * widget instances, shared data and participants
 */

public class ApiKeyController extends Controller {

  private static final long serialVersionUID = -2985087125119757793L;
  static Logger _logger = Logger.getLogger(ApiKeyController.class.getName()); 

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void index(HttpServletRequest request, HttpServletResponse response)
      throws UnauthorizedAccessException, IOException {
    returnXml(ApiKeyHelper.createXML(ApiKeys.getInstance().getKeys()),response);
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean create(String resourceId, HttpServletRequest request, HttpServletResponse response)
      throws ResourceDuplicationException, InvalidParametersException,
      UnauthorizedAccessException {
    String value = request.getParameter("apikey");
    String email = request.getParameter("email");
    if (value == null || email == null || value.trim().length() ==0 || email.trim().length() == 0) throw new InvalidParametersException();
    
    try {
      ApiKeys.getInstance().addKey(value, email);
    } catch (Exception e) {
      throw new ResourceDuplicationException();  
    }
    
    _logger.info("New API key registered for "+email);
    return true;
  }

  
/*
  @Override
  protected void update(String resourceId, HttpServletRequest request)
      throws ResourceNotFoundException, InvalidParametersException,
      UnauthorizedAccessException {

    String value = request.getParameter("apikey");
    String email = request.getParameter("email");
    if (value == null || email == null || value.trim().length() ==0 || email.trim().length() == 0) throw new InvalidParametersException();
    
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IApiKey apiKey = persistenceManager.findById(IApiKey.class, resourceId);
    if (apiKey == null) throw new ResourceNotFoundException();
    String oldValue = apiKey.getValue();
    String oldEmail = apiKey.getEmail();
    apiKey.setEmail(email);
    apiKey.setValue(value);
    persistenceManager.save(apiKey);
    migrateWidgetInstances(apiKey, oldValue);
    _logger.info("API key updated from "+oldEmail+" : "+oldValue + " to "+email + " : "+value);

  }
*/

  
  /**
   * Migrates any widget instances using the previous key to the new key.
   * @param key
   * @param oldValue
   */
 /*
  private void migrateWidgetInstances(IApiKey apiKey, String oldValue){
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    // 
    IWidgetInstance[] instances = persistenceManager.findByValue(IWidgetInstance.class, "apiKey", oldValue);
    for (IWidgetInstance instance: instances){
      //FIXME this doesn't really work right now because we can't migrate the shared data key. To do
      //      this we would need to store both the original shared data key and the internal version in the WidgetInstance or
      //      somewhere. (We then ought to rename one of them to make it clear which it is). We could then transparently
      //      update all the sharedDataKeys for instances, participants and shared data
      instance.setApiKey(apiKey.getValue());
      persistenceManager.save(instance);
    }
  }
 */

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean remove(String resourceId, HttpServletRequest request)
      throws ResourceNotFoundException, UnauthorizedAccessException,
      InvalidParametersException {
    
      ApiKeys.getInstance().removeKey(resourceId);
      _logger.info("API key deleted: "+resourceId); 
      return true;
  }
  
}
