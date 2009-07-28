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

/**
 * from http://www.devbistro.com/articles/Java/Password-Encryption
 */
package org.apache.wookie.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.wookie.exceptions.SystemUnavailableException;

import sun.misc.BASE64Encoder;

/**
 * A class for generating digests/hashing
 * @author Paul Sharples
 * @version $Id: HashGenerator.java,v 1.2 2009-07-28 16:05:21 scottwilson Exp $
 */
public final class HashGenerator {
	
	private static HashGenerator _instance;
	
	private final String _defaultAlgorithm = "SHA";
	
	private String _algorithm = _defaultAlgorithm;
	
	private HashGenerator(){}
	
	public synchronized String encrypt(String pText, String algorithm)
	throws SystemUnavailableException {
		_algorithm = algorithm;
		return generateEncryption(pText);
	}
	
	public synchronized String encrypt(String pText)
	throws SystemUnavailableException {
		// always set this back to SHA in case it was chnaged
		_algorithm = _defaultAlgorithm;
		return generateEncryption(pText);
	}
	
	public synchronized String generateEncryption(String plaintext)
			throws SystemUnavailableException {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(_algorithm); 
		} 
		catch (NoSuchAlgorithmException e) {
			throw new SystemUnavailableException(e.getMessage());
		}
		try {
			md.update(plaintext.getBytes("UTF-8")); 
		} 
		catch (UnsupportedEncodingException e) {
			throw new SystemUnavailableException(e.getMessage());
		}

		byte raw[] = md.digest(); //step 4
		String hash = (new BASE64Encoder()).encode(raw); 
		return hash; //step 6
	}

	public static synchronized HashGenerator getInstance(){ 
		
		if (_instance == null) {
			_instance = new HashGenerator();
		}
		return _instance;
	}
}