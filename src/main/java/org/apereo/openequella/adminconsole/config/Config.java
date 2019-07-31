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
package org.apereo.openequella.adminconsole.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.apereo.openequella.adminconsole.service.JsonService;
import org.apereo.openequella.adminconsole.service.StorageService;

public class Config {
	private List<ServerProfile> servers = new ArrayList<>();
	private Integer defaultServerIndex;
	private ProxySettings proxy = new ProxySettings();

	public List<ServerProfile> getServers() {
		return servers;
	}

	public ProxySettings getProxy() {
		return proxy;
	}

	public void setProxy(ProxySettings proxy) {
		this.proxy = proxy;
	}

	public Integer getDefaultServerIndex() {
		return defaultServerIndex;
	}

	public void setDefaultServerIndex(Integer defaultServerIndex) {
		this.defaultServerIndex = defaultServerIndex;
	}

	public void setServers(List<ServerProfile> servers) {
		this.servers = servers;
	}

	public static Config readServerConfigFile() {
        final File serversFile = getServerConfigFile();
        if (serversFile.exists()) {
			return JsonService.readFile(serversFile, Config.class);
        }
        return new Config();
	}
	
	public void writeServerConfigFile(){
        final File serversFile = getServerConfigFile();
        JsonService.writeFile(serversFile, this);
	}
	
	public static boolean deleteServerConfigFile(){
		final File serversFile = getServerConfigFile();
		if (serversFile.exists()){
			if (!serversFile.delete()) {
				throw new RuntimeException("Failed to delete config file " + serversFile.getAbsolutePath());
			}
		}
		return false;
	}

	public static void backupServerConfigFile(){
		copyFiles(getServerConfigFile(), StorageService.getFile("config.json.bak"));
	}

	public static void restoreServerConfigFile(){
		copyFiles(StorageService.getFile("config.json.bak"), getServerConfigFile());
	}

	private static void copyFiles(File src, File dest) {
		if (src.exists())
		{
			try
			{
				Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException io)
			{
				throw new RuntimeException(io);
			}
		}
	}

    private static File getServerConfigFile(){
        return StorageService.getFile("config.json");
    }
}