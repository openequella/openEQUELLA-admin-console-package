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
package org.apereo.openequella.adminconsole.configurator;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.apereo.openequella.adminconsole.config.ProxySettings;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.JGroup;
import org.apereo.openequella.adminconsole.swing.TableLayout;

public class ProxySettingsDialog extends JDialog implements ActionListener, KeyListener {
  private static final long serialVersionUID = 1L;
  public static final int RESULT_CANCEL = 0;
  public static final int RESULT_OK = 1;

  private static final int WINDOW_WIDTH = 300;

  private static final int PROXY_MIN = 1;
  private static final int PROXY_DEFAULT = 8080;
  private static final int PROXY_MAX = Short.MAX_VALUE - 1;
  private static final int PROXY_STEP = 1;

  private static final String WINDOW_TITLE = "Proxy Settings";
  private static final String LABEL_PROXY_HOST = "Proxy Host:";
  private static final String LABEL_PROXY_PORT = "Proxy Port:";
  private static final String LABEL_PROXY_REQUIRES_AUTH = "Server requires authentication";
  private static final String LABEL_PROXY_USERNAME = "Username:";
  private static final String LABEL_PROXY_PASSWORD = "Password:"; 

  
  private static final String BUTTON_OK = "OK";
  private static final String BUTTON_CANCEL = "Cancel";

  private final ProxySettings proxySettings;

  private int result = RESULT_CANCEL;

  private JTextField hostField;
  private SpinnerNumberModel portModel;

  private JGroup credentials;
  private JTextField usernameField;
  private JPasswordField passwordField;

  private JButton ok;
  private JButton cancel;

  public ProxySettingsDialog(Frame frame, ProxySettings proxySettings) {
    super(frame);
    this.proxySettings = proxySettings;
    setup();

    hostField.setText(proxySettings.getHost());
    portModel.setValue(proxySettings.getPort());

    String username = proxySettings.getUsername();
    if (username != null && username.length() > 0) {
      credentials.setSelected(true);
      usernameField.setText(username);
      passwordField.setText(proxySettings.getPassword());
    }

    updateButtons();
  }

  private void setup() {
    setupCredentials();
    final JPanel hostPanel = createHostPanel();

    ok = new JButton(BUTTON_OK);
    cancel = new JButton(BUTTON_CANCEL);

    ok.addActionListener(this);
    cancel.addActionListener(this);

    final int width1 = cancel.getPreferredSize().width;
    final int height1 = hostPanel.getPreferredSize().height;
    final int height2 = credentials.getPreferredSize().height;
    final int height3 = cancel.getPreferredSize().height;
    final int[] rows = { height1, height2, height3 };
    final int[] cols = { WINDOW_WIDTH - (width1 * 2), width1, width1, };

    JPanel all = new JPanel(new TableLayout(rows, cols));
    all.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    all.add(hostPanel, new Rectangle(0, 0, 3, 1));
    all.add(credentials, new Rectangle(0, 1, 3, 1));
    all.add(ok, new Rectangle(1, 2, 1, 1));
    all.add(cancel, new Rectangle(2, 2, 1, 1));

    updateButtons();

    setTitle(WINDOW_TITLE);
    setModal(true);
    setResizable(false);
    getContentPane().add(all);
    getRootPane().setDefaultButton(ok);

    pack();
    ComponentHelper.centreOnScreen(this);
  }

  protected JPanel createHostPanel() {
    JLabel hostLabel = new JLabel(LABEL_PROXY_HOST);
    JLabel portLabel = new JLabel(LABEL_PROXY_PORT);

    hostField = new JTextField();
    hostField.addKeyListener(this);

    portModel = new SpinnerNumberModel(PROXY_DEFAULT, PROXY_MIN, PROXY_MAX, PROXY_STEP);
    JSpinner portSpinner = new JSpinner(portModel);

    final int width1 = hostLabel.getPreferredSize().width;
    final int width2 = portSpinner.getPreferredSize().width;
    final int height1 = hostField.getPreferredSize().height;
    final int[] rows = { height1, height1 };
    final int[] cols = { width1, width2, TableLayout.FILL };

    final JPanel hostPanel = new JPanel();
    hostPanel.setLayout(new TableLayout(rows, cols));

    hostPanel.add(hostLabel, new Rectangle(0, 0, 1, 1));
    hostPanel.add(hostField, new Rectangle(1, 0, 2, 1));
    hostPanel.add(portLabel, new Rectangle(0, 1, 1, 1));
    hostPanel.add(portSpinner, new Rectangle(1, 1, 1, 1));
    return hostPanel;
  }

  private void setupCredentials() {
    JLabel usernameLabel = new JLabel(LABEL_PROXY_USERNAME);
    JLabel passwordLabel = new JLabel(LABEL_PROXY_PASSWORD);

    usernameField = new JTextField();
    passwordField = new JPasswordField();

    usernameField.addKeyListener(this);
    passwordField.addKeyListener(this);

    final int width1 = usernameLabel.getPreferredSize().width;
    final int width2 = passwordLabel.getPreferredSize().width;
    final int width3 = Math.max(width1, width2);
    final int height1 = usernameField.getPreferredSize().height;

    final int[] rows = { height1, height1 };
    final int[] cols = { width3, TableLayout.FILL };

    credentials = new JGroup(LABEL_PROXY_REQUIRES_AUTH, false);
    credentials.addActionListener(this);
    credentials.setInnerLayout(new TableLayout(rows, cols));

    credentials.addInner(usernameLabel, new Rectangle(0, 0, 1, 1));
    credentials.addInner(passwordLabel, new Rectangle(0, 1, 1, 1));
    credentials.addInner(usernameField, new Rectangle(1, 0, 1, 1));
    credentials.addInner(passwordField, new Rectangle(1, 1, 1, 1));

    credentials.setSelected(false);
  }

  public ProxySettings getUpdatedSettings() {
    proxySettings.setHost(hostField.getText());
    proxySettings.setPort(portModel.getNumber().intValue());

    if (credentials.isSelected()) {
      proxySettings.setUsername(usernameField.getText());
      proxySettings.setPassword(new String(passwordField.getPassword()));
    } else {
      proxySettings.setUsername(null);
      proxySettings.setPassword(null);
    }
    return proxySettings;
  }

  public int getResult() {
    return result;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == ok) {
      result = RESULT_OK;
      dispose();
    } else if (e.getSource() == cancel) {
      dispose();
    } else {
      updateButtons();
    }
  }

  private void updateButtons() {
    boolean hostEmpty = hostField.getText().length() == 0;
    boolean credsRequired = credentials.isSelected();
    boolean usernameEmpty = usernameField.getText().length() == 0;
    boolean passwordEmpty = passwordField.getPassword().length == 0;
    boolean credsEmpty = usernameEmpty || passwordEmpty;

    ok.setEnabled(!(hostEmpty || (credsRequired && credsEmpty)));
  }

  @Override
  public void keyReleased(KeyEvent e) {
    updateButtons();
  }

  @Override
  public void keyPressed(KeyEvent e) {
    // We do not want to listen to this event
  }

  @Override
  public void keyTyped(KeyEvent e) {
    // We do not want to listen to this event
  }
}
