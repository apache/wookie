package org.apache.wookie.services.impl.jpa;

import org.apache.log4j.Logger;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.services.PreferencesService;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.w3c.IAuthor;
import org.apache.wookie.w3c.IContent;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.IIcon;
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.IParam;
import org.apache.wookie.w3c.W3CWidget;

public class JpaWidgetMetadataService implements WidgetMetadataService {
	
	static Logger _logger = Logger.getLogger(JpaWidgetMetadataService.class.getName());


	@Override
	public IWidget importWidget(W3CWidget model, String packagePath) {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget widget = createWidget(persistenceManager, model);
		createAuthor(persistenceManager, model, widget);
		createStartFiles(persistenceManager, model,widget);
		createNames(persistenceManager, model,widget);
		createDescriptions(persistenceManager, model,widget);
		createIcons(persistenceManager, model, widget);
		createLicenses(persistenceManager, model,widget);		
		createPreferences(persistenceManager, model,widget);
		createFeatures(persistenceManager, model,widget);
        persistenceManager.save(widget);
		
		_logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully imported into the system as "+widget.getLocalName("en"));
    
		return widget;	 
	}

	@Override
	public void removeWidget(IWidget widget) {

		if(widget==null) return;
		
		String widgetGuid = widget.getIdentifier();
		String widgetName = widget.getLocalName("en");
		
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		
		// remove any widget instances for this widget
		IWidgetInstance[] instances = persistenceManager.findByValue(IWidgetInstance.class, "widget", widget);	
		for(IWidgetInstance instance : instances){
			
			// Delete all participants and shared data associated with any instances
			//
			// Note that we don't call this within WidgetInstanceFactory.destroy() as 
			// if called in a different context (to remove just one instance) it would 
			// have the side effect of deleting participants and shared data from other instances,
			// not just the one being deleted.
			//
			// Note also that we have to use the instance as the hook for removing participants as there is no
			// specific query for getting participants for a widget.
			//		
			
			//TODO
			//IParticipant[] participants = new SharedContext(instance).getParticipants();
			//persistenceManager.delete(participants);
	        //ISharedData[] sharedData = new SharedContext(instance).getSharedData();
	        //persistenceManager.delete(sharedData);
	        
			// remove any preferences
			PreferencesService.Factory.getInstance().removePreferences(instance.getOpensocialToken());
			
			// TODO
			// remove the instance
			// WidgetInstanceFactory.destroy(instance);
			
		}
        
		// remove the widget db entry itself
		persistenceManager.delete(widget);
		
    _logger.info("'"+widgetName+"' - " + "Widget was successfully deleted from the system.");
	} 

	@Override
	public void updateWidget(IWidget widget, W3CWidget model) {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		   
		widget.setDir(model.getDir());
        widget.setLang(model.getLang());
        widget.setDefaultLocale(model.getDefaultLocale());
		
        //
        // Don't override the identifier - see WOOKIE-383
        //
        //widget.setIdentifier(model.getIdentifier());
        
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdateLocation());
		
		// Clear old values
		widget.setContentList(null);
		widget.setNames(null);
		widget.setDescriptions(null);
		widget.setLicenses(null);
		widget.setFeatures(null);	
		widget.setIcons(null);
		widget.setPreferences(null);

		// Set with updated values
		createAuthor(persistenceManager, model,widget);
		createStartFiles(persistenceManager, model,widget);
		createNames(persistenceManager, model,widget);
		createDescriptions(persistenceManager, model,widget);
		createIcons(persistenceManager, model, widget);
		createLicenses(persistenceManager, model,widget);		
		createPreferences(persistenceManager, model,widget);
		createFeatures(persistenceManager, model,widget);
        persistenceManager.save(widget);

