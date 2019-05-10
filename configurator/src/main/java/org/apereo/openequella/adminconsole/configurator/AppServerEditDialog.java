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
import javax.swing.JTextField;

import org.apereo.openequella.adminconsole.config.ServerProfile;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.TableLayout;

public class AppServerEditDialog extends JDialog implements ActionListener, KeyListener {
  private static final long serialVersionUID = 1L;

  public static final int RESULT_CANCEL = 0;
  public static final int RESULT_OK = 1;

  private static final String WINDOW_TITLE = "Server Editor";
  private static final String LABEL_SERVER_NAME = "Server Name:";
  private static final String LABEL_SERVER_URL = "Server URL:";
  private static final String BUTTON_SAVE = "Save";
  private static final String BUTTON_CANCEL = "Cancel";

  private int result = RESULT_CANCEL;

  private final ServerProfile profile;

  private final JTextField profileField;
  private final JTextField serverField;
  private final JButton okButton;
  private final JButton cancelButton;
  

  public AppServerEditDialog(Frame frame) {
    this(frame, new ServerProfile());
  }

  public AppServerEditDialog(Frame frame, ServerProfile profile) {
    super(frame);

    this.profile = profile;
    profileField = new JTextField();
    serverField = new JTextField();
    okButton = new JButton(BUTTON_SAVE);
    cancelButton = new JButton(BUTTON_CANCEL);

    setup();

    profileField.setText(profile.getName());
    serverField.setText(profile.getUrl());

    updateOkButton();
  }

  private void setup() {
    final JLabel profileLabel = new JLabel(LABEL_SERVER_NAME);
    final JLabel serverLabel = new JLabel(LABEL_SERVER_URL);

    profileField.addKeyListener(this);
    serverField.addKeyListener(this);

    okButton.addActionListener(this);
    cancelButton.addActionListener(this);

    okButton.setEnabled(false);

    final int width1 = profileLabel.getPreferredSize().width;
    final int width2 = cancelButton.getPreferredSize().width;
    final int height1 = profileLabel.getPreferredSize().height;
    final int height2 = profileField.getPreferredSize().height;
    final int height3 = cancelButton.getPreferredSize().height;
    final int[] rows = { height1, height2, height1, height2, TableLayout.FILL, height3 };
    final int[] cols = { width1, TableLayout.FILL, width2, width2 };

    JPanel all = new JPanel(new TableLayout(rows, cols, 5, 5));
    all.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    all.add(profileLabel, new Rectangle(0, 0, 4, 1));
    all.add(profileField, new Rectangle(0, 1, 4, 1));

    all.add(serverLabel, new Rectangle(0, 2, 4, 1));
    all.add(serverField, new Rectangle(0, 3, 4, 1));

    all.add(okButton, new Rectangle(2, 5, 1, 1));
    all.add(cancelButton, new Rectangle(3, 5, 1, 1));

    setModal(true);
    setTitle(WINDOW_TITLE);
    getContentPane().add(all);
    getRootPane().setDefaultButton(okButton);

    pack();
    ComponentHelper.ensureMinimumSize(this, 500, 0);
    ComponentHelper.centreOnScreen(this);
  }

  public ServerProfile getUpdatedProfile() {
    profile.setName(profileField.getText());
    profile.setUrl(serverField.getText());

    return profile;
  }

  public int getResult() {
    return result;
  }

  protected void updateOkButton()
  {
    boolean profileEmpty = profileField.getText().trim().length() == 0;
    boolean serverEmpty = serverField.getText().trim().length() == 0;

    okButton.setEnabled(!profileEmpty && !serverEmpty);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == okButton) {
      result = RESULT_OK;
      dispose();
    } else if (e.getSource() == cancelButton) {
      dispose();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    updateOkButton();
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
