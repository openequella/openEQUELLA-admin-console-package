/*
 * Copyright 2019 Apereo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apereo.openequella.adminconsole.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apereo.openequella.adminconsole.Storage;
import org.apereo.openequella.adminconsole.json.JsonService;

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

    private static File getServerConfigFile(){
        return Storage.getFile("config.json");
    }
}