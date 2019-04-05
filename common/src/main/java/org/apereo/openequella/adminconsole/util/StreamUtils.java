package org.apereo.openequella.adminconsole.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class StreamUtils {
	public static void copyStream(
      final InputStream in,
      final OutputStream out,
      final int bufferSize,
      final ProgressListener listener)
      throws IOException {
    byte[] buf = new byte[bufferSize];
    int len;
    while ((len = in.read(buf)) != -1) {
      out.write(buf, 0, len);
      if (listener != null) {
        listener.add(len);
      }
    }
  }

  public static void copyStream(
      final InputStreamReader in,
      final StringBuilder out)
      throws IOException {
    char[] buf = new char[4096];
    int len;
    while ((len = in.read(buf)) != -1) {
      out.append(new String(buf, 0, len));
    }
  }
}