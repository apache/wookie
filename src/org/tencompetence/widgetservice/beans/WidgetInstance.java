package org.tencompetence.widgetservice.beans;
/**
 * WidgetInstance - a simple bean to model an actual widgets instance attributes
 * 
 * @author Paul Sharples
 * @version $Id: WidgetInstance.java,v 1.1 2007-10-14 10:58:31 ps3com Exp $
 */
public class WidgetInstance extends AbstractKeyBean {

		private static final long serialVersionUID = 1L;
		
		private String userId;
		private String runId;
		private String envId;
		private String serviceId;
		private String nonce;
		private String idKey; // aka SHAKey
		private boolean updated;
		private boolean shown;
		private boolean hidden;
		private Widget widget;

		public WidgetInstance(){}
		
		
		public Widget getWidget() {
			return widget;
		}
		
		public void setWidget(Widget widget) {
			this.widget = widget;
		}
		
		public String getEnvId() {
			return envId;
		}
		
		public void setEnvId(String envId) {
			this.envId = envId;
		}
		
		public boolean isHidden() {
			return hidden;
		}
		
		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}
		
		public String getIdKey() {
			return idKey;
		}
		
		public void setIdKey(String idKey) {
			this.idKey = idKey;
		}
		
		public String getNonce() {
			return nonce;
		}
		
		public void setNonce(String nonce) {
			this.nonce = nonce;
		}
		
		public String getRunId() {
			return runId;
		}
		
		public void setRunId(String runId) {
			this.runId = runId;
		}
		
		public boolean isShown() {
			return shown;
		}
		
		public void setShown(boolean shown) {
			this.shown = shown;
		}
		
		public boolean isUpdated() {
			return updated;
		}
		
		public void setUpdated(boolean updated) {
			this.updated = updated;
		}
		
		public String getUserId() {
			return userId;
		}
		
		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getServiceId() {
			return serviceId;
		}

		public void setServiceId(String serviceId) {
			this.serviceId = serviceId;
		}

}
