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
package org.apache.wookie.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.wookie.util.html.HtmlCleaner;
import org.junit.Test;


public class HtmlCleanerTest {

	/**
	 * Tests injecting a script
	 */
	@Test
	public void injectScript(){
		HtmlCleaner cleaner = new HtmlCleaner();
		String content = "";
		String out = "";
		StringWriter writer = new StringWriter();
		try {
			cleaner.setReader(new StringReader(content));
			cleaner.injectScript("test.js");
			cleaner.process(writer);
			out = writer.getBuffer().toString();
			assertEquals("<html><head><script type=\"text/javascript\" src=\"test.js\"></script></head><body></body></html>", out);
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * tests that user scripts are placed after injected scripts
	 */
	@Test
	public void injectScriptWithUserScript(){
		HtmlCleaner cleaner = new HtmlCleaner();
		String content = "<head><script type=\"text/javascript\" src=\"user.js\"></script></head>";
		String out = "";
		StringWriter writer = new StringWriter();
		try {
			cleaner.setReader(new StringReader(content));
			cleaner.injectScript("inject.js");
			cleaner.process(writer);
			out = writer.getBuffer().toString();
			assertEquals("<html><head><script type=\"text/javascript\" src=\"inject.js\"></script><script type=\"text/javascript\" src=\"user.js\"></script></head><body></body></html>", out);
		} catch (IOException e) {
			fail();
		}
	}

	/**
	 * tests injecting stylesheet
	 */
	@Test
	public void injectStylesheet(){
		HtmlCleaner cleaner = new HtmlCleaner();
		String content = "";
		String out = "";
		StringWriter writer = new StringWriter();
		try {
			cleaner.setReader(new StringReader(content));
			cleaner.injectStylesheet("test.css");
			cleaner.process(writer);
			out = writer.getBuffer().toString();
			assertEquals("<html><head><link type=\"text/css\" rel=\"stylesheet\" href=\"test.css\" /></head><body></body></html>", out);
		} catch (IOException e) {
			fail();
		}
	}

	@Test (expected = IOException.class)
	public void nullReader() throws IOException{
		HtmlCleaner cleaner = new HtmlCleaner();
		StringWriter writer = new StringWriter();
		cleaner.setReader(null);
		cleaner.injectStylesheet("test.css");
		cleaner.process(writer);
		fail();
	}
	@Test (expected = IOException.class)
	public void nullContentInReader() throws IOException{
		HtmlCleaner cleaner = new HtmlCleaner();
		StringWriter writer = new StringWriter();
		cleaner.setReader(new FileReader("bogus.html"));
		cleaner.injectStylesheet("test.css");
		cleaner.process(writer);
	}

	@Test  (expected = IOException.class)
	public void nullWriter() throws IOException{
		HtmlCleaner cleaner = new HtmlCleaner();
		FileWriter writer = null;
		cleaner.setReader(new StringReader("test"));
		cleaner.injectStylesheet("test.css");
		cleaner.process(writer);
	}


}
