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
package org.apache.wookie.digsig.client.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.ObjectContainer;
import org.apache.xml.security.signature.SignatureProperties;
import org.apache.xml.security.signature.SignatureProperty;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.w3c.dom.Element;

/** @author Pushpalanka */

/**
 * Perform signing over a widget according to the recommendations specified
 * in W3C widgets digsig specification
 */
public class SignWidgets {

		private String keyStoreType = "JKS";
		private String keyStoreFile = "";
		private String keyStorePass = "";
		private String privateKeyAlias = "";
		private String privateKeyPass = "";
		private String certificateAlias = "";
		private String pathToWidget = "";
		private String documentsList = "";
		private String role = "AuthorSignature";
		private String target = "";
		private String roleURI = "";
		private String name = "";
		private String[] documents;
		private String signatureFileName = "";

		public void setKeyStoreFile(String keyStoreFile) {
				this.keyStoreFile = keyStoreFile;
		}

		public void setKeyStorePass(String keyStorePass) {
				this.keyStorePass = keyStorePass;
		}

		public void setPrivateKeyAlias(String privateKeyAlias) {
				this.privateKeyAlias = privateKeyAlias;
		}

		public void setPrivateKeyPass(String privateKeyPass) {
				this.privateKeyPass = privateKeyPass;
		}

		public void setCertificateAlias(String certificateAlias) {
				this.certificateAlias = certificateAlias;
		}

		public void setRole(String role) {
				this.role = role;
		}

		public void setDocumentsList(String documentsList) {
				this.documentsList = documentsList;
		}

		public void setPathToWidget(String pathToWidget) {
				this.pathToWidget = pathToWidget;
		}

		public void setName(String name) {
				this.name = name;
		}

		static {
				org.apache.xml.security.Init.init();
		}

		/** Generate signature for the widget content under W3C widgets digsig specification */
		public void sign() throws KeyStoreException, IOException,
				NoSuchAlgorithmException, CertificateException,
				UnrecoverableKeyException, XMLSecurityException,
				ParserConfigurationException {

				//set signature file names according to the role as specified in W3C widgets digsig spec
				if(role.equals("AuthorSignature")){
						signatureFileName = "/author-signature.xml";
				}
				else{
						Random randomGenerator = new Random();
						Integer number = randomGenerator.nextInt(20);
						signatureFileName = "/signature" + number + ".xml";
				}

				File signatureFile = new File(pathToWidget + signatureFileName);

				//get the private key from keystore
				KeyStore ks = KeyStore.getInstance(keyStoreType);
				FileInputStream fis = new FileInputStream(keyStoreFile);
				ks.load(fis, keyStorePass.toCharArray());
				PrivateKey privateKey = (PrivateKey) ks.getKey(privateKeyAlias,
						privateKeyPass.toCharArray());

				javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
						.newInstance();
				dbf.setNamespaceAware(true);
				javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.newDocument();

				//BaseURI provides the base for the path to referenced content from the signature
				String BaseURI = signatureFile.toURI().toURL().toString();
				XMLSignature sig = new XMLSignature(doc, BaseURI,
						XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA256,
						Transforms.TRANSFORM_C14N11_OMIT_COMMENTS);
				
				sig.setId(role);

				doc.appendChild(sig.getElement());
				{

						Transforms transforms = new Transforms(doc);

						transforms.addTransform(Transforms.TRANSFORM_C14N11_OMIT_COMMENTS);
						addDocuments(sig, documentsList);

						customizeToRole(role);

						//Adding signature properties
						SignatureProperty profile = new SignatureProperty(doc, target,
								SignerConstants.SIGNATURE_PROPERTY_ID_PROFILE);
						Element profileElement = doc
								.createElement(SignerConstants.SIGNATURE_PROPERTY_ID_PROFILE_TAG_NAME);
						profileElement.setAttribute("URI", SignerConstants.PROFILE_URI);
						profile.appendChild(profileElement);

						SignatureProperty role = new SignatureProperty(doc, target,
								SignerConstants.SIGNATURE_PROPERTY_ID_ROLE);
						Element roleElement = doc
								.createElement(SignerConstants.SIGNATURE_PROPERTY_ID_ROLE_TAG_NAME);
						roleElement.setAttribute("URI", roleURI);
						role.appendChild(roleElement);

						SignatureProperty identifier = new SignatureProperty(doc, target,
								SignerConstants.SIGNATURE_PROPERTY_ID_IDENTIFIER);
						Element identifierElement = doc
								.createElement(SignerConstants.SIGNATURE_PROPERTY_ID_IDENTIFIER_TAG_NAME);
						identifierElement.setTextContent(name);
						identifier.appendChild(identifierElement);

						SignatureProperties props = new SignatureProperties(doc);

						props.setXPathNamespaceContext(SignerConstants.XMLNS_DSP,
								SignerConstants.SIGNATURE_PROPERTIES_NAMESPACE);
						props.addSignatureProperty(profile);
						props.addSignatureProperty(role);
						props.addSignatureProperty(identifier);
						ObjectContainer object = new ObjectContainer(doc);
						object.setId("prop");

						object.appendChild(doc.createTextNode("\n"));
						object.appendChild(props.getElement());
						object.appendChild(doc.createTextNode("\n"));

						//Refer the properties section of the signature which needs
						//to be signed too, that include role information.
						sig.appendObject(object);
						sig.addDocument("#prop", transforms,
								SignerConstants.DIGEST_METHOD_ALGORITHM);

						ResourceResolver offlineResolver = null;
						sig.addResourceResolver(offlineResolver);

						{
								X509Certificate cert = (X509Certificate) ks
										.getCertificate(certificateAlias);
								if(cert.equals(null)){
										throw new NullPointerException();
								}
								sig.addKeyInfo(cert);
								sig.addKeyInfo(cert.getPublicKey());

								System.out.println("Start signing");
								sig.sign(privateKey);
								System.out.println("Finished signing");
						}

						FileOutputStream f = new FileOutputStream(signatureFile);

						XMLUtils.outputDOM(doc, f);
						f.close();
						System.out.println("Wrote signature to " + BaseURI);
				}
		}

