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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.wookie.w3c.util.IRIValidator;

/**
 * A class representing a single Policy, equivalent to a single
 * entry in the <code>policies</code> configuration file
 */
public class Policy {

  private String scope;
  private String origin;
  private String directive;

  public Policy(){

  }

  /**
   * Construct a Policy instance from a policy string comprising
   * the scope, origin and directive separated by spaces
   * @param policy
   * @throws Exception 
   */
  public Policy(String policy) throws PolicyFormatException{
    String[] elements = policy.split(" ");
    if (elements.length == 3){
      setScope(elements[0]);
      setOrigin(elements[1]);
      setDirective(elements[2]);
    } else {
      throw new PolicyFormatException("Policy incorrectly formatted");
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString(){
    String policy = scope + " " + origin + " " + directive;
    return policy;
  }

  /**
   * @return the scope
   */
  public String getScope() {
    return scope;
  }
  /**
   * @param scope the scope to set
   */
  public void setScope(String scope) throws PolicyFormatException{
    this.scope = this.checkScope(scope);
  }
  /**
   * @return the origin
   */
  public String getOrigin() {
    return origin;
  }
  /**
   * @param origin the origin to set
   * @throws Exception 
   */
  public void setOrigin(String origin) throws PolicyFormatException {
    this.origin = this.checkOrigin(origin);
  }
  /**
   * @return the directive
   */
  public String getDirective() {
    return directive;
  }
  /**
   * @param directive the directive to set
   */
  public void setDirective(String directive) throws PolicyFormatException {
    directive = directive.trim().toUpperCase();
    if (directive.equals("ALLOW") || directive.equals("DENY")){
       this.directive = directive;
    } else {
      throw new PolicyFormatException("Unsupported policy directive: "+directive);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (!obj.getClass().equals(this.getClass())) return false;

    Policy otherPolicy = (Policy)obj;

    if(otherPolicy.getScope().equalsIgnoreCase(getScope()) && (otherPolicy.getOrigin().equalsIgnoreCase(getOrigin()))){
      return true;
    }

    return false;
  }

  /**
   * Checks whether the policy grants or denies access to a given URI.
   * @param uri
   * @return returns 1 if the policy grants access to the URI, -1 if it denies access, and 0 if it doesn't match
   * @throws URISyntaxException
   */
  public int allows(URI uri) throws URISyntaxException{
    
    //
    // Policy is being checked which has no origin set
    //
    if(this.getOrigin() == null) return 0;

    //
    // Check valid URI
    //
    if(uri.getScheme() == null) throw new URISyntaxException(uri.toString(), "no valid scheme");
    if(uri.getAuthority() == null) throw new URISyntaxException(uri.toString(), "no valid authority");

    //
    // Wildcard match
    //
    if (getOrigin().equals("*")){
      if (getDirective().equalsIgnoreCase("ALLOW")){
        return 1;
      } else {
        return -1;
      }
    }

    //
    // Get the match origin, and check they match
    //
    URI match = new URI(getOrigin());

    //
    // Check schemes match
    //
    if(!uri.getScheme().equalsIgnoreCase(match.getScheme())) return 0;

    //
    // Check ports match
    //
    int requestedPort = uri.getPort();
    //
    // If there is no specified port in the request, substitute with default port
    // based on the scheme
    //
    if (requestedPort == -1 && uri.getScheme().equals("http")) requestedPort = 80;
    if (requestedPort == -1 && uri.getScheme().equals("https")) requestedPort = 443;
    //
    // Note that where the Policy has no port specified, we treat
    // this as being "any port" otherwise the ports must match
    //
    if( match.getPort() != -1 && requestedPort !=  match.getPort()) return 0;


    if(match.getAuthority().startsWith(("*"))){
      
      //
      // Subdomain match
      //
      String subdomain = uri.getAuthority().split("\\.")[0];
      if (subdomain != null){
        String matchHost = match.getAuthority().replace("*", subdomain);
        if(uri.getHost().equalsIgnoreCase(matchHost))
          if (getDirective().equalsIgnoreCase("ALLOW")){
            return 1;
          } else {
            return -1;
          }
      }
    } else {

      //
      // Simple hosts match
      //
      if(uri.getHost().equalsIgnoreCase(match.getHost()))
        if (getDirective().equalsIgnoreCase("ALLOW")){
          return 1;
        } else {
          return -1;
        }
    }
    return 0;
  }
  
  private String checkScope(String scope) throws PolicyFormatException{
    //
    // Wildcards are a valid scope
    //
    if (scope.equals("*")) return scope;
    
    try {
      //
      // URLs are a valid scope
      //
      new URL(scope);
      return scope;
    } catch (MalformedURLException e) {
      //
      // IRIs are a valid scope
      //
      if (!IRIValidator.isValidIRI(scope)) throw new PolicyFormatException("scope is not a valid wildcard, URL or IRI");
      return scope;
    }
  }

  /**
   * Checks whether a supplied origin parameter is valid, and returns the processed result
   * @param origin
   * @return a processed origin with extraneous elements removed
   * @throws PolicyFormatException if the origin is not valid
   */
  private String checkOrigin(String origin) throws PolicyFormatException{
    origin = origin.trim();
    if (origin.equals("*")) return origin;
    
    if (!IRIValidator.isValidIRI(origin)) throw new PolicyFormatException("origin is not a valid IRI");
    

    URI uri;
    try {
      uri = new URI(origin);
      if ((uri.getHost() == null && (uri.getAuthority() == null || !uri.getAuthority().startsWith("*.")))) throw new PolicyFormatException("origin has no host");
      if (uri.getUserInfo()!=null) throw new PolicyFormatException("origin has userinfo");
      
      URI processedURI;
      //
      // If the origin contains a *, we construct it using an authority
      //
      if (uri.getAuthority().startsWith("*.")){
        processedURI = new URI(uri.getScheme(),uri.getAuthority(),null,null,null);
      } else {
        //
        // Remove query and path
        //        
        processedURI = new URI(uri.getScheme(),null,uri.getHost(),uri.getPort(),null,null,null);
      }
      
      return processedURI.toString();
    } catch (URISyntaxException e1) {
      throw new PolicyFormatException("origin is not a valid URI");
    }
  }

}
