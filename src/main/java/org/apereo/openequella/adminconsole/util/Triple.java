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

import java.util.Objects;

public class Triple<FIRST, SECOND, THIRD> {
	private final FIRST first;
	private final SECOND second;
	private final THIRD third;

	public Triple(FIRST first, SECOND second, THIRD third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public FIRST getFirst() {
		return first;
	}

	public SECOND getSecond() {
		return second;
	}

	public THIRD getThird() {
		return third;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second, third);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second)
				&& Objects.equals(third, other.third);
	}
}