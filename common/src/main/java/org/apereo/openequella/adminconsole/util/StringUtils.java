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

package org.apereo.openequella.adminconsole.util;

import javax.annotation.Nullable;

public class StringUtils {
  /**
   * Ensures start is a valid point within the string text. The end of the resulting string will be
   * original end point of the source string
   *
   * @param text
   * @param start If negative, used as a position from the right of the string
   * @return
   */
  public static String safeSubstring(final String text, final int start) {
    if (text == null) {
      return null;
    }
    return safeSubstring(text, start, text.length());
  }

  /**
   * Ensures start and end are valid points within the string text
   *
   * @param text
   * @param start If negative, used as a position from the right of the string
   * @param end If negative, used as a position from the right of the string
   * @return
   */
  public static String safeSubstring(@Nullable String text, final int start, final int end) {
    if (text == null) {
      return null;
    }
    int newStart = start;
    int newEnd = end;
    int textLength = text.length();

    if (newStart < 0) {
      // from the right
      newStart = textLength + newStart;

      // went too far
      if (newStart < 0) {
        newStart = 0;
      }
    }

    if (newEnd > textLength) {
      newEnd = textLength;
    } else if (newEnd < 0) {
      // from the right
      newEnd = textLength + newEnd;
    }
    if (newEnd < 0) {
      newEnd = 0;
    }

    // ensure start not too big
    if (newStart > newEnd) {
      newStart = newEnd;
    }
    return text.substring(newStart, newEnd);
  }

  public static boolean bothNullOrEqual(@Nullable String val1, @Nullable String val2) {
		if (val1 == null && val2 == null) {
			return true;
		}
		if ((val1 == null && val2 != null) || (val1 != null && val2 == null)) {
			return false;
		}
		return val1.equals(val2);
	}
}
