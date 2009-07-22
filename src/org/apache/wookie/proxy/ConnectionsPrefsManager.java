/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.apache.wookie.proxy;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.log4j.Logger;

/**
 * Read in & process the proxy settings from the properties file 
 * @author Paul Sharples
 * @version $Id: ConnectionsPrefsManager.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
 *
 */
public class ConnectionsPrefsManager {
	
	static Logger fLogger = Logger.getLogger(ConnectionsPrefsManager.class.getName());

	private static boolean fIsProxySet = false;
	private static boolean fParsedFile = false;
	private static boolean fUseNTLMAuthentication = false;
	private static String fHostname = null;
		
	private static void init(Configuration properties){
		fHostname = properties.getString("widget.proxy.hostname");				
		if(fHostname != null) {
			fIsProxySet = (fHostname.length() > 0 ? true : false);
		}
		fUseNTLMAuthentication = properties.getBoolean("widget.proxy.usentlmauthentication");
		fParsedFile = true;
	}

    private static boolean isProxyServerSet(){
    	return fIsProxySet;
    }
    
	public static void setProxySettings(HttpClient client, Configuration properties) {
		if(!fParsedFile) init(properties); // just do this once - will have to reboot for changes to take effect
		if(isProxyServerSet()){
			int port;
    		try{
    			port = properties.getInt("widget.proxy.port");
    		} 
    		catch(Exception ex){
    			port=8080; // default for now if not specified
    		}
    		String username =  properties.getString("widget.proxy.username");
    		String password =  properties.getString("widget.proxy.password");
    		
    		HostConfiguration hConf= client.getHostConfiguration();
    		hConf.setProxy(fHostname, port);
    		AuthScope scopeProxy = new AuthScope(fHostname,port , AuthScope.ANY_REALM);
       		if(fUseNTLMAuthentication){								  
    			String domain = System.getenv("USERDOMAIN");    //$NON-NLS-1$
    			String computerName = System.getenv("COMPUTERNAME");//$NON-NLS-1$
    			if (domain!=null && computerName!=null){
    				NTCredentials userNTLMCredentials = new NTCredentials(username, password, computerName, domain);
    				client.getState().setProxyCredentials(scopeProxy, userNTLMCredentials);
    			}
    			else {
    				fLogger.error("Cannot find domain or computername for NTLM proxy authentication");
    			}
    		}
    		else{
    			client.getState().setProxyCredentials(scopeProxy, new UsernamePasswordCredentials(username, password));
    		}
		}
		
	}

}
