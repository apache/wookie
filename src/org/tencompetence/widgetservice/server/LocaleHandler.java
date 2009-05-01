package org.tencompetence.widgetservice.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;

/**
 * A class to manage the locales as specified in the widgetserver.properties
 * @author Paul Sharples
 * @version $Id: LocaleHandler.java,v 1.1 2009-05-01 10:40:09 ps3com Exp $
 */
public class LocaleHandler {	
	
	static Logger _logger = Logger.getLogger(LocaleHandler.class.getName());

	private List<Locale> fLocales;
    
    private Locale fDefaultLocale;
    
    private final static String DEFAULT_LANGUGE = "en";
    
    private static LocaleHandler instance = null;
    
    private static Map<Locale, Messages> resourceBundles = null;
    
    private static final String BUNDLE_NAME = "org.tencompetence.widgetservice.messages";
    
    private LocaleHandler() {}

    /**
     * @return 
     */
    public static synchronized LocaleHandler getInstance(){
        if (instance == null)
            instance = new LocaleHandler();
        return instance;
    }
    
    public synchronized void initialize(Configuration configuration){
    	String defaultLocale = configuration.getString("widget.default.locale");
		if(!defaultLocale.equals(null)){
			fDefaultLocale = new Locale(defaultLocale);
		}
		else{
			fDefaultLocale = new Locale(DEFAULT_LANGUGE);
		}
		getLocalesFromConfig(configuration);
		initResourceBundles();
    }
	

	@SuppressWarnings("unchecked")
	private void getLocalesFromConfig(Configuration configuration) {  	
    	List<Locale> localeList = new ArrayList<Locale>();    	
    	List<String> localeStringList = configuration.getList("widget.locales");    	
    	if ((localeStringList != null) && (localeStringList.size() > 0)) {    		    		    		
    		for (final String language : localeStringList) {
    			localeList.add(new Locale(language));
    		}
    	}
    	else {
    		// using default
        	localeList.add(new Locale(DEFAULT_LANGUGE));
    	}    	
    	fLocales = localeList;
    	localeStringList = null;
    }
    
	public List<Locale> getLocales() {
		return fLocales;
	}

	public Locale getDefaultLocale() {
		return fDefaultLocale;
	}
	
	public void showLocales(){
		for(Locale locale : fLocales){
			_logger.debug("locale found:"+locale.toString());
		}
	}
	
	private void initResourceBundles() {
		resourceBundles = new HashMap<Locale, Messages>();
		for (final Locale locale : getLocales()) {
			final ResourceBundle rb = ResourceBundle.getBundle(BUNDLE_NAME, locale);
			resourceBundles.put(locale, new Messages(rb));
		}
	}

	public Messages getResourceBundle(final Locale locale) {
		Messages bundle = resourceBundles.get(locale);
		if (bundle == null) {
			bundle = resourceBundles.get(new Locale(DEFAULT_LANGUGE));			
		}				
		return bundle;
	}
	
}
