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

package org.apache.wookie.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.feature.Features;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.util.NewWidgetBroadcaster;
import org.apache.wookie.util.WgtWatcher;
import org.apache.wookie.util.WidgetFileUtils;
import org.apache.wookie.util.WidgetJavascriptSyntaxAnalyzer;
import org.apache.wookie.util.digitalsignature.DigitalSignatureProcessor;
import org.apache.wookie.util.html.StartPageProcessor;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.util.WidgetPackageUtils;

/**
 * ContextListener - does some init work and makes certain things are available 
 * to resources under this context
 * 
 * @author Paul Sharples
 * @version $Id: ContextListener.java,v 1.2 2009-07-28 16:05:23 scottwilson Exp $ 
 *
 */
public class ContextListener implements ServletContextListener {
    /*
     * In the case of the 'log4j.properties' file used within a server environment
     * there is no need to explicitly load the file.  It will be automatically loaded as
     * long as it is placed at the root of the source code. This way it eventually is found under...
     * 
     * 			'/webappname/WEB-INF/classes'  ...at runtime.
     */
    static Logger _logger = Logger.getLogger(ContextListener.class.getName());
    public static Boolean usePreferenceInstanceQueues;
    public static Boolean useSharedDataInstanceQueues;

    public void contextInitialized(ServletContextEvent event) {
        try {
            ServletContext context = event.getServletContext();
            WidgetRuntimeHelper.setWebContextPath(context.getContextPath());
            /* 
             *  load the widgetserver.properties and local.widget.properties file
             *  and put it into this context as an attribute 'properties' available to all resources
             */
            File localPropsFile = new File(System.getProperty("user.dir") + File.separator + "local.widgetserver.properties");
            PropertiesConfiguration localConfiguration = new PropertiesConfiguration(localPropsFile);
            CompositeConfiguration configuration = new CompositeConfiguration();
            configuration.addConfiguration(localConfiguration);
            configuration.addConfiguration(new PropertiesConfiguration("widgetserver.properties"));

            context.setAttribute("properties", (Configuration) configuration);

            // load these up now so we don't have to do it on every request(i.e. filter) in the future
            usePreferenceInstanceQueues = configuration.getBoolean(WidgetRuntimeHelper.USE_PREFERENCE_INSTANCE_QUEUES);	
            useSharedDataInstanceQueues = configuration.getBoolean(WidgetRuntimeHelper.USE_SHAREDDATA_INSTANCE_QUEUES);		 	

            /*
             * Merge in system properties overrides
             */
            Iterator<Object> systemKeysIter = System.getProperties().keySet().iterator();
            while (systemKeysIter.hasNext()) {
                String key = systemKeysIter.next().toString();
                if (configuration.containsKey(key) || key.startsWith("widget.")) {
                    String setting = configuration.getString(key);
                    String override = System.getProperty(key);
                    if ((override != null) && (override.length() > 0) && !override.equals(setting)) {
                        configuration.setProperty(key, override);
                        if (setting != null) {
                            _logger.info("Overridden server configuration property: " + key + "=" +override);
                        }
                    }
                }
            }

            /*
             * Initialize persistence manager factory now, not on first request
             */
            PersistenceManagerFactory.initialize(configuration);

            /*
             * Initialise the locale handler
             */
            LocaleHandler.getInstance().initialize(configuration);
            final Locale locale = new Locale(configuration.getString("widget.default.locale"));
            final Messages localizedMessages = LocaleHandler.getInstance().getResourceBundle(locale);

            /* 
             *  load the opensocial.properties file and put it into this context
             *  as an attribute 'opensocial' available to all resources
             */
            File localOpenSocialPropsFile = new File(System.getProperty("user.dir") + File.separator + "local.opensocial.properties");
            PropertiesConfiguration localOpenSocialConfiguration = new PropertiesConfiguration(localOpenSocialPropsFile);
            CompositeConfiguration opensocialConfiguration = new CompositeConfiguration();
            opensocialConfiguration.addConfiguration(localOpenSocialConfiguration);
            opensocialConfiguration.addConfiguration(new PropertiesConfiguration("opensocial.properties"));
            context.setAttribute("opensocial", (Configuration) opensocialConfiguration);

            /*
             * Load installed features
             */
            Features.loadFeatures(context);

            /*
             * Run diagnostics
             */
            Diagnostics.run(context, configuration);

            /*
             * Start hot-deploy widget watcher
             */
            if (configuration.getBoolean("widget.hot_deploy")) {
                startWatcher(context, configuration, localizedMessages);
            } else {
                _logger.info(localizedMessages.getString("WidgetHotDeploy.0"));
            }
        } 
        catch (ConfigurationException ex) {
            _logger.error("ConfigurationException thrown: "+ ex.toString());
        }
    }

