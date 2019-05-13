/**
 * Copyright <2019>, The Apereo Foundation
 *
 * This project includes software developed by The Apereo Foundation.
 * http://www.apereo.org/
 *
 * Licensed under the Apache License, Version 2.0, (the "License"); you may not
 * use this software except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apereo.openequella.adminconsole.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apereo.openequella.adminconsole.util.Encrypt;

public class ProxySettings {
  private String host;
  private int port;
  private String username;
  private String password;
  private String obsPassword;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  @JsonIgnore
  public String getPassword() {
    return password;
  }

  @JsonIgnore
  public void setPassword(String password) {
    this.password = password;
    this.obsPassword = Encrypt.encrypt(password);
  }

  /**
   * @return the obscured password
   */
  public String getObsPassword() {
    return obsPassword;
  }

  /**
   * @param obsPassword an obscured password
   */
  public void setObsPassword(String obsPassword) {
    this.obsPassword = obsPassword;
    this.password = Encrypt.decrypt(obsPassword);
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
