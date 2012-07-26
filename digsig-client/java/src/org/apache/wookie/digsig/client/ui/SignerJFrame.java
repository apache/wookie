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
package org.apache.wookie.digsig.client.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.wookie.digsig.client.model.SignWidgets;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignatureException;

/** @author Pushpalanka */

/** Provide a GUI for the user submit the needed to details to sign a widget */
public class SignerJFrame extends javax.swing.JFrame {

        private static final long serialVersionUID = 1L;
        SignWidgets signWidgets; // private?
		private String role;

		/** Creates new form SignerJFrame */
		public SignerJFrame(SignWidgets signWidgets) {
				initComponents();
				this.signWidgets = signWidgets;
		}

		/** This method is called from within the constructor to initialize the form. */
		private void initComponents() {

				buttonGroup1 = new ButtonGroup();
				jPanel1 = new JPanel();
				jLabel1 = new JLabel();
				authorRadioButton1 = new JRadioButton();
				distributorRadioButton2 = new JRadioButton();
				jLabel2 = new JLabel();
				jLabel3 = new JLabel();
				jLabel4 = new JLabel();
				jLabel5 = new JLabel();
				jLabel6 = new JLabel();
				jLabel7 = new JLabel();
				kSTextField = new JTextField();
				kSPasswordField = new JPasswordField();
				pKAlias = new JTextField();
				pKPasswordField = new JPasswordField();
				certificateAliasText = new JTextField();
				filesScrollPane = new JScrollPane();
				jTextArea1 = new JTextArea();
				signButton = new JButton();
				browseButton1 = new JButton();
				browseButton2 = new JButton();
				jLabel8 = new JLabel();
				jLabel9 = new JLabel();
				widgetTextField1 = new JTextField();
				widgetName = new JTextField();
				jLabel10 = new JLabel();

				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				setBackground(new Color(228, 222, 145));
				setBounds(new Rectangle(350, 100, 0, 0));

				jPanel1.setBackground(new Color(245, 240, 181));

				jLabel1.setIcon(new ImageIcon(
						"digsig-client/java/resources/logo.png")); // NOI18N

				authorRadioButton1.setBackground(new Color(245, 240, 181));
				buttonGroup1.add(authorRadioButton1);
				authorRadioButton1.setSelected(true);
				authorRadioButton1.setText("Author");

				distributorRadioButton2.setBackground(new Color(245, 240, 181));
				buttonGroup1.add(distributorRadioButton2);
				distributorRadioButton2.setText("Distributor");

				jLabel2.setText("Keystore file:");

				jLabel3.setText("Keystore Password:");

				jLabel4.setText("Private Key Alias:");

				jLabel5.setText("Private Key Password:");

				jLabel6.setText("Certificate Alias:");

				jLabel7.setText("Following files will be signed");

				pKPasswordField.setToolTipText("Keep blank to use as same as keystore");

				certificateAliasText.setToolTipText("Keep blank to use as same as key");

				jTextArea1.setColumns(20);
				jTextArea1.setEditable(false);
				jTextArea1.setRows(5);
				filesScrollPane.setViewportView(jTextArea1);

				signButton.setBackground(new java.awt.Color(100, 40, 0));
				signButton.setForeground(new java.awt.Color(255, 255, 255));
				signButton.setText("Sign");
				signButton.setFocusPainted(true);
				signButton.setBorderPainted(true);
				signButton.setPreferredSize(new java.awt.Dimension(67, 23));
				signButton.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
								try {
										signButtonMouseClicked(evt);
								}
								catch(Exception e) {
										e.printStackTrace();
								}
						}
				});

				browseButton1.setText("Browse");
				browseButton1.setBackground(new java.awt.Color(228, 222, 145));
				browseButton1.setFocusPainted(true);
				browseButton1.setBorderPainted(true);
				browseButton1.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
								browseButton1MouseClicked(evt);
						}
				});

				browseButton2.setText("Browse");
				browseButton2.setBackground(new java.awt.Color(228, 222, 145));
				browseButton2.setFocusPainted(true);
				browseButton2.setBorderPainted(true);
				browseButton2.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
								browseButton2MouseClicked(evt);
						}
				});

				jLabel8.setFont(new Font("Garamond", 1, 24));
				jLabel8.setForeground(new Color(113, 70, 20));
				jLabel8.setHorizontalAlignment(SwingConstants.CENTER);
				jLabel8.setText("Widget Signer");

				jLabel9.setText("Path to Widget:");

				jLabel10.setText("Widget Name:");

				GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1Layout
						.setHorizontalGroup(jPanel1Layout
								.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(
										jPanel1Layout
												.createSequentialGroup()
												.addContainerGap()
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(jLabel2).addComponent(jLabel3)
																.addComponent(jLabel4).addComponent(jLabel5)
																.addComponent(jLabel6).addComponent(jLabel9)
																.addComponent(jLabel10))
												.addGap(12, 12, 12)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addGroup(
																		jPanel1Layout.createSequentialGroup()
																				.addComponent(jLabel7).addContainerGap())
																.addGroup(
																		jPanel1Layout
																				.createSequentialGroup()
																				.addGroup(
																						jPanel1Layout
																								.createParallelGroup(
																										GroupLayout.Alignment.TRAILING)
																								.addGroup(
																										GroupLayout.Alignment.LEADING,
																										jPanel1Layout
																												.createSequentialGroup()
																												.addGap(2, 2, 2)
																												.addGroup(
																														jPanel1Layout
																																.createParallelGroup(
																																		GroupLayout.Alignment.LEADING)
																																.addGroup(
																																		jPanel1Layout
																																				.createSequentialGroup()
																																				.addComponent(
																																						authorRadioButton1)
																																				.addGap(79,
																																						79, 79)
																																				.addComponent(
																																						distributorRadioButton2))
																																.addComponent(
																																		jLabel8,
																																		GroupLayout.PREFERRED_SIZE,
																																		227,
																																		GroupLayout.PREFERRED_SIZE))
																												.addPreferredGap(
																														LayoutStyle.ComponentPlacement.RELATED,
																														39, Short.MAX_VALUE)
																												.addComponent(jLabel1))
																								.addGroup(
																										jPanel1Layout
																												.createSequentialGroup()
																												.addGroup(
																														jPanel1Layout
																																.createParallelGroup(
																																		GroupLayout.Alignment.LEADING)
																																.addComponent(
																																		filesScrollPane,
																																		GroupLayout.DEFAULT_SIZE,
																																		382,
																																		Short.MAX_VALUE)
																																.addComponent(
																																		widgetTextField1,
																																		GroupLayout.DEFAULT_SIZE,
																																		382,
																																		Short.MAX_VALUE)
																																.addComponent(
																																		kSTextField,
																																		GroupLayout.Alignment.TRAILING,
																																		GroupLayout.DEFAULT_SIZE,
																																		382,
																																		Short.MAX_VALUE)
																																.addGroup(
																																		jPanel1Layout
																																				.createParallelGroup(
																																						GroupLayout.Alignment.TRAILING,
																																						false)
																																				.addComponent(
																																						certificateAliasText,
																																						GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						pKPasswordField,
																																						GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						pKAlias,
																																						GroupLayout.Alignment.LEADING)
																																				.addComponent(
																																						kSPasswordField,
																																						GroupLayout.Alignment.LEADING,
																																						GroupLayout.PREFERRED_SIZE,
																																						211,
																																						GroupLayout.PREFERRED_SIZE))
																																.addComponent(
																																		widgetName,
																																		GroupLayout.PREFERRED_SIZE,
																																		212,
																																		GroupLayout.PREFERRED_SIZE))
																												.addGap(29, 29, 29)
																												.addGroup(
																														jPanel1Layout
																																.createParallelGroup(
																																		GroupLayout.Alignment.LEADING)
																																.addComponent(
																																		signButton,
																																		GroupLayout.DEFAULT_SIZE,
																																		GroupLayout.DEFAULT_SIZE,
																																		Short.MAX_VALUE)
																																.addGroup(
																																		jPanel1Layout
																																				.createParallelGroup(
																																						GroupLayout.Alignment.TRAILING,
																																						false)
																																				.addComponent(
																																						browseButton1,
																																						GroupLayout.DEFAULT_SIZE,
																																						GroupLayout.DEFAULT_SIZE,
																																						Short.MAX_VALUE)
																																				.addComponent(
																																						browseButton2,
																																						GroupLayout.DEFAULT_SIZE,
																																						GroupLayout.DEFAULT_SIZE,
																																						Short.MAX_VALUE)))))
																				.addGap(24, 24, 24)))));
				jPanel1Layout
						.setVerticalGroup(jPanel1Layout
								.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addGroup(
										jPanel1Layout
												.createSequentialGroup()
												.addContainerGap()
												.addGroup(
														jPanel1Layout
																.createParallelGroup(
																		GroupLayout.Alignment.TRAILING, false)
																.addGroup(
																		jPanel1Layout
																				.createSequentialGroup()
																				.addComponent(jLabel8,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						Short.MAX_VALUE)
																				.addGap(18, 18, 18)
																				.addGroup(
																						jPanel1Layout
																								.createParallelGroup(
																										GroupLayout.Alignment.BASELINE)
																								.addComponent(authorRadioButton1)
																								.addComponent(
																										distributorRadioButton2)))
																.addComponent(jLabel1))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.BASELINE)
																.addComponent(jLabel2)
																.addComponent(browseButton1)
																.addComponent(kSTextField,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(jLabel3)
																.addComponent(kSPasswordField,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(jLabel4)
																.addComponent(pKAlias, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(jLabel5)
																.addComponent(pKPasswordField,
																		GroupLayout.PREFERRED_SIZE, 20,
																		GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addComponent(jLabel6)
																.addComponent(certificateAliasText,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.LEADING)
																.addGroup(
																		jPanel1Layout
																				.createParallelGroup(
																						GroupLayout.Alignment.BASELINE)
																				.addComponent(browseButton2)
																				.addComponent(widgetTextField1,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE))
																.addComponent(jLabel9))
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addGroup(
														jPanel1Layout
																.createParallelGroup(GroupLayout.Alignment.TRAILING)
																.addGroup(
																		jPanel1Layout
																				.createSequentialGroup()
																				.addGroup(
																						jPanel1Layout
																								.createParallelGroup(
																										GroupLayout.Alignment.BASELINE)
																								.addComponent(widgetName,
																										GroupLayout.PREFERRED_SIZE,
																										GroupLayout.DEFAULT_SIZE,
																										GroupLayout.PREFERRED_SIZE)
																								.addComponent(jLabel10))
																				.addGap(18, 18, 18)
																				.addComponent(jLabel7)
																				.addPreferredGap(
																						LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(filesScrollPane,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE))
																.addComponent(signButton,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE))
												.addContainerGap(22, Short.MAX_VALUE)));

				GroupLayout layout = new GroupLayout(getContentPane());
				getContentPane().setLayout(layout);
				layout.setHorizontalGroup(layout.createParallelGroup(
						GroupLayout.Alignment.LEADING).addComponent(jPanel1,
						GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
				layout.setVerticalGroup(layout.createParallelGroup(
						GroupLayout.Alignment.LEADING).addComponent(jPanel1,
						GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

				pack();
		}

		/** Handle signing when user clicked sign button */
		private void signButtonMouseClicked(MouseEvent evt) throws Exception {
				role = "AuthorSignature";

				String ksPassword = String.valueOf(kSPasswordField.getPassword());
				String pkPassword = "";
				String pKAliasText = pKAlias.getText();
				String certificateAlias = "";
				if(distributorRadioButton2.isSelected()){
						role = "DistributorSignature";
				}

				//If private key password is not filled in, use keystore password as default
				if(String.valueOf(pKPasswordField.getPassword()).equals("")){
						pkPassword = ksPassword;
				}
				else{
						pkPassword = String.valueOf(pKPasswordField.getPassword());
				}

				//If certificate alias is not filled in, use private key alias as default
				if(certificateAliasText.getText().equals("")){
						certificateAlias = pKAliasText;
				}
				else{
						certificateAlias = certificateAliasText.getText();
				}

				signWidgets.setRole(role);
				signWidgets.setKeyStoreFile(kSTextField.getText());
				signWidgets.setKeyStorePass(ksPassword);
				signWidgets.setPrivateKeyAlias(pKAliasText);
				signWidgets.setPrivateKeyPass(pkPassword);
				signWidgets.setCertificateAlias(certificateAlias);
				signWidgets.setDocumentsList(result);
				signWidgets.setPathToWidget(widgetTextField1.getText());
				signWidgets.setName(widgetName.getText());
				try {
						signWidgets.sign();
				}

				catch(NoSuchAlgorithmException e) {
						JOptionPane.showMessageDialog(this, "Incorrect algorithm! Use RSA-256.",
								"Error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						kSTextField.setText("");
						return;
				}
				catch(CertificateException e) {
						JOptionPane.showMessageDialog(this, "Provided certificate has problems.",
								"Error", JOptionPane.ERROR_MESSAGE);
						kSTextField.setText("");
						kSPasswordField.setText("");
						certificateAliasText.setText("");
						e.printStackTrace();
						return;
				}
				catch(UnrecoverableKeyException e) {
						JOptionPane.showMessageDialog(this, "A given password is incorrect.",
								"Error", JOptionPane.ERROR_MESSAGE);
						kSPasswordField.setText("");
						e.printStackTrace();
						return;
				}
				catch(ParserConfigurationException e) {
						JOptionPane.showMessageDialog(this, "Error in creating the signature",
								"Error", JOptionPane.ERROR_MESSAGE);
						kSPasswordField.setText("");
						e.printStackTrace();
						return;
				}
				catch(NullPointerException e) {
						JOptionPane.showMessageDialog(this, "Error with the given aliases.",
								"Error", JOptionPane.ERROR_MESSAGE);
						pKAlias.setText("");
						certificateAliasText.setText("");
						e.printStackTrace();
						return;
				}
				catch(XMLSignatureException e) {
						JOptionPane.showMessageDialog(this, "Expect a RSA Private Key.", "Error",
								JOptionPane.ERROR_MESSAGE);
						kSTextField.setText("");
						kSPasswordField.setText("");
						e.printStackTrace();
						return;
				}
				catch(XMLSecurityException e) {
						JOptionPane.showMessageDialog(this,
								"Error in generating signature using transforms.", "Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						return;
				}
				catch(IOException e) {
						JOptionPane.showMessageDialog(this, "Error in accessing files.", "Error",
								JOptionPane.ERROR_MESSAGE);
						kSPasswordField.setText("");
						widgetTextField1.setText("");
						e.printStackTrace();
						return;
				}

				try {
						signWidgets.repackZip();
						System.out.print(".wgt stored.");
				}
				catch(IOException e) {
						JOptionPane.showMessageDialog(this, "Error in creating the .wgt.",
								"Error", JOptionPane.ERROR_MESSAGE);
						kSTextField.setText("");
						kSPasswordField.setText("");
						e.printStackTrace();
				}

				JOptionPane.showMessageDialog(this, "Signed " + widgetName.getText()
						+ " widget" + " saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
		}

		/**
		 * Get the location of keystore file(This needs to be RSA with key length being 4096 bits,
		 * according to W3C widget digsig specification)
		 */
		private void browseButton1MouseClicked(java.awt.event.MouseEvent evt) {
				JFileChooser chooser = new JFileChooser("");
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".jks",
						new String[]{"jks"});
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog((java.awt.Component) null);

				if(returnVal == JFileChooser.APPROVE_OPTION){

						java.io.File inFile = chooser.getSelectedFile();
						kSTextField.setText(inFile.getAbsolutePath());
				}
		}

		/** Get the location of widget to be signed */
		private void browseButton2MouseClicked(java.awt.event.MouseEvent evt) {
				JFileChooser chooser = new JFileChooser("");
				File absoluteWidgetFolder = null;
				jTextArea1.setText("");
				result = "";
				chooser.setMultiSelectionEnabled(false);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = chooser.showOpenDialog((java.awt.Component) null);

				if(returnVal == JFileChooser.APPROVE_OPTION){
						absoluteWidgetFolder = chooser.getSelectedFile();
						widgetTextField1.setText(absoluteWidgetFolder.getAbsolutePath());
						try {
								//So the content that is to be signed for user to check.
								jTextArea1.setText(listFilesForFolder(absoluteWidgetFolder));
						}
						catch(NullPointerException e) {
								JOptionPane.showMessageDialog(this, "No files found to be signed.",
										"Error", JOptionPane.ERROR_MESSAGE);
						}
				}
		}

		/**
		 * List the files and folders inside the widget folder according to the hierarchies
		 *
		 * @param absolutePath : to widget content folder
		 * @return result : list of names of files inside the widget folder
		 */
		private String listFilesForFolder(File absolutePath) {
				String path = widgetTextField1.getText();
				File[] list = absolutePath.listFiles();

				for(final File fileEntry : list){

						//remove files starting with '.'(hidden files) as those should not be included in widget
						if(!fileEntry.getName().startsWith(".")){
								String relativePath = new File(path).toURI()
										.relativize(fileEntry.toURI()).getPath();
								if(fileEntry.isDirectory()){
										listFilesForFolder(fileEntry);
								}

								else{
										//author should not sign any other signature
										if(authorRadioButton1.isSelected()
												&& ((relativePath.contains("signature")) && (relativePath
												.endsWith(".xml")))){

										}
										//distributor should not sign another distributors signature, but author
										else if(distributorRadioButton2.isSelected()
												&& ((relativePath.contains("signature")
												&& (relativePath.endsWith(".xml")) && (!relativePath
												.contains("author"))))){

										}
										else{
												result = result + relativePath + "\n";
										}
								}
						}
				}
				return result;
		}

		/**
		 * @param args
		 *
		 */
		private javax.swing.JRadioButton authorRadioButton1;
		private javax.swing.JRadioButton distributorRadioButton2;
		private javax.swing.JButton browseButton1;
		private javax.swing.JButton browseButton2;
		private javax.swing.ButtonGroup buttonGroup1;
		private javax.swing.JTextField certificateAliasText;
		private javax.swing.JScrollPane filesScrollPane;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel6;
		private javax.swing.JLabel jLabel7;
		private javax.swing.JLabel jLabel8;
		private javax.swing.JLabel jLabel9;
		private javax.swing.JLabel jLabel10;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JTextArea jTextArea1;
		private javax.swing.JTextField widgetName;
		private javax.swing.JPasswordField kSPasswordField;
		private javax.swing.JTextField kSTextField;
		private javax.swing.JTextField pKAlias;
		private javax.swing.JPasswordField pKPasswordField;
		private javax.swing.JButton signButton;
		private javax.swing.JTextField widgetTextField1;
		private String result = "";
}
