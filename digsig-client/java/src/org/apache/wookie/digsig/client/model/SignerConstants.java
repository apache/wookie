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

/** @author Pushpalanka */
public class SignerConstants {

		public static final String SIGN_TARGET_DISTRIBUTOR = "#DistributorSignature";
		public static final String SIGN_TARGET_AUTHOR = "#AuthorSignature";
		public static final String DIGEST_METHOD_ALGORITHM = "http://www.w3.org/2001/04/xmlenc#sha256";
		public static final String PROFILE_URI = "http://www.w3.org/ns/widgets-digsig#profile";
		public static final String ROLE_URI_DISTRIBUTOR = "http://www.w3.org/ns/widgets-digsig#role-distributor";
		public static final String ROLE_URI_AUTHOR = "http://www.w3.org/ns/widgets-digsig#role-author";
		public static final String SIGNATURE_PROPERTIES_NAMESPACE = "http://www.w3.org/2009/xmldsig-properties";
		public static final String SIGNATURE_PROPERTY_ID_ROLE = "role";
		public static final String SIGNATURE_PROPERTY_ID_PROFILE = "profile";
		public static final String SIGNATURE_PROPERTY_ID_IDENTIFIER = "identifier";
		public static final String SIGNATURE_PROPERTY_ID_ROLE_TAG_NAME = "dsp:Role";
		public static final String SIGNATURE_PROPERTY_ID_PROFILE_TAG_NAME = "dsp:Profile";
		public static final String SIGNATURE_PROPERTY_ID_IDENTIFIER_TAG_NAME = "dsp:Identifier";
		public static final String XMLNS_DSP = "xmlns:dsp";
}
