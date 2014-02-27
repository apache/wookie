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
package org.apache.wookie.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.exceptions.UnauthorizedAccessException;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.util.WidgetFormattingUtils;
import org.apache.wookie.w3c.IDescription;
import org.apache.wookie.w3c.IName;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * AJAX controller for widget metadata. This implements the request for metadata on initial Widget load
 * within the browser.
 */
public class MetadataController extends Controller {

	private static final long serialVersionUID = -8594233883227640866L;

	/* (non-Javadoc)
	 * @see org.apache.wookie.controller.Controller#index(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void index(HttpServletRequest request,
			HttpServletResponse response) throws UnauthorizedAccessException,
			IOException {
		
		AuthToken authToken = (AuthToken) request.getAttribute("org.apache.wookie.auth.AuthToken");
		if (authToken == null) throw new UnauthorizedAccessException();

		
		// Get i18n-enabled metadata for the Widget's locale and encode it using unicode control characters.
		
		String locales[] = {authToken.getLang()};
		IWidget widget = WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId());
			
		String author = "";
        String email = "";
        String href = "";
		if (widget.getAuthor() != null){
            if (widget.getAuthor().getAuthorName() != null) author = WidgetFormattingUtils.getEncoded(widget.getAuthor().getDir(), widget.getAuthor().getAuthorName());
	        if (widget.getAuthor().getEmail() != null) email = widget.getAuthor().getEmail();
	        if (widget.getAuthor().getHref() != null) href = widget.getAuthor().getHref();
		}

		String name = "";
		IName iname = (IName)LocalizationUtils.getLocalizedElement(widget.getNames().toArray(new IName[widget.getNames().size()]), locales, widget.getDefaultLocale());
		if (iname != null && iname.getName() != null) name = WidgetFormattingUtils.getEncoded(iname.getDir(), iname.getName());
		String shortName = "";
		if (iname != null && iname.getShort() != null) shortName = WidgetFormattingUtils.getEncoded(iname.getDir(), iname.getShort());
		
		String description = "";
		IDescription idescription = (IDescription)LocalizationUtils.getLocalizedElement(widget.getDescriptions().toArray(new IDescription[widget.getDescriptions().size()]), locales, widget.getDefaultLocale());
		if (idescription != null && idescription.getDescription() != null) description = WidgetFormattingUtils.getEncoded(idescription.getDir(), idescription.getDescription());
		
		String version = "";
		if (widget.getVersion() != null) version = WidgetFormattingUtils.getEncoded(widget.getDir(), widget.getVersion());
		
		String width = "0";
		if (widget.getWidth() != null) width = String.valueOf(widget.getWidth());
		
		String height = "0";
		if (widget.getHeight() != null) height = String.valueOf(widget.getHeight());
		
		// Add in metadata

		try {
			JSONObject map = new JSONObject();
			map.put("id", String.valueOf(widget.getIdentifier()));	//$NON-NLS-1$
			map.put("author", author);	//$NON-NLS-1$
			map.put("authorEmail", email);//$NON-NLS-1$
			map.put("authorHref", href);//$NON-NLS-1$
			map.put("name", name);//$NON-NLS-1$
			map.put("description", description);//$NON-NLS-1$	
			map.put("shortName", shortName); //$NON-NLS-1$
			map.put("version",version);//$NON-NLS-1$
			map.put("width", width);//$NON-NLS-1$
			map.put("height", height);//$NON-NLS-1$
			response.getWriter().write(map.toString());
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

	
	

}
