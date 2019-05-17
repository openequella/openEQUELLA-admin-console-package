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

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExecUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExecUtils.class);

  public static int exec(String[] cmdarray, Map<String, String> additionalEnv, File dir) {
    try {
      if (LOGGER.isDebugEnabled()) {
        LOGGER.debug("Exec " + Arrays.asList(cmdarray));
      }

      final ProcessBuilder pbuilder = new ProcessBuilder(cmdarray).inheritIO();
      if (additionalEnv != null) {
        pbuilder.environment().putAll(additionalEnv);
      }
      pbuilder.directory(dir);
      final Process process = pbuilder.start();

      final int exitStatus = process.waitFor();
      LOGGER.info("Exec finished with status " + exitStatus);
      return exitStatus;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private ExecUtils() {
    throw new Error();
  }
}
