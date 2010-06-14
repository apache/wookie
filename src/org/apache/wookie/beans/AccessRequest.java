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
package org.apache.wookie.beans;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class AccessRequest extends AbstractKeyBean<AccessRequest> {
	
	private static final long serialVersionUID = -1205834267479594166L;
	
	private String origin;
	private boolean subdomains;
	private Widget widget;
	private boolean granted;
	
	public boolean isGranted() {
		return granted;
	}
	public void setGranted(boolean granted) {
		this.granted = granted;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public boolean isSubdomains() {
		return subdomains;
	}
	public void setSubdomains(boolean subdomains) {
		this.subdomains = subdomains;
	}
	public Widget getWidget() {
		return widget;
	}
	public void setWidget(Widget widget) {
		this.widget = widget;
	}
	
	/// Active record methods
	public static AccessRequest findById(Object id){
		return (AccessRequest) findById(AccessRequest.class, id);
	}

	public static AccessRequest[] findByValue(String key, Object value) {
		return (AccessRequest[]) findByValue(AccessRequest.class, key, value);
	}

	@SuppressWarnings("unchecked")
	public static AccessRequest[] findByValues(Map map) {
		return (AccessRequest[]) findByValues(AccessRequest.class, map);
	}
	
	public static AccessRequest[] findAll(){
		return (AccessRequest[]) findAll(AccessRequest.class);
	}

	//// Special Queries
	public static AccessRequest[] findAllApplicable(Widget widget){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("widget", widget);
		map.put("granted", true);
		return findByValues(map);
	}
	
	private URI getOriginAsURI(){
		try {
			return new URI(origin);
		} catch (URISyntaxException e) {
			// origins other than "*" MUST be valid URIs
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Implementation of the W3C WARP algorithm for a single access request
	 * @param requestedUri the URI requested
	 * @return true if this access request grants access, otherwise false
	 */
	public boolean isAllowed(URI requestedUri){
		if (origin.equals("*")) return true;
		URI accessUri = getOriginAsURI();
		// Schemes must match
		if (!accessUri.getScheme().equalsIgnoreCase(requestedUri.getScheme())) return false;
		if (isSubdomains()){
			// Host must match or match with subdomains
			if (!accessUri.getHost().equalsIgnoreCase(requestedUri.getHost()) &&
					!requestedUri.getHost().endsWith("."+accessUri.getHost())) return false;
		} else {
			// Hosts must match
			if (!accessUri.getHost().equalsIgnoreCase(requestedUri.getHost())) return false;
		}
		// Ports must match
		// Default: no port in request, and access policy set to 80
		if (requestedUri.getPort() == -1 && accessUri.getPort() == 80) return true;
		if (accessUri.getPort()==requestedUri.getPort()) return true;
		return false;
	}

}
