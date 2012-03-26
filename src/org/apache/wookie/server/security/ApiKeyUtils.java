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

package org.apache.wookie.server.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.wookie.Messages;
import org.apache.wookie.exceptions.SystemUnavailableException;
import org.apache.wookie.util.HashGenerator;
import org.apache.wookie.w3c.util.RandomGUID;

/**
 * Static utility methods for API keys
 * 
 * (Under Wookie 0.9.x these methods were used for the "request an API key" function in the admin UI, and are
 * retained here for use either in admin clients or possible future APIs)
 */
public class ApiKeyUtils {
  
  /**
   * Generates and registers a new API key and notifies the requestor via email of the key values.
   * 
   * @param request the request to create a new API key
   * @param email the email address of the registering party
   * @param localizedMessages the set of localized messages
   * @throws Exception 
   */
  public static void generateKey(HttpServletRequest request, String email, Messages localizedMessages) throws Exception {
    
    String hashKey = generateAPIKey(email);
    
    ApiKeys.getInstance().addKey(hashKey, email);
    
    String message = localizedMessages.getString("WidgetKeyManager.0")+hashKey+" \n";  //$NON-NLS-1$//$NON-NLS-2$
    message+="\n" + localizedMessages.getString("WidgetKeyManager.1"); //$NON-NLS-1$ //$NON-NLS-2$
    
    Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
    
    String server = properties.getString("widget.email.server");
    int port = properties.getInt("widget.email.port");
    String contact = properties.getString("widget.email.contact");
    sendEmail(server, port, contact, email, message, properties.getString("widget.email.username"), properties.getString("widget.email.password")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  /**
   * Generate a usable pseudo-random API key that will work OK in a URL
   * @param email
   * @return a String usable as an API key
   * @throws SystemUnavailableException
   */
  public static String generateAPIKey(String email) throws SystemUnavailableException{
    // generate a nonce
    RandomGUID r = new RandomGUID();
    String nonce = "nonce-" + r.toString();        //$NON-NLS-1$

    // now use SHA hash on the nonce        
    String hashKey = HashGenerator.getInstance().encrypt(nonce+email);  

    // get rid of any chars that might upset a url...
    hashKey = hashKey.replaceAll("=", ".eq."); //$NON-NLS-1$ //$NON-NLS-2$
    hashKey = hashKey.replaceAll("\\?", ".qu."); //$NON-NLS-1$ //$NON-NLS-2$
    hashKey = hashKey.replaceAll("&", ".am."); //$NON-NLS-1$ //$NON-NLS-2$
    hashKey = hashKey.replaceAll("\\+", ".pl."); //$NON-NLS-1$ //$NON-NLS-2$
    
    return hashKey;
  }

  /**
   * Send email.
   * 
   * @param mailserver - the SMTP mail server address
   * @param from
   * @param to
   * @param message
   * @throws Exception
   */
  private static void sendEmail(String mailserver, int port, String from, String to, String message, String username, String password) throws EmailException {  
    Email email = new SimpleEmail();
    email.setDebug(false); // true if you want to debug
        email.setHostName(mailserver);
        if (username != null) {
      email.setAuthentication(username, password);
          email.getMailSession().getProperties().put(
             "mail.smtp.starttls.enable", "true");
    }
        email.setFrom(from, "Wookie Server");
        email.setSubject("Wookie API Key");
        email.setMsg(message);
        email.addTo(to);
        email.send();
  }

}
