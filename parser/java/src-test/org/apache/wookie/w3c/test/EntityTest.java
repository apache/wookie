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
 package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.wookie.w3c.IParamEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.impl.AuthorEntity;
import org.apache.wookie.w3c.impl.ContentEntity;
import org.apache.wookie.w3c.impl.DescriptionEntity;
import org.apache.wookie.w3c.impl.FeatureEntity;
import org.apache.wookie.w3c.impl.IconEntity;
import org.apache.wookie.w3c.impl.LicenseEntity;
import org.apache.wookie.w3c.impl.NameEntity;
import org.apache.wookie.w3c.impl.ParamEntity;
import org.apache.wookie.w3c.impl.PreferenceEntity;
import org.apache.wookie.w3c.impl.WidgetManifestModel;
import org.apache.wookie.w3c.util.FormattingUtils;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.jdom.JDOMException;
import org.junit.Test;

/**
 * Tests generic functionality in the w3c.impl package classes, including constructors, getters and setters etc
 */
public class EntityTest {
	
	@Test
	public void author(){
		AuthorEntity author = new AuthorEntity("test","http://test","test@test.net");
		assertEquals("test",author.getAuthorName());
		assertEquals("test@test.net", author.getEmail());
		assertEquals("http://test", author.getHref());
		author.setAuthorName("test2");
		author.setEmail("test2@test.net");
		author.setHref("http://test2");
		assertEquals("test2",author.getAuthorName());
		assertEquals("test2@test.net", author.getEmail());
		assertEquals("http://test2", author.getHref());
	}
	
	@Test
	public void content(){
		ContentEntity content = new ContentEntity("http://test", "UTF-8", "text/html");
		assertEquals("http://test",content.getSrc());
		assertEquals("UTF-8", content.getCharSet());
		assertEquals("text/html", content.getType());
	}
	
	@Test
	public void description(){
		DescriptionEntity desc = new DescriptionEntity("test","en");
		assertEquals("test", desc.getDescription());
		assertEquals("en", desc.getLang());
		desc.setDescription("test2");
		assertEquals("test2", desc.getDescription());
	}
	
	@Test
	public void feature(){
		FeatureEntity feature = new FeatureEntity("http://test",true);
		assertEquals("http://test", feature.getName());
		assertEquals(true, feature.isRequired());
		assertFalse(feature.hasParams());
		ParamEntity param = new ParamEntity("name","value");
		List<IParamEntity> params = new ArrayList<IParamEntity>();
		params.add(param);
		feature = new FeatureEntity("http://test",true,params);
		assertTrue(feature.hasParams());
		feature.setRequired(false);
		assertFalse(feature.isRequired());
		feature.setName("http://test2");
		assertEquals("http://test2",feature.getName());
		ParamEntity param2 = new ParamEntity();
		param2.setName("name2");
		param2.setValue("value2");
		params.add(param2);
		assertEquals("name2", param2.getName());
		assertEquals("value2", param2.getValue());
		feature.setParameters(params);
		assertEquals(2,feature.getParameters().size());
	}
	
	@Test
	public void icon(){
		IconEntity icon = new IconEntity("test.png", 320,200);
		assertEquals(320,icon.getHeight().intValue());
		assertEquals(200,icon.getWidth().intValue());
		icon.setHeight(800);
		assertEquals(800,icon.getHeight().intValue());
		icon.setWidth(400);
		assertEquals(400,icon.getWidth().intValue());
		icon.setHeight(null);
		assertEquals(null, icon.getHeight());
	}
	
	@Test
	public void license(){
		LicenseEntity license = new LicenseEntity("test","http://test","en","ltr");
		assertEquals("test",license.getLicenseText());
		assertEquals("http://test", license.getHref());
		assertEquals("en", license.getLang());
		assertEquals("ltr", license.getDir());
		license.setLicenseText("test2");
		assertEquals("test2", license.getLicenseText());
		license.setHref("http://test2");
		assertEquals("http://test2",license.getHref());
	}
	
	@Test
	public void name(){
		NameEntity name = new NameEntity("test","tst","en");
		assertEquals("test",name.getName());
		assertEquals("tst", name.getShort());
		assertEquals("en", name.getLang());
		name.setName("test2");
		assertEquals("test2", name.getName());
		name.setShort("t2");
		assertEquals("t2", name.getShort());
	}
	
	@Test
	public void preference(){
		PreferenceEntity pref = new PreferenceEntity();
		pref.setReadOnly(true);
		assertTrue(pref.isReadOnly());
		pref.setReadOnly(false);
		assertFalse(pref.isReadOnly());
	}
	
	@Test
	public void widget() throws JDOMException, IOException, BadManifestException{
		WidgetManifestModel widget = new WidgetManifestModel("<widget xmlns=\""+IW3CXMLConfiguration.MANIFEST_NAMESPACE+"\"><name>test</name></widget>",null,null,null,null,null);
		assertNull(widget.getAuthor());
		assertEquals("test",widget.getLocalName("en"));
		assertEquals("floating",widget.getViewModes());
		
		widget = new WidgetManifestModel("<widget xmlns=\""+IW3CXMLConfiguration.MANIFEST_NAMESPACE+"\" viewmodes=\"fullscreen\"></widget>",null,null,null,null,null);
		assertNull(widget.getAuthor());
		assertEquals(IW3CXMLConfiguration.UNKNOWN,widget.getLocalName("en"));
		assertEquals("fullscreen",widget.getViewModes());
	}
	
	@Test
	public void utils(){
	  UnicodeUtils utils = new UnicodeUtils();
	  LocalizationUtils lutils = new LocalizationUtils();
	  FormattingUtils futils = new FormattingUtils();
	  WidgetPackageUtils wputils = new WidgetPackageUtils();
	  assertNotNull(utils);
    assertNotNull(futils);
	  assertNotNull(lutils);
	  assertNotNull(wputils);
	}

}
