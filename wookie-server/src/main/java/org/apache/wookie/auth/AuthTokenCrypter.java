/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Basic encryption utility class
 * 
 * Using the default constructor, a new token_key file is created with a secure
 * random key if one does not already exist.
 */
public class AuthTokenCrypter {
	// Labels for key derivation
	private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	private static final int HMAC_SHA1_LEN = 20;
	private final static String CIPHER_TYPE = "AES/CBC/PKCS5Padding";
	private final static String CIPHER_KEY_TYPE = "AES";
	private static final byte CIPHER_KEY_LABEL = 0;
	private static final int CIPHER_BLOCK_SIZE = 16;
	private static final byte HMAC_KEY_LABEL = 1;
	private final static String HMAC_TYPE = "HMACSHA1";
	private final static int MIN_HMAC_KEY_LEN = 8;

	/** minimum length of master key */
	public static final int MASTER_KEY_MIN_LEN = 16;

	private byte[] cipherKey;
	private byte[] hmacKey;

	/**
	 * Creates a crypter based on a key in a file. The key is the first line in
	 * the file, whitespace trimmed from either end, as UTF-8 bytes.
	 * 
	 * The following *nix command line will create an excellent key:
	 * 
	 * <pre>
	 * dd if=/dev/random bs=32 count=1  | openssl base64 > /tmp/key.txt
	 * </pre>
	 * 
	 * @throws IOException
	 *             if the file can't be read.
	 */
	private void init(File keyfile) throws IOException {
		BufferedReader reader = null;
		try {
			FileInputStream openFile = new FileInputStream(keyfile);
			reader = new BufferedReader(
					new InputStreamReader(openFile, "UTF-8"));
			init(reader.readLine());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Get a default crypter from the "token_key" file. If no file
	 * exists, generates a new random key and saves it
	 * @throws IOException
	 *             if there is a problem getting or creating a token_key file
	 */
	public AuthTokenCrypter() throws IOException {
		File keyFile = new File("token_key");
		if (!keyFile.exists()) {
			keyFile.createNewFile();
			final SecureRandom random = new SecureRandom();
			String key = new BigInteger(130, random).toString(32);
			FileWriter writer = new FileWriter(keyFile);
			writer.write(key);
			writer.append("\n#\n# This is the key used to encrypt tokens\n#");
			writer.flush();
			writer.close();
		}
		init(keyFile);
	}

	private void init(String masterKey) {
		if (masterKey == null) {
			throw new IllegalArgumentException("Unexpectedly empty masterKey:"
					+ masterKey);
		}
		masterKey = masterKey.trim();
		byte[] keyBytes = getUtf8Bytes(masterKey);
		init(keyBytes);
	}

	/**
	 * @return UTF-8 byte array for the input string.
	 */
	private static byte[] getUtf8Bytes(String s) {
		if (s == null) {
			return ArrayUtils.EMPTY_BYTE_ARRAY;
		}
		ByteBuffer bb = UTF8_CHARSET.encode(s);
		return ArrayUtils.subarray(bb.array(), 0, bb.limit());

	}

	private void init(byte[] masterKey) {
		if (masterKey.length < MASTER_KEY_MIN_LEN) {
			// "Master key needs at least %s bytes", MASTER_KEY_MIN_LEN);
		}

		cipherKey = deriveKey(CIPHER_KEY_LABEL, masterKey, 16);
		hmacKey = deriveKey(HMAC_KEY_LABEL, masterKey, 0);
	}

	/**
	 * Generates unique keys from a master key.
	 * 
	 * @param label
	 *            type of key to derive
	 * @param masterKey
	 *            master key
	 * @param len
	 *            length of key needed, less than 20 bytes. 20 bytes are
	 *            returned if len is 0.
	 * 
	 * @return a derived key of the specified length
	 */
	private byte[] deriveKey(byte label, byte[] masterKey, int len) {
		byte[] base = concat(new byte[] { label }, masterKey);
		byte[] hash = DigestUtils.sha(base);
		if (len == 0) {
			return hash;
		}
		byte[] out = new byte[len];
		System.arraycopy(hash, 0, out, 0, out.length);
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shindig.util.BlobCrypter#wrap(java.util.Map)
	 */
	public String wrap(Map<String, String> in) throws Exception {
		try {
			byte[] encoded = serialize(in);
			byte[] cipherText = aes128cbcEncrypt(cipherKey, encoded);
			byte[] hmac = hmacSha1(hmacKey, cipherText);
			byte[] b64 = Base64.encodeBase64URLSafe(concat(cipherText, hmac));
			return new String(b64, "UTF-8");
		} catch (GeneralSecurityException e) {
			throw new Exception(e);
		}
	}

	/**
	 * Encode the input for transfer. We use something a lot like HTML form
	 * encodings.
	 * 
	 * @param in
	 *            map of parameters to encode
	 */
	private byte[] serialize(Map<String, String> in) {
		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> val : in.entrySet()) {
			sb.append(encode(val.getKey()));
			sb.append('=');
			sb.append(encode(val.getValue()));
			sb.append('&');
		}
		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1); // Remove the last &
		}
		return getUtf8Bytes(sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shindig.util.BlobCrypter#unwrap(java.lang.String, int)
	 */
	public Map<String, String> unwrap(String in) throws Exception {
		try {
			byte[] bin = Base64.decodeBase64(getUtf8Bytes(in));
			byte[] hmac = new byte[HMAC_SHA1_LEN];
			byte[] cipherText = new byte[bin.length - HMAC_SHA1_LEN];
			System.arraycopy(bin, 0, cipherText, 0, cipherText.length);
			System.arraycopy(bin, cipherText.length, hmac, 0, hmac.length);
			hmacSha1Verify(hmacKey, cipherText, hmac);
			byte[] plain = aes128cbcDecrypt(cipherKey, cipherText);
			Map<String, String> out = deserialize(plain);
			return out;
		} catch (GeneralSecurityException e) {
			throw new Exception("Invalid token signature", e);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new Exception("Invalid token format", e);
		} catch (NegativeArraySizeException e) {
			throw new Exception("Invalid token format", e);
		}

	}

	private Map<String, String> deserialize(byte[] plain)
			throws UnsupportedEncodingException {
		String base = new String(plain, "UTF-8");
		// replaces [&=] regex
		String[] items = StringUtils.splitPreserveAllTokens(base, "&=");
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < items.length;) {
			String key = decode(items[i++]);
			String val = decode(items[i++]);
			map.put(key, val);
		}
		return map;
	}

	private static String encode(String input) {
		try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private static String decode(String input) {
		try {
			return URLDecoder.decode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the values from each provided array combined into a single array.
	 * For example, {@code concat(new byte[] a, b}, new byte[] {}, new byte[]
	 * {c}} returns the array {@code a, b, c}}.
	 * 
	 * @param arrays
	 *            zero or more {@code byte} arrays
	 * @return a single array containing all the values from the source arrays,
	 *         in order
	 */
	private static byte[] concat(byte[]... arrays) {
		int length = 0;
		for (byte[] array : arrays) {
			length += array.length;
		}
		byte[] result = new byte[length];
		int pos = 0;
		for (byte[] array : arrays) {
			System.arraycopy(array, 0, result, pos, array.length);
			pos += array.length;
		}
		return result;
	}

	private static byte[] aes128cbcEncrypt(byte[] key, byte[] plain)
			throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
		byte iv[] = getRandomBytes(cipher.getBlockSize());
		return concat(iv, aes128cbcEncryptWithIV(key, iv, plain));
	}

	/**
	 * AES-128-CBC decryption. The IV is assumed to be the first 16 bytes of the
	 * cipher text.
	 * 
	 * @param key
	 * @param cipherText
	 * 
	 * @return the plain text
	 * 
	 * @throws GeneralSecurityException
	 */
	private static byte[] aes128cbcDecrypt(byte[] key, byte[] cipherText)
			throws GeneralSecurityException {
		byte iv[] = new byte[CIPHER_BLOCK_SIZE];
		System.arraycopy(cipherText, 0, iv, 0, iv.length);
		return aes128cbcDecryptWithIv(key, iv, cipherText, iv.length);
	}

	/**
	 * Returns strong random bytes.
	 * 
	 * @param numBytes
	 *            number of bytes of randomness
	 */
	private static byte[] getRandomBytes(int numBytes) {
		SecureRandom RAND = new SecureRandom();
		byte[] out = new byte[numBytes];
		RAND.nextBytes(out);
		return out;
	}

	/**
	 * AES-128-CBC encryption with a given IV.
	 * 
	 * @param key
	 * @param iv
	 * @param plain
	 * 
	 * @return the cipher text
	 * 
	 * @throws GeneralSecurityException
	 */
	private static byte[] aes128cbcEncryptWithIV(byte[] key, byte[] iv,
			byte[] plain) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
		Key cipherKey = new SecretKeySpec(key, CIPHER_KEY_TYPE);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.ENCRYPT_MODE, cipherKey, ivSpec);
		return cipher.doFinal(plain);
	}

