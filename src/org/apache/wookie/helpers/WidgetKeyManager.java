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

package org.apache.wookie.helpers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

import org.apache.wookie.Messages;
import org.apache.wookie.beans.ApiKey;
import org.apache.wookie.exceptions.SystemUnavailableException;
import org.apache.wookie.util.HashGenerator;
import org.apache.wookie.util.RandomGUID;

/**
 * @author scott
 *
 */
public class WidgetKeyManager{
  static Logger _logger = Logger.getLogger(WidgetKeyManager.class.getName());
	
	/**
	 * Revoke the given key
	 * @param id the api key value
	 * @return true if the key was successfully revoked
	 */
	public static boolean revokeKey(String id){
		if (id == null) return false;
		if (id.trim().equals("")) return false;
		id = id.trim();
		ApiKey[] key = ApiKey.findByValue("value", id);
		if (key == null || key.length !=1);
		return revokeKey(key[0]);
	}
	
	/**
	 * Revoke a key
	 * @param key the key to revoke
	 */
	private static boolean revokeKey(ApiKey key){
		if (key == null) return false;
		key.delete();
		return true;
	}
	
	/**
	 * Registers a new API key and notifies the requestor via email of the key values.
	 * 
	 * @param key
	 * @param domain
	 * @throws EmailException if there is a problem sending the email notification about this key
	 * @throws SystemUnavailableException if there is a problem generating the key
	 */
	public static void createKey(HttpServletRequest request, String email, Messages localizedMessages) throws SystemUnavailableException, EmailException {
		
		ApiKey key = new ApiKey();
		key.setEmail(email);

		// generate a nonce
		RandomGUID r = new RandomGUID();
		String nonce = "nonce-" + r.toString();				 //$NON-NLS-1$

		// now use SHA hash on the nonce				
		String hashKey = HashGenerator.getInstance().encrypt(nonce+email);	

		// get rid of any chars that might upset a url...
		hashKey = hashKey.replaceAll("=", ".eq."); //$NON-NLS-1$ //$NON-NLS-2$
		hashKey = hashKey.replaceAll("\\?", ".qu."); //$NON-NLS-1$ //$NON-NLS-2$
		hashKey = hashKey.replaceAll("&", ".am."); //$NON-NLS-1$ //$NON-NLS-2$
		hashKey = hashKey.replaceAll("\\+", ".pl."); //$NON-NLS-1$ //$NON-NLS-2$

		key.setValue(hashKey);
		key.save();
		
		String message = localizedMessages.getString("WidgetKeyManager.0")+hashKey+" \n";  //$NON-NLS-1$//$NON-NLS-2$
		message+="\n" + localizedMessages.getString("WidgetKeyManager.1"); //$NON-NLS-1$ //$NON-NLS-2$
		
		Configuration properties = (Configuration) request.getSession().getServletContext().getAttribute("properties"); //$NON-NLS-1$
		
		String server = properties.getString("widget.email.server");
		int port = properties.getInt("widget.email.port");
		String contact = properties.getString("widget.email.contact");
		sendEmail(server, port, contact, email, message, properties.getString("widget.email.username"), properties.getString("widget.email.password")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Check if the given key is valid
	 * @param key the api key supplied with the request

	 * @return true if valid, otherwise false
	 */
	public static boolean isValid(String key) {
		// No key/n
		if (key == null) {
		  _logger.info("No API key supplied");
		  return false;
		}

		// Empty key/empty origin
		if (key.trim().equals("")) return false; //$NON-NLS-1$
		ApiKey[] apiKey = ApiKey.findByValue("value", key);
		if (apiKey == null || apiKey.length !=1) {
		  _logger.info("Invalid API key supplied: " + key);
		  return false;
		}
		return true;
	}

	/**
	 * Checks if the given request is accompanied by a valid API key
	 * @param request
	 * @return true if valid, otherwise false
	 */
	public static boolean isValidRequest(HttpServletRequest request) {
		return isValid(request.getParameter("api_key")); //$NON-NLS-1$
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
