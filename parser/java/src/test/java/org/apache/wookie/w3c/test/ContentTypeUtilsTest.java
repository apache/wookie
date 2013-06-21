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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.wookie.w3c.util.ContentTypeUtils;
import org.junit.Test;

/**
 * Test cases for content-type handling
 */
public class ContentTypeUtilsTest extends ContentTypeUtils {
	
  /**
   * Test that we can instantiate the ContentTypeUtils class
   */
	@Test
	public void classTest(){
		@SuppressWarnings("unused")
		ContentTypeUtils test = new ContentTypeUtils();
	}
	
	/**
	 * Checking whether null is a supported file type should return False 
	 */
	@Test
	public void Nulls(){
		File nullFile = null;
		String nullString = null;
		assertFalse(ContentTypeUtils.isSupportedImageType(nullFile));
		assertFalse(ContentTypeUtils.isSupportedImageType(nullString));
	}
	
	/**
	 * Checking whether an empty string is a supported file type should return False
	 */
	@Test
	public void Empty(){
		assertFalse(ContentTypeUtils.isSupportedImageType(""));
	}
	
	/**
	 * Test the behaviour of file names with different kinds of dot placement. Only the
	 * final file extension should matter - the utils should ignore any dots elsewhere in
	 * the file name
	 */
	@Test
	public void Dots(){
		assertFalse(ContentTypeUtils.isSupportedImageType(".\\."));
		assertFalse(ContentTypeUtils.isSupportedImageType(".\\."));
		assertFalse(ContentTypeUtils.isSupportedImageType(".\\"));
		assertFalse(ContentTypeUtils.isSupportedImageType("\\."));
		assertFalse(ContentTypeUtils.isSupportedImageType("."));
		assertFalse(ContentTypeUtils.isSupportedImageType(".."));
		assertFalse(ContentTypeUtils.isSupportedImageType("test."));
		assertFalse(ContentTypeUtils.isSupportedImageType(".test"));
		assertTrue(ContentTypeUtils.isSupportedImageType("test.png"));
		assertTrue(ContentTypeUtils.isSupportedImageType(".test.png"));
		assertTrue(ContentTypeUtils.isSupportedImageType("...test.png"));
		assertTrue(ContentTypeUtils.isSupportedImageType(".test.test.png"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.png."));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("test.png")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File(".test.png")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("...test.png")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File(".test.test.png")));
		assertFalse(ContentTypeUtils.isSupportedImageType(new File("test.png.")));
	}

	/**
	 * Tests for positives on supported types
	 */
	@Test
	public void Types(){
		assertTrue(ContentTypeUtils.isSupportedImageType("test.gif"));
		assertTrue(ContentTypeUtils.isSupportedImageType("test.jpg"));
		assertTrue(ContentTypeUtils.isSupportedImageType("test.svg"));
		assertTrue(ContentTypeUtils.isSupportedImageType("test.png"));
		assertTrue(ContentTypeUtils.isSupportedImageType("test.ico"));
	}
	
	/**
	 * Tests for negatives on some definitely unsupported types
	 */
	@Test
	public void Types2(){
		assertFalse(ContentTypeUtils.isSupportedImageType("test.exe"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.mpeg"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.wav"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.html"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.png.exe"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.htm"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.css"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.js"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.xml"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.txt"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.xhtml"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.xht"));
	}
	
	/**
	 * Test with invalid characters in file extensions
	 */
	@Test
	public void InvalidExtensions(){
		assertFalse(ContentTypeUtils.isSupportedImageType("test.1exe"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.p n g"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.p3ng"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.png0"));
		assertFalse(ContentTypeUtils.isSupportedImageType("test.pñg"));
	}
	
	/**
	 * Test the content type sniffer correctly identifies the types of files without an extension
	 * @throws IOException
	 */
	@Test
	public void sniff() throws IOException{
		
		assertEquals("image/bmp", ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_bmp")));
		assertEquals("image/gif", ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_gif")));
		assertEquals("image/jpeg", ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_jpg")));
		assertEquals("image/png", ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_png")));
		assertEquals("image/vnd.microsoft.icon", ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_ico")));
		assertEquals(null, ContentTypeUtils.sniffContentType(new File("parser/java/src-test/icons/icon_exr")));

		assertFalse(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_bmp")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_ico")));
		assertFalse(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_exr")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_gif")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_jpg")));
		assertTrue(ContentTypeUtils.isSupportedImageType(new File("parser/java/src-test/icons/icon_png")));
	}
}
