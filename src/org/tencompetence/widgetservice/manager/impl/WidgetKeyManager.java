/**
 * 
 */
package org.tencompetence.widgetservice.manager.impl;

import java.io.PrintWriter;
import java.net.Socket;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.ApiKey;
import org.tencompetence.widgetservice.util.HashGenerator;
import org.tencompetence.widgetservice.util.RandomGUID;

/**
 * @author scott
 *
 */
public class WidgetKeyManager{
	
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
	 * Registers a new API key
	 * @param key
	 * @param domain
	 * @throws Exception
	 */
	public static void createKey(HttpServletRequest request, String email, Messages localizedMessages) throws Exception{
		
		ApiKey key = new ApiKey();
		key.setEmail(email);

		// generate a nonce
		String nonce = RandomGUID.getUniqueID("nonce-");				 //$NON-NLS-1$

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
		
		sendEmail(properties.getString("widget.email.server"), properties.getInt("widget.email.port"), properties.getString("widget.email.contact"), email, message); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Check if the given key is valid
	 * @param key the api key supplied with the request

	 * @return true if valid, otherwise false
	 */
	public static boolean isValid(String key) {
		// No key/n
		if (key == null) return false;

		// Empty key/empty origin
		if (key.trim().equals("")) return false; //$NON-NLS-1$
		ApiKey[] apiKey = ApiKey.findByValue("value", key);
		if (apiKey == null || apiKey.length !=1) return false;
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
	 * TODO use JavaMail or similar to do this PROPERLY
	 * @param from
	 * @param to
	 * @param message
	 * @throws Exception
	 */
	private static void sendEmail(String mailserver, int port, String from, String to, String message) throws Exception
	{
		try
		{
			// get a socket connection to the mail
			// server at SMTP port 25
			Socket socket = new Socket(mailserver, port);

			// Create an output stream for sending message
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// send mail using SMTP protocol
			out.println("MAIL FROM: " + from); //$NON-NLS-1$
			out.println("RCPT TO: " + to); //$NON-NLS-1$
			out.println("DATA\n");  // Skip line after DATA //$NON-NLS-1$
			out.println(message);
			out.println(".");       // End message with a single period //$NON-NLS-1$
			out.flush();
		}
		catch (Exception e)
		{
			System.out.println("Failed to send email: " + e); //$NON-NLS-1$
		}

	}

}
