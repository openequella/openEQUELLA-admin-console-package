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

import java.util.UUID;

import javax.annotation.Nullable;

import org.apereo.openequella.adminconsole.util.StringUtils;

public class ServerProfile {
	private String uuid;
	private String url;
	private String name;

	public ServerProfile() {
		uuid = UUID.randomUUID().toString();
	}

	public String getUrl() {
		return url;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (other == null) {
			return false;
		}
		if (other instanceof ServerProfile) {
			final ServerProfile otherProfile = (ServerProfile) other;
			return StringUtils.bothNullOrEqual(otherProfile.getUuid(), getUuid());
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (uuid != null) {
			return uuid.hashCode();
		}
		return 0;
	}
}