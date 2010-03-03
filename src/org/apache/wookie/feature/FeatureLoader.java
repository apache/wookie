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
package org.apache.wookie.feature;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.wookie.beans.ServerFeature;
import org.apache.wookie.util.hibernate.DBManagerFactory;
import org.apache.wookie.util.hibernate.IDBManager;
import org.apache.wookie.w3c.util.IRIValidator;

import java.util.Iterator;

/**
 * Loads installed features when server is initialized
 *
 */
public class FeatureLoader {

	static Logger _logger = Logger.getLogger(FeatureLoader.class.getName());

	/**
	 * Loads features based on the properties configuration supplied
	 * @param config
	 */
	@SuppressWarnings("unchecked")
	public static void loadFeatures(PropertiesConfiguration config){
		final IDBManager dbManager = DBManagerFactory.getDBManager();
		dbManager.beginTransaction();
		
		// Remove existing features
		for (ServerFeature sf: ServerFeature.findAll()){
			sf.delete();
		}
		
		// Add features in properties configuration
		Iterator i = config.getKeys();
		while (i.hasNext()){
			String klass = (String) i.next();
			String name = config.getString(klass);
			try {
				ServerFeature sf = createFeature(name, klass);
				// Only install it if there isn't an existing
				// feature with the same name
				if (ServerFeature.findByName(name) == null){
					sf.save();
					_logger.info("Installed feature:"+name);					
				} else {
					_logger.error("Error installing feature: "+name+" was already installed");
				}

			} catch (Exception e) {
				_logger.error("Error installing feature:"+e.getMessage());
			}
		}
		dbManager.commitTransaction();
	}

	/**
	 * Returns a valid ServerFeature for the supplied parameters, or throws
	 * an exception if the feature specified is not a valid ServerFeature.
	 * @param name the name of the feature, which must be a valid IRI
	 * @param klass the Class name of the feature, which must implement the IFeature interface
	 */
	@SuppressWarnings("unchecked")
	public static ServerFeature createFeature(String name, String klass) throws Exception{
		// Are required parameters missing?
		if (name == null || klass == null){
			throw new Exception("Invalid feature");
		}
		// Does the class exist?
		Class theClass;
		try {
			theClass = Class.forName(klass);
		} catch (Exception e) {
			throw new Exception("Invalid feature: class not found");
		}
		// Does the class implement IFeature?
		boolean implementsFeature = false;
		Class[] interfaces = theClass.getInterfaces();
		if (interfaces != null) {
			if (interfaces.length > 0){
				if (interfaces[0].getName().equals("org.apache.wookie.feature.IFeature")) implementsFeature = true;
			}
		}
		if (!implementsFeature) throw new Exception("Invalid feature: class is not a Feature class");
		
		// Does the feature name match that in the class?
		if (!((IFeature) theClass.newInstance()).getName().equals(name)) throw new Exception("Invalid feature: feature name supplied and name in the class do not match");;

		// Is the feature name a valid IRI?
		if (!IRIValidator.isValidIRI(name)){
			throw new Exception("Invalid feature: name is not a valid IRI");			
		}
		
		// All is well, create the SF and return it
		ServerFeature sf = new ServerFeature();
		sf.setClassName(klass);
		sf.setFeatureName(name);
		return sf;
	}

}
