/**
 * from http://www.devbistro.com/articles/Java/Password-Encryption
 */
package org.tencompetence.widgetservice.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.tencompetence.widgetservice.exceptions.SystemUnavailableException;

import sun.misc.BASE64Encoder;

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