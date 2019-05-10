package org.apereo.openequella.adminconsole.util;

import java.util.Objects;

public class Pair<FIRST, SECOND> {
	private final FIRST first;
	private final SECOND second;

	public Pair(FIRST first, SECOND second) {
		this.first = first;
		this.second = second;
	}

	public FIRST getFirst() {
		return first;
	}

	public SECOND getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}
}