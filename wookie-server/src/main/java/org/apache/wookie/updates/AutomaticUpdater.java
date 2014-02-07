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

package org.apache.wookie.updates;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.helpers.WidgetFactory;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.util.W3CWidgetFactoryUtils;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.updates.UpdateUtils;

/** 
 * Instances of this class check for new widget updates, and apply them automatically.
 * There are two configurations - either a single-use updater, or a scheduled updater. In 
 * each case the new AutomaticUpdater will asynchronously perform update actions in a
 * new thread. In the case of a single-use updater, it will terminate when completed;
 * for a scheduled updater, it will continue to check for updates according to the specified
 * frequency (hourly, daily, weekly).
 */
public class AutomaticUpdater {

	static Logger logger = Logger.getLogger(AutomaticUpdater.class.getName());

	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	

	private ServletContext context;
	private boolean onlyUseHttps = false;
	

	/**
	 * Creates a single-use automatic updater, which executes immediately.
	 * @param context
	 * @param onlyUseHttps
	 */
	public AutomaticUpdater(ServletContext context, boolean onlyUseHttps){
		logger.info("Automatic update scheduler starting up");
		this.context = context;
		this.onlyUseHttps = onlyUseHttps;
		scheduler.execute(updater);
	}

	/**
	 * Creates an automatic updater that executes every hour, day or week depending
	 * on the value of the frequency parameter.
	 * @param context
	 * @param onlyUseHttps
	 * @param frequency
	 */
	public AutomaticUpdater(ServletContext context, boolean onlyUseHttps, String frequency){
		logger.info("Automatic update scheduler starting up");
		this.context = context;
		this.onlyUseHttps = onlyUseHttps;
		
		int interval = 1;
		TimeUnit timeUnit = getTimeUnit(frequency);
		if (frequency.equalsIgnoreCase("weekly")) interval = 7; 
		scheduler.scheduleAtFixedRate(updater, 1, interval, timeUnit);
	}

	// FIXME localize messages
	final Runnable updater = new Runnable() {
		public void run() { 
			try {
				logger.info("Checking for updates");

				//
				// Check to see if we're requiring updates over HTTPS - if not output a warning
				//
				if (!onlyUseHttps) logger.warn("checking for updates using non-secure method");
				
				//
				// Get all installed widgets
				//
				IWidget[] widgets = WidgetMetadataService.Factory.getInstance().getAllWidgets();

				//
				// Create a W3CWidget factory for the current context
				//
				W3CWidgetFactory factory  = W3CWidgetFactoryUtils.createW3CWidgetFactory(context);

				//
				// Iterate over the widgets and attempt to install updates
				//
				for (IWidget widget: widgets){
					try {						
						W3CWidget updatedWidget = UpdateUtils.getUpdate(factory, widget.getIdentifier(), widget.getUpdateLocation(), widget.getVersion(), onlyUseHttps);
						if (updatedWidget != null && WidgetMetadataService.Factory.getInstance().getWidget(widget.getIdentifier()) != null){
							WidgetFactory.update(updatedWidget, widget, false, null);
							logger.info("Successfully updated "+widget.getIdentifier()+" to version "+updatedWidget.getVersion());
						}
						
					} catch (Exception e) {
						logger.warn(e.getMessage(), e);
					}
				}
				
			} catch (Exception e) {
				//
				// Log errors, as otherwise the thread will terminate silently
				//
				logger.error("Problem with automatic update", e);
			} finally {
			}
		}
	};
	
	/*
	 * Returns the time unit for the specified frequency; for "hourly" this
	 * is hours; the default otherwise is days
	 * @param name the frequency name
	 * @return a TimeUnit applicable for the frequency
	 */
	private TimeUnit getTimeUnit(String name){
		if (name.equalsIgnoreCase("hourly"))
			return TimeUnit.HOURS;
		return TimeUnit.DAYS;
	}

}
