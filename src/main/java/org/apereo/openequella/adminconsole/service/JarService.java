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
package org.apereo.openequella.adminconsole.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apereo.openequella.adminconsole.util.ExecUtils;
import org.apereo.openequella.adminconsole.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JarService.class);

	private final String baseUrl;
	private final File cacheFolder;
	private final File binFolder;

	public JarService(String baseUrl, String uuid) {
		this.baseUrl = baseUrl;
		this.cacheFolder = StorageService.getCacheFolder(uuid,"cache");
		this.binFolder = StorageService.getCacheFolder(uuid,"bin");
	}

	public void ensureBinJars(String... jarNames) {
		try {
			for (final String jarName : jarNames) {
				final File cacheJarFile = ensureCacheJar(jarName);
				if (cacheJarFile != null) {
					final File binJarFile = getBinJarFile(jarName);
					Files.copy(cacheJarFile.toPath(), binJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	public File ensureCacheJar(String jarName) {
		downloadJar(jarName);
		return getCacheJarFile(jarName);
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	public void downloadJar(String jarName) {
		try {
			//check cache entry
			final File metadataFile = getCacheMetadataFile(jarName);
			final File jarFile = getCacheJarFile(jarName);

			// hit the server to download
			//final URL url = new URL(baseUrl + "/console.do?jar=" + URLEncoder.encode(jarName + ".jar", "utf-8"));
			final String strUrl = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + "adminconsole.jar";
			final URL url = new URL(strUrl);
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(10000);
				conn.setRequestMethod("GET");

				if (metadataFile.exists()) {
					final JarMetadata meta = JsonService.readFile(metadataFile, JarService.JarMetadata.class);
					conn.setRequestProperty("If-None-Match", meta.getEtag());
					conn.setRequestProperty("If-Modified-Since", meta.getModifiedDate());
				}
				conn.connect();
				final int responseCode = conn.getResponseCode();
				// we're ok with our version, but check that it exists first
				if (responseCode == 304 && jarFile.exists()) {
					return;
				}

				if (responseCode >= 400 || responseCode < 200){
					throw new Exception("Error downloading jar: " + readError(conn));
				}

				// read etag and modified date
				final String lastModified = conn.getHeaderField("Last-Modified");
				final String etag = conn.getHeaderField("ETag");
				final JarMetadata meta = new JarMetadata();
				meta.setModifiedDate(lastModified);
				meta.setEtag(etag);
				JsonService.writeFile(metadataFile, meta);

				// download and store in our cache
				try (InputStream is = (InputStream) conn.getInputStream();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream fos = new FileOutputStream(jarFile)) {
					StreamUtils.copyStream(bis, fos);
				}
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		} catch (Exception e) {
			LOGGER.error("Failed to download jar " + jarName, e);
			throw new RuntimeException(e);
		}
	}

	public int executeJar(String jarName, String mainClass, String... jvmArgs) {
		final File binJar = getBinJarFile(jarName);
		final List<String> command = new ArrayList<>();
		command.add("java");
		command.add("-cp");
		command.add(jarName + ".jar");
		command.addAll(Arrays.asList(jvmArgs));
		command.add(mainClass);

		return ExecUtils.exec(command.toArray(new String[] {}), null, binJar.getParentFile());
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getCacheMetadataFile(String jarName) {
		return StorageService.getFile(cacheFolder, jarName + "-manifest.json");
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getCacheJarFile(String jarName) {
		return StorageService.getFile(cacheFolder, jarName + ".jar");
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getBinJarFile(String jarName) {
		return StorageService.getFile(binFolder, jarName + ".jar");
	}

	private String readError(HttpURLConnection conn){
		try (final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
			final StringBuilder sb = new StringBuilder();
			String output;
			while( (output = br.readLine()) != null ){
				sb.append(output);
			}
			return sb.toString();
		}
		catch (IOException io){
			throw new RuntimeException(io);
		}
	}

	public static class JarMetadata {
		private String etag;
		private String modifiedDate;
		private String expiryDate;

		/**
		 * @return the etag
		 */
		public String getEtag() {
			return etag;
		}

		/**
		 * @return the expiryDate
		 */
		public String getExpiryDate() {
			return expiryDate;
		}

		/**
		 * @param expiryDate the expiryDate to set
		 */
		public void setExpiryDate(String expiryDate) {
			this.expiryDate = expiryDate;
		}

		/**
		 * @return the modifiedDate
		 */
		public String getModifiedDate() {
			return modifiedDate;
		}

		/**
		 * @param modifiedDate the modifiedDate to set
		 */
		public void setModifiedDate(String modifiedDate) {
			this.modifiedDate = modifiedDate;
		}

		/**
		 * @param etag the etag to set
		 */
		public void setEtag(String etag) {
			this.etag = etag;
		}
	}
}