package org.apereo.openequella.adminconsole;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apereo.openequella.adminconsole.json.JsonService;
import org.apereo.openequella.adminconsole.util.ExecUtils;
import org.apereo.openequella.adminconsole.util.ExecUtils.ExecResult;

public class JarService {
	private final String baseUrl;
	private final File cacheFolder;
	private final File binFolder;

	public JarService(String baseUrl) {
		this.baseUrl = baseUrl;
		this.cacheFolder = Storage.getFolder("cache");
		this.binFolder = Storage.getFolder("bin");
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
	public File ensureCacheJar(String jarName){
		if (downloadJar(jarName)){
			return getCacheJarFile(jarName);
		}
		return null;
	}

	/**
	 * @param jarName Omit the jar extension
	 * @return true if a jar was downloaded or we already have one available
	 */
	public boolean downloadJar(String jarName){
		boolean isFatal = true;
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
				if (metadataFile.exists()){
					final JarMetadata meta = JsonService.readFile(metadataFile, JarMetadata.class);
					conn.setRequestProperty("If-None-Match", meta.getEtag());
					conn.setRequestProperty("If-Modified-Since", meta.getModifiedDate());
				}
				conn.connect();
				final int responseCode = conn.getResponseCode();
				if (responseCode == 304){
					// we're ok with our version, but check that it exists first
					return jarFile.exists();
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
					FileOutputStream fos = new FileOutputStream(jarFile)){
					copyData(bis, fos);
					return true;
				}
			}
			finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}
		catch (Exception e) {
			if (isFatal){
				throw new RuntimeException(e);
			}
			System.out.println("Failed to download jar " + jarName + ".  Using cached version.");
		}
		return false;
	}

	public ExecResult executeJar(String jarName, String mainClass, String... jvmArgs){
		ensureBinJars(jarName);
		final File binJar = getBinJarFile(jarName);
		final List<String> command = new ArrayList<>();
		command.add("java");
		command.add("-cp");
		command.add(jarName + ".jar");
		for (String jvmArg : jvmArgs)
		{
			command.add(jvmArg);
		}
		command.add(mainClass);
		
		final ExecResult result = ExecUtils.exec(command.toArray(new String[]{}), null, binJar.getParentFile());
		return result;
	}


	private void copyData(InputStream in, OutputStream out) throws Exception {
		final byte[] buffer = new byte[8 * 1024];
		int len;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getCacheMetadataFile(String jarName){
		return Storage.getFile(cacheFolder, jarName + "-manifest.json");
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getCacheJarFile(String jarName){
		return Storage.getFile(cacheFolder, jarName + ".jar");
	}

	/**
	 * @param jarName Omit the jar extension
	 */
	private File getBinJarFile(String jarName){
		return Storage.getFile(binFolder, jarName + ".jar");
	}
}