/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.beans;

/**
 * WidgetInstance - a simple bean to model an actual widgets instance attributes
 * 
 * @author Paul Sharples
 * @version $Id: WidgetInstance.java,v 1.5 2009-02-20 23:51:00 scottwilson Exp $
 */
public class WidgetInstance extends AbstractKeyBean {

		private static final long serialVersionUID = 1L;
		
		private String userId;
		private String sharedDataKey;
		private String nonce;
		private String idKey; // aka SHAKey
		private String opensocialToken; // Token used with OpenSocial gadgets;
		private boolean updated;
		private boolean shown;
		private boolean hidden;
		private boolean locked;
		private Widget widget;

		public WidgetInstance(){}
		
		
		public Widget getWidget() {
			return widget;
		}
		
		public void setWidget(Widget widget) {
			this.widget = widget;
		}
		
		public String getSharedDataKey() {
			return sharedDataKey;
		}

		public void setSharedDataKey(String sharedDataKey) {
			this.sharedDataKey = sharedDataKey;
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

		public String getOpensocialToken() {
			return opensocialToken;
		}

		public void setOpensocialToken(String opensocialToken) {
			this.opensocialToken = opensocialToken;
		}
		
		public String getNonce() {
			return nonce;
		}
		
		public void setNonce(String nonce) {
			this.nonce = nonce;
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

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}

}
