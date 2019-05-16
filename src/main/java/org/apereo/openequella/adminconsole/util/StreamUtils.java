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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamUtils {

  public static void copyStream(final InputStream in, final OutputStream out) throws IOException {
    copyStream(in, out, 4096, null);
  }

  public static void copyStream(final InputStream in, final OutputStream out, final int bufferSize,
      final ProgressListener listener) throws IOException {
    byte[] buf = new byte[bufferSize];
    int len;
    while ((len = in.read(buf)) != -1) {
      out.write(buf, 0, len);
      if (listener != null) {
        listener.add(len);
      }
    }
  }

  public static void copyStream(final InputStreamReader in, final StringBuilder out) throws IOException {
    char[] buf = new char[4096];
    int len;
    while ((len = in.read(buf)) != -1) {
      out.append(new String(buf, 0, len));
    }
  }
}