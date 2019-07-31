/**
 *     Licensed to The Apereo Foundation under one or more contributor license
 *     agreements. See the NOTICE file distributed with this work for additional
 *     information regarding copyright ownership.
 *
 *     The Apereo Foundation licenses this file to you under the Apache License,
 *     Version 2.0, (the "License"); you may not use this file except in compliance
 *     with the License. You may obtain a copy of the License at:
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.apereo.openequella.adminconsole.util;

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