    /**
     * Starts a watcher thread for hot-deploy of new widgets dropped into the deploy folder
     * this is controlled using the <code>widget.hot_deploy=true|false</code> property 
     * and configured to look in the folder specified by the <code>widget.deployfolder</code> property
     * @param context the current servlet context
     * @param configuration the configuration properties
     */
    private void startWatcher(final ServletContext context, final Configuration configuration, final Messages localizedMessages){
        /*
         * Start watching for widget deployment
         */
        final File deploy = new File(WidgetPackageUtils.convertPathToPlatform(context.getRealPath(configuration.getString("widget.deployfolder"))));
        final String UPLOADFOLDER = context.getRealPath(configuration.getString("widget.useruploadfolder"));
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


        Thread thr = new Thread(){
            public void run() {
                int interval = 5000;
                WgtWatcher watcher = new WgtWatcher();
                watcher.setWatchDir(deploy);
                watcher.setListener(new WgtWatcher.FileChangeListener(){
                    public void fileModified(File f) {
                        // get persistence manager for this thread
                        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
                        try{
                            persistenceManager.begin();
                            File upload = WidgetFileUtils.dealWithDroppedFile(UPLOADFOLDER, f);
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
                                    _logger.info(localizedMessages.getString("WidgetHotDeploy.4"));
                                } else {
                                    keyStore.load(stream, PASSWORD.toCharArray());
                                    stream.close();
                                    fac.setDigitalSignatureParser(new DigitalSignatureProcessor(keyStore,
                                            digSigSchema, REJECTINVALID, REJECTUNTRUSTED));
                                }
                            }

                            W3CWidget model = fac.parse(upload);
                            WidgetJavascriptSyntaxAnalyzer jsa = new WidgetJavascriptSyntaxAnalyzer(fac.getUnzippedWidgetDirectory());
                            if(persistenceManager.findWidgetByGuid(model.getIdentifier()) == null) {
                                WidgetFactory.addNewWidget(model, upload, true);
                                String message = model.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.19");
                                _logger.info(message);
                            } else {
                                String message = model.getLocalName("en") +"' - " + localizedMessages.getString("WidgetAdminServlet.20");
                                WidgetFactory.update(model, persistenceManager.findWidgetByGuid(model.getIdentifier()), true, upload);
                                _logger.info(message);
                            }
                            persistenceManager.commit();
                            NewWidgetBroadcaster.broadcast(configuration, model.getIdentifier());
                        } catch (IOException e) {
                            persistenceManager.rollback();
                            String error = f.getName()+":"+localizedMessages.getString("WidgetHotDeploy.1") + " - " + e.getLocalizedMessage();
                            _logger.error(error, e);
                        } catch (BadWidgetZipFileException e) {
                            persistenceManager.rollback();
                            String error = f.getName()+":"+localizedMessages.getString("WidgetHotDeploy.2") + " - " + e.getLocalizedMessage();
                            _logger.error(error, e);
                        } catch (BadManifestException e) {
                            persistenceManager.rollback();
                            String error = f.getName()+":"+localizedMessages.getString("WidgetHotDeploy.3") + " - " + e.getLocalizedMessage();
                            _logger.error(error, e);
                        } catch (Exception e) {
                            persistenceManager.rollback();
                            String error = f.getName()+":"+e.getLocalizedMessage();
                            _logger.error(error, e);
                        } finally {
                            // close thread persistence manager
                            PersistenceManagerFactory.closePersistenceManager();						    
                        }
                    }
                    public void fileRemoved(File f) {
                        // Not implemented - the .wgt files are removed as part of the deployment process
                    }
                });
                try {
                    while (true) {
                        watcher.check();
                        Thread.sleep(interval);
                    }
                } catch (InterruptedException iex) {
                }
            }	
        };

        thr.start();

    }

    public void contextDestroyed(ServletContextEvent event){
        /*
         * Terminate persistence manager factory
         */
        PersistenceManagerFactory.terminate();	    
    }
}
