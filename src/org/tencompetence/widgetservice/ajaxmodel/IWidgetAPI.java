package org.tencompetence.widgetservice.ajaxmodel;

public interface IWidgetAPI {	
	public String preferenceForKey(String id_key, String key);
	public String setPreferenceForKey(String id_key, String key, String value);
	public String sharedDataForKey(String id_key, String key);
	public String setSharedDataForKey(String id_key, String key, String value);
	public String appendSharedDataForKey(String id_key, String key, String value);
	public String userPropertyForKey(String id_key, String key);
	public String setUserPropertyForKey(String id_key, String key, String value);
	public String contextPropertyForKey(String id_key, String key);
	public String setContextPropertyForKey(String id_key, String key, String value);
	public String lock(String id_key);
	public String unlock(String id_key);
	public String openURL(String url);	
	public String system(String idkey, String arg1, String arg2);
}
