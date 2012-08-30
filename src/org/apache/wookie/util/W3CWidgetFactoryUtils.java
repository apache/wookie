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

package org.apache.wookie.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

import javax.servlet.ServletContext;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.feature.Features;
import org.apache.wookie.util.digitalsignature.DigitalSignatureProcessor;
import org.apache.wookie.util.html.StartPageProcessor;
import org.apache.wookie.w3c.W3CWidgetFactory;

/**
 * Utility for creating a W3CWidgetFactory using current context and configuration settings
 */
public class W3CWidgetFactoryUtils {
	
	private static Logger logger = Logger.getLogger(W3CWidgetFactoryUtils.class.getName());
	
	/**
	 * Create a contextualized W3CWidgetFactory for parsing W3C Widget packages
	 * @param context the servlet context
	 * @param configuration the configuration properties
	 * @param localizedMessages the set of localized messages for errors/warnings
	 * @return a new W3CWidgetFactory instance
	 * @throws IOException
	 * @throws Exception
	 */
	public static W3CWidgetFactory createW3CWidgetFactory(final ServletContext context, final Configuration configuration, final Messages localizedMessages) throws IOException, Exception {
        final String WIDGETFOLDER = context.getRealPath(configuration.getString("widget.widgetfolder"));
        final String localWidgetFolderPath = configuration.getString("widget.widgetfolder");
        final String[] locales = configuration.getStringArray("widget.locales");
        final String contextPath = context.getContextPath();
        // Digital signature settings
        final boolean VERIFYSIGNATURE = configuration.getBoolean("widget.deployment.verifysignature");//$NON-NLS-1$
        final boolean REJECTINVALID= configuration.getBoolean("widget.deployment.rejectinvalidsignatures");
        final boolean REJECTUNTRUSTED= configuration.getBoolean("widget.deployment.rejectuntrustedsignatures");
        final String PASSWORD = configuration.getString("widget.deployment.trustedkeystore.password");
        final String KEYSTORE = configuration.getString("widget.deployment.trustedkeystore");//$NON-NLS-1$
        
        W3CWidgetFactory fac = new W3CWidgetFactory();
        fac.setLocales(locales);
        fac.setLocalPath(contextPath+localWidgetFolderPath);
        fac.setOutputDirectory(WIDGETFOLDER);
        fac.setFeatures(Features.getFeatureNames());
        fac.setStartPageProcessor(new StartPageProcessor());
        if (VERIFYSIGNATURE) {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            String digSigSchema = context
                    .getRealPath("/WEB-INF/classes/org/apache/wookie/util/digitalsignature/xmldsig-core-schema.xsd");
            InputStream stream = context.getResourceAsStream("/WEB-INF/classes/" + KEYSTORE);
            if (stream == null) {
                stream = context.getResourceAsStream("/WEB-INF/classes/" + "generated-" + KEYSTORE);
            }
            if (stream == null) {
                FileOutputStream fos = new FileOutputStream(context
                        .getRealPath("/WEB-INF/classes/") + "generated-" + KEYSTORE);
                keyStore.load(null, PASSWORD.toCharArray());
                keyStore.store(fos, PASSWORD.toCharArray());
                fos.close();
                fac.setDigitalSignatureParser(new DigitalSignatureProcessor(keyStore,
                        digSigSchema, REJECTINVALID, REJECTUNTRUSTED));
                logger.info(localizedMessages.getString("WidgetHotDeploy.4"));
            } else {
                keyStore.load(stream, PASSWORD.toCharArray());
                stream.close();
                fac.setDigitalSignatureParser(new DigitalSignatureProcessor(keyStore,
                        digSigSchema, REJECTINVALID, REJECTUNTRUSTED));
            }
        }
        return fac;
	}
	
	/**
	 * Create a W3CWidgetFactory instance for parsing W3C Widget packages
	 * @param context the servlet context
	 * @return a new W3CWidgetFactory instance
	 * @throws IOException
	 * @throws Exception
	 */
	public static W3CWidgetFactory createW3CWidgetFactory(ServletContext context) throws IOException, Exception{
		Configuration configuration = (Configuration) context.getAttribute("properties");
		Messages messages = null;
		return createW3CWidgetFactory(context, configuration, messages);
	}

}
