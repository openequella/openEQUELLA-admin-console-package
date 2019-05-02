package org.apereo.openequella.adminconsole.util;

import javax.annotation.Nullable;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * The admin console could be decompiled to work out the stored password, but at least
 * it safeguards it against the casual user.
 */
public class Encrypt {
	private static String SECRET_KEY = "IPityTheF00lWhoDon'tLikeHe";

	
	public static String encrypt( String value) {
		if (value != null) {
			try {
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword(SECRET_KEY);
				return textEncryptor.encrypt(value);
			} catch (Exception e) {
				throw new RuntimeException("Error encrypting", e);
			}
		}
		return null;
	}

	
	public static String decrypt( String value) {
		if (value != null && value.length() != 0) {
			try {
				BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
				textEncryptor.setPassword(SECRET_KEY);
				return textEncryptor.decrypt(value);
			} catch (Exception e) {
				throw new RuntimeException("Error decrypting", e);
			}
		}
		return null;
	}
}