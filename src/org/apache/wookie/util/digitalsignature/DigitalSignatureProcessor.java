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
package org.apache.wookie.util.digitalsignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.wookie.w3c.IDigitalSignatureProcessor;
import org.apache.wookie.w3c.exceptions.BadAuthorSignatureException;
import org.apache.wookie.w3c.exceptions.BadDistributorSignatureException;
import org.apache.wookie.w3c.exceptions.InsecuredWidgetContentException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.Element;

/**
 * verify widgets using digital signatures
 * 
 * @author Pushpalanka Jayawardhana
 */
public class DigitalSignatureProcessor implements IDigitalSignatureProcessor {

  static {
    org.apache.xml.security.Init.init();
  }
  static Logger _logger = Logger.getLogger(DigitalSignatureProcessor.class
      .getName());

  private KeyStore keystore;
  // If true, Wookie will not deploy any widgets with invalid digital
  // signatures. If set to false, the widget will be
  // imported and a warning logged.
  private boolean rejectInvalid;
  // If true, Wookie will only deploy Widgets that have valid digital signatures
  // AND that each signature uses a trusted
  // certificate located in the trusted keystore.
  private boolean rejectUntrusted;
  private HashSet<String> filesList = new HashSet<String>();

  public DigitalSignatureProcessor(KeyStore keyStore, boolean rejectInvalid,
      boolean rejectUntrusted) {
    this.keystore = keyStore;
    this.rejectInvalid = rejectInvalid;
    this.rejectUntrusted = rejectUntrusted;
    // to make it default to reject invalid signatures if signatures with
    // untrusted certificate are rejecting
    if (rejectUntrusted) {
      this.rejectInvalid = rejectUntrusted;
    }
  }

  /**
   * Process the signatures inside the widget to verify validity
   * 
   * @param packagePath
   *          : widget package path
   * @throws BadAuthorSignatureException
   * @throws BadDistributorSignatureException
   */
  public void processDigitalSignatures(String packagePath)
      throws BadAuthorSignatureException, BadDistributorSignatureException,
      InsecuredWidgetContentException {

    File widget = new File(packagePath);
    File[] authorSignaturesList = locateAuthorSignatureFiles(widget);
    File[] distributorSignaturesList = locateDistributorSignatureFiles(widget);
    if ((authorSignaturesList.length == 0 && distributorSignaturesList.length == 0)
        && rejectInvalid) {
      throw new InsecuredWidgetContentException(
          "Couldn't locate any signature file inside the widget "
              + widget.getName());
    } else if (authorSignaturesList.length == 0
        && distributorSignaturesList.length == 0) {
      _logger.warn("Couldn't locate any signature file inside the widget "
          + widget.getName() + ", " + "but it will still be imported.");
    } else {
      verifyWidget(authorSignaturesList, distributorSignaturesList, widget);
    }

  }

  /**
   * Verify widgets to be deployed to be valid.
   * 
   * @param authorSignaturesList
   *          : List of author signatures inside widget
   * @param distributorSignaturesList
   *          : List of distributor signatures inside widget
   * @param widget
   *          : to be deployed
   * @return whether verified to be valid
   * @throws BadAuthorSignatureException
   * @throws BadDistributorSignatureException
   */
  private void verifyWidget(File[] authorSignaturesList,
      File[] distributorSignaturesList, File widget)
      throws BadAuthorSignatureException, BadDistributorSignatureException {
    boolean isWidgetValid = false;

    for (File file : authorSignaturesList) {
      try {
        isWidgetValid = isSignatureValid(file, "author", widget);
        if ((!isWidgetValid) && rejectInvalid) {
          throw new InsecuredWidgetContentException(
              "Rejecting invalid author signature " + file.getName());
        }
        if (!isWidgetValid && !rejectInvalid) {
          _logger.warn("The author signature \'" + file.getName()
              + "\' is not valid, but widget will be imported");
        }
      } catch (Exception e) {
        throw new BadAuthorSignatureException("Invalid Author Signature.");
      }
    }

    for (File file : distributorSignaturesList) {
      try {
        isWidgetValid = isSignatureValid(file, "", widget);
        if ((!isWidgetValid) && rejectInvalid) {
          throw new InsecuredWidgetContentException(
              "Rejecting invalid distributor signature " + file.getName());
        }
        if (!isWidgetValid && !rejectInvalid) {
          _logger.warn("The distributor signature \'" + file.getName()
              + "\' is not valid, but widget will be imported");
        }
      } catch (Exception e) {
        throw new BadDistributorSignatureException(
            "Invalid Distributor Signature " + file.getName());
      }
    }

  }

