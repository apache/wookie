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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wookie.exceptions.InvalidParametersException;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.helpers.PoliciesHelper;
import org.apache.wookie.proxy.Policies;
import org.apache.wookie.proxy.Policy;
import org.apache.wookie.proxy.PolicyFormatException;

/**
 *
 */
public class PoliciesController extends Controller {

  private static final long serialVersionUID = -5779464009961150201L;
  static Logger _logger = Logger.getLogger(PoliciesController.class.getName());   

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#show(java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void show(String resourceId, HttpServletRequest request,
      HttpServletResponse response) throws ResourceNotFoundException,
      UnauthorizedAccessException, IOException {
    
    Policy[] policies;
    
    //
    // For some reason the main controller resourceId method isn't parsing
    // the resource part correctly
    //
    resourceId = request.getPathInfo().trim();
    if (resourceId != null) resourceId = StringUtils.stripStart(resourceId, "/");
    
    //
    // The resource id is the policy scope. E.g., calling
    // policies/* would get the global policies
    //
    try {
      policies = Policies.getInstance().getPolicies(resourceId);         
    } catch (ConfigurationException e) {
      _logger.error("Problem with policies configuration", e);
      throw new IOException();
    }
    
    switch (format(request)) {
    case XML: returnXml(PoliciesHelper.toXml(policies),response);break;
    case HTML: returnHtml(PoliciesHelper.toHtml(policies),response);break;
    case JSON: returnJson(PoliciesHelper.toJson(policies),response);break;
    }
    
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void index(HttpServletRequest request, HttpServletResponse response)
  throws UnauthorizedAccessException, IOException {
    Policy[] policies;
    try {
      policies = Policies.getInstance().getPolicies();
    } catch (ConfigurationException e) {
      _logger.error("Problem with policies configuration", e);
      throw new IOException();
    }

    switch (format(request)) {
    case XML: returnXml(PoliciesHelper.toXml(policies),response);break;
    case HTML: returnHtml(PoliciesHelper.toHtml(policies),response);break;
    case JSON: returnJson(PoliciesHelper.toJson(policies),response);break;
    }
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#create(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean create(String resourceId, HttpServletRequest request)
      throws ResourceDuplicationException, InvalidParametersException,
      UnauthorizedAccessException {
    try {
      
      //
      // Check the request body
      //
      String policy = request.getReader().readLine();
      return Policies.getInstance().addPolicy(policy);
      
    } catch (ConfigurationException e) {
      
      //
      // Problem with the configuration
      //
      _logger.error("Problem with policies configuration", e);
      throw new InvalidParametersException();
      
    } catch (PolicyFormatException e) {
      
      //
      // Format of the policy is incorrect
      //
      throw new InvalidParametersException();
      
    } catch (IOException e) {
      
      //
      // No body 
      //
      throw new InvalidParametersException();
    }
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.controller.Controller#remove(java.lang.String, javax.servlet.http.HttpServletRequest)
   */
  @Override
  protected boolean remove(String resourceId, HttpServletRequest request)
  throws ResourceNotFoundException, UnauthorizedAccessException,
  InvalidParametersException {

    //
    // For some reason the main controller resourceId method isn't parsing
    // the resource part correctly
    //
    resourceId = request.getPathInfo().trim();
    if (resourceId != null) resourceId = StringUtils.stripStart(resourceId, "/");

    try {

      //
      // Obtain a policy from the resource identifier
      //
      Policy policy;
      policy = new Policy(resourceId);
      Policies.getInstance().removePolicy(policy);
      return true;
      
    } catch (PolicyFormatException e) {

      //
      // The resource isn't formatted correctly
      //
      throw new InvalidParametersException();

    } catch (ConfigurationException e) {

      //
      // Problem with the configuration
      //
      _logger.error("Problem with policies configuration", e);
      throw new InvalidParametersException();
    }

  }

}