		_logger.info("'"+model.getLocalName("en") +"' - " + "Widget was successfully updated in the system.");
	}
	
	private static IWidget createWidget(IPersistenceManager persistenceManager, W3CWidget model){
		IWidget widget;
		widget = persistenceManager.newInstance(IWidget.class);		
		widget.setDir(model.getDir());
		widget.setLang(model.getLang());
		widget.setDefaultLocale(model.getDefaultLocale());
		widget.setIdentifier(model.getIdentifier());
		widget.setHeight(model.getHeight());
		widget.setWidth(model.getWidth());
		widget.setVersion(model.getVersion());
		widget.setUpdateLocation(model.getUpdateLocation());
		return widget;
	}
	
	private static void createAuthor(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
	   if (model.getAuthor() != null){
	      IAuthor author = persistenceManager.newInstance(IAuthor.class);
	      author.setAuthorName(model.getAuthor().getAuthorName());
	      author.setEmail(model.getAuthor().getEmail());
	      author.setHref(model.getAuthor().getHref());
	      author.setDir(model.getAuthor().getDir());
	      author.setLang(model.getAuthor().getLang());
	      widget.setAuthor(author);
	    }
	}

	private static void createStartFiles(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IContent page:model.getContentList()){
			IContent start = persistenceManager.newInstance(IContent.class);
			start.setCharSet(page.getCharSet());
			start.setLang(page.getLang());
			start.setSrc(page.getSrc());
            widget.getContentList().add(start);
		}
	}

	private static void createNames(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IName name:model.getNames()){
			IName widgetName = persistenceManager.newInstance(IName.class);
			widgetName.setLang(name.getLang());
			widgetName.setDir(name.getDir());
			widgetName.setName(name.getName());
			widgetName.setShort(name.getShort());
            widget.getNames().add(widgetName);
            persistenceManager.save(widget);
		}
	}

	private static void createDescriptions(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for (IDescription desc:model.getDescriptions()){
			IDescription widgetDesc = persistenceManager.newInstance(IDescription.class);
			widgetDesc.setDescription(desc.getDescription());
			widgetDesc.setLang(desc.getLang());
			widgetDesc.setDir(desc.getDir());
            widget.getDescriptions().add(widgetDesc);
		} 
	}

	private static void createIcons(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IIcon icon: model.getIcons()){
            IIcon widgetIcon = persistenceManager.newInstance(IIcon.class);
            widgetIcon.setSrc(icon.getSrc());
            widgetIcon.setHeight(icon.getHeight());
            widgetIcon.setWidth(icon.getWidth());
            widgetIcon.setLang(icon.getLang());
            widget.getIcons().add(widgetIcon);
		}
	}

	private static void createLicenses(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(ILicense licenseModel: model.getLicenses()){
            ILicense license = persistenceManager.newInstance(ILicense.class);
            license.setLicenseText(licenseModel.getLicenseText());
            license.setHref(licenseModel.getHref());
            license.setLang(licenseModel.getLang());
            license.setDir(licenseModel.getDir());
            widget.getLicenses().add(license);
		}
	}

	private static void createPreferences(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(org.apache.wookie.w3c.IPreference pref : model.getPreferences()){
			org.apache.wookie.w3c.IPreference preferenceDefault = persistenceManager.newInstance(org.apache.wookie.w3c.IPreference.class);
			preferenceDefault.setName(pref.getName());
			preferenceDefault.setValue(pref.getValue());
			preferenceDefault.setReadOnly(pref.isReadOnly());
            widget.getPreferences().add(preferenceDefault);
		}
	}

	private static void createFeatures(IPersistenceManager persistenceManager, W3CWidget model, IWidget widget){
		for(IFeature ofeature: model.getFeatures()){
            IFeature feature = persistenceManager.newInstance(IFeature.class);
			feature.setName(ofeature.getName());
			feature.setRequired(ofeature.isRequired());
            widget.getFeatures().add(feature);
			// now attach all parameters to this feature.
			for(org.apache.wookie.w3c.IParam oparam : ofeature.getParameters()){
	            IParam param = persistenceManager.newInstance(IParam.class);
				param.setName(oparam.getName());
				param.setValue(oparam.getValue());
	            feature.getParameters().add(param);
			}
		}
	}

	@Override
	public IWidget getWidget(String identifier) {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	    IWidget widget = persistenceManager.findWidgetByGuid(identifier);
	    
	    // attempt to get specific widget by id
	    if (widget == null) {
	      persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	      widget = persistenceManager.findById(IWidget.class, identifier);
	    }
	    
		return widget;
	}

	@Override
	public IWidget[] getAllWidgets() {
	    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidget[] widgets = persistenceManager.findAll(IWidget.class);
		return widgets;
	}

}
