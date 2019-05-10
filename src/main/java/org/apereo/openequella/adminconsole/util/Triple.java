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