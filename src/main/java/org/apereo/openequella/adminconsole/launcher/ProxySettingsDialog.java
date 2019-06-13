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

package org.apereo.openequella.adminconsole.launcher;

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;

import org.apereo.openequella.adminconsole.config.ProxySettings;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.TableLayout;

public class ProxySettingsDialog extends JDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  
  public static final int RESULT_CANCEL = 0;
  public static final int RESULT_OK = 1;

  private static final String WINDOW_TITLE = "Proxy Settings";
  private static final String LABEL_PROXY_HOST = "Proxy Host:";
  private static final String LABEL_PROXY_PORT = "Proxy Port:";
  private static final String LABEL_PROXY_USERNAME = "Username:";
  private static final String LABEL_PROXY_PASSWORD = "Password:"; 
  
  private static final String BUTTON_SAVE = "Save";
  private static final String BUTTON_CANCEL = "Cancel";

  private final ProxySettings proxySettings;

  private int result = RESULT_CANCEL;

  private JTextField hostField;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JTextField portField;

  private JButton saveButton;
  private JButton cancelButton;

  public ProxySettingsDialog(Frame frame, ProxySettings proxySettings) {
    super(frame);

    this.proxySettings = proxySettings;
    saveButton = new JButton(BUTTON_SAVE);
    cancelButton = new JButton(BUTTON_CANCEL);
    usernameField = new JTextField();
    passwordField = new JPasswordField();
    hostField = new JTextField();

    // Use DecimalFormat to filter non-digit characters
    DecimalFormat portFormate = new DecimalFormat();
    // Call setGroupingUsed to remove the comma between digits
    portFormate.setGroupingUsed(false);
    portField = new JFormattedTextField(portFormate);

    setup();

    hostField.setText(proxySettings.getHost());
    usernameField.setText(proxySettings.getUsername());
    passwordField.setText(proxySettings.getPassword());
    int port = proxySettings.getPort();
    portField.setText(String.valueOf(port == 0 ? "" : port));
  }

  private void setup() {
    
    saveButton.addActionListener(this);
    cancelButton.addActionListener(this);

    final JComponent mainPanel = createMainPanel();
    final int buttonWidth = cancelButton.getPreferredSize().width;
    JPanel all = new JPanel(new TableLayout(
      new int[]{ mainPanel.getPreferredSize().height, saveButton.getPreferredSize().height },
      new int[]{ TableLayout.FILL, buttonWidth, buttonWidth }
    ));
    all.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    all.add(mainPanel, new Rectangle(0, 0, 3, 1));
    all.add(saveButton, new Rectangle(1, 1, 1, 1));
    all.add(cancelButton, new Rectangle(2, 1, 1, 1));

    setTitle(WINDOW_TITLE);
    setModal(true);
    setResizable(false);
    getContentPane().add(all);
    getRootPane().setDefaultButton(saveButton);

    pack();
    ComponentHelper.ensureMinimumSize(this, 500, 0);
    ComponentHelper.centreOnScreen(this);
  }

  protected JComponent createMainPanel() {
    
    final JLabel hostLabel = new JLabel(LABEL_PROXY_HOST);
    final JLabel portLabel = new JLabel(LABEL_PROXY_PORT);
    final JLabel usernameLabel = new JLabel(LABEL_PROXY_USERNAME);
    final JLabel passwordLabel = new JLabel(LABEL_PROXY_PASSWORD);

    final int height1 = hostField.getPreferredSize().height;
    final int[] rows = { height1, height1, height1, height1, height1, height1, height1, height1 };
    final int[] cols = { TableLayout.FILL };
    final JPanel hostPanel = new JPanel();
    hostPanel.setLayout(new TableLayout(rows, cols));

    hostPanel.add(hostLabel, new Rectangle(0, 0, 1, 1));
    hostPanel.add(hostField, new Rectangle(0, 1, 1, 1));
    hostPanel.add(portLabel, new Rectangle(0, 2, 1, 1));
    hostPanel.add(portField, new Rectangle(0, 3, 1, 1));
    hostPanel.add(usernameLabel, new Rectangle(0, 4, 1, 1));
    hostPanel.add(usernameField, new Rectangle(0, 5, 1, 1));
    hostPanel.add(passwordLabel, new Rectangle(0, 6, 1, 1));
    hostPanel.add(passwordField, new Rectangle(0, 7, 1, 1));

    return hostPanel;
  }

  public ProxySettings getUpdatedSettings() {
    proxySettings.setHost(hostField.getText());
    // As the type of port is int, use 0 to represent null
    int port = (portField.getText().isEmpty() ? 0 : Integer.parseInt(portField.getText()));
    proxySettings.setPort(port);
    proxySettings.setUsername(usernameField.getText());
    proxySettings.setPassword(new String(passwordField.getPassword()));
    return proxySettings;
  }

  public int getResult() {
    return result;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == saveButton) {
      result = RESULT_OK;
      dispose();
    } else if (e.getSource() == cancelButton) {
      dispose();
    }
  }
}