	/**
	 * AES-128-CBC decryption with a particular IV.
	 * 
	 * @param key
	 *            decryption key
	 * @param iv
	 *            initial vector for decryption
	 * @param cipherText
	 *            cipher text to decrypt
	 * @param offset
	 *            offset into cipher text to begin decryption
	 * 
	 * @return the plain text
	 * 
	 * @throws GeneralSecurityException
	 */
	private static byte[] aes128cbcDecryptWithIv(byte[] key, byte[] iv,
			byte[] cipherText, int offset) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
		Key cipherKey = new SecretKeySpec(key, CIPHER_KEY_TYPE);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		cipher.init(Cipher.DECRYPT_MODE, cipherKey, ivSpec);
		return cipher.doFinal(cipherText, offset, cipherText.length - offset);
	}

	/**
	 * HMAC sha1
	 * 
	 * @param key
	 *            the key must be at least 8 bytes in length.
	 * @param in
	 *            byte array to HMAC.
	 * @return the hash
	 * 
	 * @throws GeneralSecurityException
	 */
	private static byte[] hmacSha1(byte[] key, byte[] in)
			throws GeneralSecurityException {
		if (key.length < MIN_HMAC_KEY_LEN) {
			throw new GeneralSecurityException("HMAC key should be at least "
					+ MIN_HMAC_KEY_LEN + " bytes.");
		}
		Mac hmac = Mac.getInstance(HMAC_TYPE);
		Key hmacKey = new SecretKeySpec(key, HMAC_TYPE);
		hmac.init(hmacKey);
		hmac.update(in);
		return hmac.doFinal();
	}

	/**
	 * Verifies an HMAC SHA1 hash. Throws if the verification fails.
	 * 
	 * @param key
	 * @param in
	 * @param expected
	 * @throws GeneralSecurityException
	 */
	private static void hmacSha1Verify(byte[] key, byte[] in, byte[] expected)
			throws GeneralSecurityException {
		Mac hmac = Mac.getInstance(HMAC_TYPE);
		Key hmacKey = new SecretKeySpec(key, HMAC_TYPE);
		hmac.init(hmacKey);
		hmac.update(in);
		byte actual[] = hmac.doFinal();
		if (actual.length != expected.length) {
			throw new GeneralSecurityException("HMAC verification failure");
		}
		for (int i = 0; i < actual.length; i++) {
			if (actual[i] != expected[i]) {
				throw new GeneralSecurityException("HMAC verification failure");
			}
		}
	}
}
