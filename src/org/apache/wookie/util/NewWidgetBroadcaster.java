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
package org.apache.wookie.util;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class NewWidgetBroadcaster {

    static Logger logger = Logger.getLogger(NewWidgetBroadcaster.class.getName());

    static public void broadcast(Configuration properties, String widgetId){
        boolean enabled;
		try {
			enabled = properties.getBoolean("widget.import.broadcast");
		} catch (Exception e) {
			enabled = false;
		}
        if(enabled){
            String url = properties.getString("widget.import.broadcast.url");//$NON-NLS-1$
            if(url == null || widgetId == null || widgetId.equals("http://notsupported")){//$NON-NLS-1$
                return;
            }
            try {
                HttpClient client = new HttpClient();
                PostMethod post = new PostMethod(url);
                post.addParameter("type", "W3C");//$NON-NLS-1$
                post.addParameter("widgetId", widgetId);
                client.executeMethod(post);
                int code = post.getStatusCode();
                if(code == 200){
                    logger.info("Broadcast of imported widget " +widgetId+ " returned http code " + code);
                }
                else{
                    logger.info("Broadcast of imported widget " +widgetId+ " returned http code " + code 
                            + ". "+post.getResponseBodyAsString());
                }
            } catch (HttpException ex) { 
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }
        }
    }
 
}