  /**
   * Verify a signature file to be valid inside the widget folder.
   * 
   * @param file
   *          : signature.xml file
   * @param role
   *          : role of the signer: Author/Distributor
   * @param widget
   *          : widget folder to be deployed
   * @return whether signature file is valid inside the folder
   * @throws Exception
   */
  private boolean isSignatureValid(File file, String role, File widget)
      throws Exception {
    boolean schemaValidate = true;
    boolean isValid = false;
    final String signatureSchemaFile = "src/org/apache/wookie/util/digitalsignature/xmldsig-core"
        + "-schema.xsd";

    if (schemaValidate) {
      _logger.debug("Doing schema validation.");
    }

    javax.xml.parsers.DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory
        .newInstance();

    if (schemaValidate) {
      dbf.setAttribute("http://apache.org/xml/features/validation/schema",
          Boolean.TRUE);
      dbf.setAttribute(
          "http://apache.org/xml/features/dom/defer-node-expansion",
          Boolean.TRUE);
      dbf.setValidating(true);
      dbf.setAttribute("http://xml.org/sax/features/validation", Boolean.TRUE);
    }

    dbf.setNamespaceAware(true);
    dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);

    if (schemaValidate) {
      dbf.setAttribute(
          "http://apache.org/xml/properties/schema/external-schemaLocation",
          Constants.SignatureSpecNS + " " + signatureSchemaFile);
    }

    _logger.info("Trying to verify " + file.toURI().toURL().toString());

    javax.xml.parsers.DocumentBuilder db = dbf.newDocumentBuilder();

    db.setErrorHandler(new org.apache.xml.security.utils.IgnoreAllErrorHandler());

    if (schemaValidate) {
      db.setEntityResolver(new org.xml.sax.EntityResolver() {

        public org.xml.sax.InputSource resolveEntity(String publicId,
            String systemId) throws org.xml.sax.SAXException {

          if (systemId.endsWith("xmldsig-core-schema.xsd")) {
            try {
              return new org.xml.sax.InputSource(new FileInputStream(
                  signatureSchemaFile));
            } catch (FileNotFoundException ex) {
              throw new org.xml.sax.SAXException(ex);
            }
          } else {
            return null;
          }
        }
      });
    }

    org.w3c.dom.Document doc = db.parse(new java.io.FileInputStream(file));

    XPathFactory xpf = XPathFactory.newInstance();
    XPath xpath = xpf.newXPath();
    xpath.setNamespaceContext(new DSNamespaceContext());

