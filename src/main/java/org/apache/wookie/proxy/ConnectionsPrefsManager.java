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
 */
public class ConnectionsPrefsManager {
	
	static Logger fLogger = Logger.getLogger(ConnectionsPrefsManager.class.getName());

	private static boolean fIsProxySet = false;
	private static boolean fParsedFile = false;
	private static boolean fUseNTLMAuthentication = false;
	private static String fHostname = null;
		
	private static void init(Configuration properties){
		fHostname = properties.getString("system.proxy.hostname");				
		if(fHostname != null) {
			fIsProxySet = (fHostname.length() > 0 ? true : false);
		}
		fUseNTLMAuthentication = properties.getBoolean("system.proxy.usentlmauthentication",false);
		fParsedFile = true;
	}

    private static boolean isProxyServerSet(){
    	return fIsProxySet;
    }
    
	public static void setProxySettings(HttpClient client, Configuration properties) {
		if(!fParsedFile) init(properties); // just do this once - will have to reboot for changes to take effect
		if(isProxyServerSet()){
    		int port = properties.getInt("system.proxy.port",8080); //$NON-NLS-1$
    		String username =  properties.getString("system.proxy.username"); //$NON-NLS-1$
    		String password =  properties.getString("system.proxy.password"); //$NON-NLS-1$
    		
			fLogger.info("setting proxy conf:"+fHostname+":"+port);
    		
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