		/**
		 * To add the content of the widget as reference in the signature
		 *
		 * @param sig : generating signature
		 * @param documentsList: list of files inside widget folder. All the content of .wgt needs to be signed.
		 */
		private void addDocuments(XMLSignature sig, String documentsList) {
				documents = documentsList.split("\n");
				for(int i = 0; i < documents.length; i++){
						try {
								sig.addDocument(documents[i], null,
										SignerConstants.DIGEST_METHOD_ALGORITHM);
						}
						catch(XMLSignatureException e) {
								e.printStackTrace();
						}
				}
		}

		/**
		 * Set parameters of the signature according to the role of the signer
		 *
		 * @param role: whether signing person is author or distributor
		 */
		private void customizeToRole(String role) {
				if(role.equals("AuthorSignature")){
						target = SignerConstants.SIGN_TARGET_AUTHOR;
						roleURI = SignerConstants.ROLE_URI_AUTHOR;
				}
				else if(role.equals("DistributorSignature")){
						target = SignerConstants.SIGN_TARGET_DISTRIBUTOR;
						roleURI = SignerConstants.ROLE_URI_DISTRIBUTOR;
				}
				else{

				}
		}

		/** Pack the content into a .wgt package */
		public void repackZip() throws IOException {
				File sourceOfFiles = new File(pathToWidget);
				ZipArchiveOutputStream out = new ZipArchiveOutputStream(new File(
						sourceOfFiles.getAbsolutePath() + File.separator + name + ".wgt"));
				out.setEncoding("UTF-8");
				for(File aFile : sourceOfFiles.listFiles()){
						if(!aFile.getName().endsWith(".wgt")){
								pack(aFile, out, "");
						}
				}
				out.flush();
				out.close();
		}

		/**
		 * Recursively locates and adds files and folders to a zip archive
		 *
		 * @param file: directory that includes widget content
		 * @param out: ZipArchiveOutputStream
		 * @param path:location of content inside .wgt, relatively
		 */
		private static void pack(File file, ZipArchiveOutputStream out, String path)
				throws IOException {

				// To avoid include of hidden files and folders checking has done on name to
				// start with ".".
				if(file.isDirectory() && !(file.getName().startsWith("."))){
						path = path + file.getName() + "/";
						for(File aFile : file.listFiles()){
								pack(aFile, out, path);
						}
				}
				else{

						String name = file.getName();
						if(!name.startsWith(".")){
								ZipArchiveEntry entry = (ZipArchiveEntry) out.createArchiveEntry(file,
										path + name);
								out.putArchiveEntry(entry);
								byte[] buf = new byte[1024];
								int len;
								FileInputStream in = new FileInputStream(file);
								while((len = in.read(buf)) > 0){
										out.write(buf, 0, len);
								}
								out.closeArchiveEntry();
						}
				}
		}
}
