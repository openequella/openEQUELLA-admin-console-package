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
package org.apereo.openequella.adminconsole;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apereo.openequella.adminconsole.config.Config;
import org.apereo.openequella.adminconsole.config.ProxySettings;
import org.apereo.openequella.adminconsole.config.ServerProfile;
import org.junit.Before;
import org.junit.Test;

//import static org.testng.Assert.*;

public class ConfigTest {
    private static final String[] PROFILE_NAMES = { "Profile1 Name ╔~\\◙¥ . test 11111 {\"\"}", "Profile 2", "" };
    private static final String[] PROFILE_URLS = { "Profile1 URL ³±0]■/.iX▬∟", "Profile2 URL", "" };

    @Before
    public void removeExisting() {
        Config.deleteServerConfigFile();
    }

    @Test
    public void testWriteRead() {
        final String proxyHost = "!@#$%^&*(";
        final int proxyPort = 8000;
        final String proxyUsername = "joe.smith@something.com";
        final String proxyPassword = "1234567890 _ QWERTYUIOPASDFGHJKLZXCVBNM";
        final int defaultProfile = 2;

        final Config original = new Config();
        for (int i = 0; i < PROFILE_NAMES.length; i++) {
            addProfile(original, PROFILE_NAMES[i], PROFILE_URLS[i]);
        }

        original.setDefaultServerIndex(defaultProfile);

        final ProxySettings proxy = new ProxySettings();
        proxy.setHost(proxyHost);
        proxy.setPort(proxyPort);
        proxy.setUsername(proxyUsername);
        proxy.setPassword(proxyPassword);
        original.setProxy(proxy);

        original.writeServerConfigFile();

        final Config reloadedConfig = Config.readServerConfigFile();

        final List<ServerProfile> reloadedProfiles = reloadedConfig.getServers();
        assertEquals(PROFILE_NAMES.length, reloadedProfiles.size());

        for (int i = 0; i < reloadedProfiles.size(); i++) {
            final ServerProfile reloadedProfile = reloadedProfiles.get(i);
            assertEquals(PROFILE_NAMES[i], reloadedProfile.getName());
            assertEquals(PROFILE_URLS[i], reloadedProfile.getUrl());
        }

        assertEquals(original.getDefaultServerIndex(), reloadedConfig.getDefaultServerIndex());

        final ProxySettings reloadedProxySettings = reloadedConfig.getProxy();
        assertEquals(proxy.getHost(), reloadedProxySettings.getHost());
        assertEquals(proxy.getPort(), reloadedProxySettings.getPort());
        assertEquals(proxy.getUsername(), reloadedProxySettings.getUsername());
        assertEquals(proxy.getPassword(), reloadedProxySettings.getPassword());
    }

    private void addProfile(Config config, String name, String url) {
        ServerProfile profile = new ServerProfile();
        profile.setName(name);
        profile.setUrl(url);
        config.getServers().add(profile);
    }
}
