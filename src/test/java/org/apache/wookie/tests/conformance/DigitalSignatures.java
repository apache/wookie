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
package org.apache.wookie.tests.conformance;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.wookie.tests.helpers.WidgetUploader;
import org.apache.wookie.util.digitalsignature.DigitalSignatureProcessor;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadAuthorSignatureException;
import org.apache.wookie.w3c.exceptions.BadDistributorSignatureException;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Conformance tests for W3C Widget XML Signatures
 */
public class DigitalSignatures{

	private static DigitalSignatureProcessor proc;
	private static W3CWidgetFactory fac;
	
	@BeforeClass
	public static void setup() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException{
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(null, null);
		File schema = new File("src/org/apache/wookie/util/digitalsignature/xmldsig-core-schema.xsd");
		proc = new DigitalSignatureProcessor(keyStore, schema.getPath(), true, false);
		fac = new W3CWidgetFactory();
		fac.setDigitalSignatureParser(proc);
		fac.setOutputDirectory(createTempDirectory().getAbsolutePath());
	}
	
	
	
	@Test(expected=BadDistributorSignatureException.class)
	public void badSignature() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-validation/bad_signature/bad_signature.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void badHash() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-validation/bad_hash/bad_hash.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void changedFile() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-validation/changed_file/changed_file.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test11a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-11/11a/11a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test11b() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-11/11b/11b.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadAuthorSignatureException.class)
	public void test12a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-12/12a/12a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadAuthorSignatureException.class)
	public void test12b() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-12/12b/12b.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test13a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-13/13a/13a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test13b() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-13/13b/13b.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test16c() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-16/16c/16c.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test16e() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-16/16e/16e.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test(expected=BadDistributorSignatureException.class)
	public void test16f() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-16/16f/16f.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test(expected=BadDistributorSignatureException.class)
	public void test16g() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-16/16g/16g.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test
	public void test24a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-24/24a/24a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test(expected=BadDistributorSignatureException.class)
	public void test29a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-29/29a/29a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test
	public void test33a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-33/33a/33a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
	@Test(expected=BadDistributorSignatureException.class)
	public void test34a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-34/34a/34a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test
	public void test35a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-35/35a/35a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			e.printStackTrace();
			throw e.getCause();
		}
	}
	@Test(expected=BadDistributorSignatureException.class)
	public void test37a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-37/37a/37a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test(expected=BadDistributorSignatureException.class)
	public void test37b() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-37/37b/37b.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	@Test
	public void test40a() throws Throwable{
		try {
			File file = WidgetUploader.downloadWidget("http://dev.w3.org/2006/waf/widgets-digsig/test-suite/test-cases/ta-40/40a/40a.wgt");
			fac.parse(file);
		} catch (BadManifestException e) {
			throw e.getCause();
		}
	}
	
    /*
     * Utility method for creating a temp directory
     * @return a new temp directory
     * @throws IOException
     */
	private static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
	}
	
}