    String expression = "//ds:Signature[1]";
    Element sigElement = (Element) xpath.evaluate(expression, doc,
        XPathConstants.NODE);
    XMLSignature signature = new XMLSignature(sigElement, file.toURI().toURL()
        .toString());
    if (!isAllContentSigned(signature, role, widget)) {
      return false;
    } else {
      KeyInfo ki = signature.getKeyInfo();

      if (ki != null) {
        if (ki.containsX509Data()) {
          _logger.debug("Could find a X509Data element in the KeyInfo");
        }

        X509Certificate cert = signature.getKeyInfo().getX509Certificate();
        if (!isTrusted(cert) && (rejectUntrusted)) {
          _logger.error("Untrusted certificate submitted with the signature.");
          throw new InsecuredWidgetContentException(
              "Untrusted certificate submitted with the signature.");
        }

        if (cert != null) {
          isValid = signature.checkSignatureValue(cert);
          _logger.debug("The XML signature in file "
              + file.toURI().toURL().toString() + " is "
              + (isValid ? "valid! " : "invalid! "));
        } else {
          _logger.debug("Did not find a Certificate");

          PublicKey pk = signature.getKeyInfo().getPublicKey();

          if (pk != null) {
            _logger.debug("The XML signature in file "
                + file.toURI().toURL().toString() + " is "
                + (signature.checkSignatureValue(pk) ? "valid!" : "invalid!)"));
          } else {
            _logger
                .debug("Did not find a public key, so can't check the signature");
          }
        }
      } else {
        _logger.debug("Did not find a KeyInfo");
      }
    }
    return isValid;
  }

  /**
   * Make a list of author signatures present(According to the W3C spec there is
   * only one author signature inside a widget folder)
   * 
   * @param widget
   * @return
   */
  public File[] locateAuthorSignatureFiles(File widget) {

    return widget.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.contains("author") && name.contains("signature");
      }
    });
  }

  /**
   * Make a list of distributor signatures present
   * 
   * @param widget
   * @return
   */
  private File[] locateDistributorSignatureFiles(File widget) {
    File[] files = widget.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return (!name.contains("author")) && name.contains("signature");
      }
    });
    return files;
  }

  /**
   * Check whether all the content inside the widget folder is signed.If any
   * additions are present except for that are referenced inside the signature,
   * it is considered an invalid widget. Anyway inclusion of signature files is
   * handled according to W3C widget digsig spec
   * 
   * @param signature
   *          : validating signature file
   * @param role
   *          : role of the signer who generated the signature
   * @param widget
   *          : widget folder that is under verification
   * @return : whether all the content is signed
   * @throws org.apache.xml.security.exceptions.XMLSecurityException
   */
  private boolean isAllContentSigned(XMLSignature signature, final String role,
      File widget) throws XMLSecurityException {
    boolean isAllSigned = false;
    HashSet<String> signedFiles = new HashSet<String>();
    for (int i = 0; i < signature.getSignedInfo().getSignedContentLength() - 1; i++) {
      String[] filName = signature.getSignedInfo()
          .getReferencedContentAfterTransformsItem(i).getSourceURI()
          .split("/");
      signedFiles.add(filName[filName.length - 1]);
    }

    HashSet<String> widgetFilesList = new HashSet<String>(listFilesForFolder(
        widget, role));
    filesList.clear();

    widgetFilesList.removeAll(signedFiles);
    if (widgetFilesList.isEmpty()) {
      isAllSigned = true;
    }

    return isAllSigned;
  }

  /**
   * List the files inside the widget folder
   * 
   * @param absolutePath
   * @param role
   * @return
   */
  private HashSet<String> listFilesForFolder(File absolutePath, String role) {
    File[] list = absolutePath.listFiles();

    for (final File fileEntry : list) {
      String name = fileEntry.getName();
      if (!name.startsWith(".")) {
        if (fileEntry.isDirectory()) {
          listFilesForFolder(fileEntry, role);
        }

        else {
          if (role.equals("author")
              && ((name.contains("signature")) && (name.endsWith(".xml")))) {

          } else if (!role.equals("author") && name.contains("signature")
              && !name.contains("author") && (name.endsWith(".xml"))) {

          } else {
            filesList.add(name);
          }
        }
      }
    }
    return filesList;
  }

  /**
   * Check whether the certificate sent with the signature is trusted by the
   * Wookie server
   * 
   * @param cert
   *          : The X.509 certificate sent with the signature
   * @return : trusted
   * @throws KeyStoreException
   * @throws IOException
   * @throws NoSuchAlgorithmException
   * @throws CertificateException
   */
  private boolean isTrusted(X509Certificate cert) throws KeyStoreException {

    String alias = keystore.getCertificateAlias(cert);
    if (!(alias == null)) {
      _logger.debug("Could locate certificate in trusted keystore.");
      return true;

    }
    _logger.debug("Couldn't locate certificate in trusted keystore.");
    return false;
  }

}
