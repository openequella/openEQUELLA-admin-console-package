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
package org.apereo.openequella.adminconsole.launcher;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.apereo.openequella.adminconsole.config.Config;
import org.apereo.openequella.adminconsole.config.ProxySettings;
import org.apereo.openequella.adminconsole.config.ServerProfile;
import org.apereo.openequella.adminconsole.service.JarService;
import org.apereo.openequella.adminconsole.service.StorageService;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.ServerPicker;
import org.apereo.openequella.adminconsole.swing.TableLayout;
import org.apereo.openequella.adminconsole.util.BlindSSLSocketFactory;
import org.apereo.openequella.adminconsole.util.ExecUtils.ExecResult;
import org.apereo.openequella.adminconsole.util.Proxy;
import org.slf4j.LoggerFactory;

public class ClientLauncher extends JFrame implements ActionListener, WindowListener {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientLauncher.class);

  private static final int WINDOW_WIDTH = 450;

  private static final String WINDOW_TITLE = "openEQUELLA Administration Console Launcher";
  private static final String LABEL_SERVER = "Server:";
  private static final String LAUNCH_BUTTON = "Launch";
  private static final String ADD_SERVER_BUTTON = "Add Server...";
  private static final String EDIT_SERVER_BUTTON = "Edit...";
  private static final String REMOVE_SERVER_BUTTON = "Remove";
  private static final String MAKE_DEFAULT_SERVER_BUTTON = "Make Default";
  private static final String PROXY_SETTINGS_BUTTON = "Proxy Settings...";
  private static final String CLONE_SERVER_TITLE = "Clone server?";
  private static final String PROMPT_CLONE = "Do you want to clone the currently selected server?";
  private static final String REMOVE_SERVER_TITLE = "Remove server?";
  private static final String PROMPT_REMOVE = "Are you sure you want to remove this server?";
  private static final String MAKE_DEFAULT_TITLE = "Make default?";
  private static final String PROMPT_MAKE_DEFAULT = "Make this server the default server to show?";

  private Config config;

  private final ServerPicker serverPicker;
  private final JButton addServerButton;
  private final JButton editServerButton;
  private final JButton removeServerButton;
  private final JButton makeDefaultServerButton;
  private final JButton editProxyButton;
  private final JButton launchButton;

  public static void main(String args[]) throws Exception {
    new ClientLauncher();
  }

  public ClientLauncher() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      BlindSSLSocketFactory.register();

      config = readConfig();
      serverPicker = new ServerPicker(config);
      addServerButton = new JButton(ADD_SERVER_BUTTON);
      editServerButton = new JButton(EDIT_SERVER_BUTTON);
      removeServerButton = new JButton(REMOVE_SERVER_BUTTON);
      editProxyButton = new JButton(PROXY_SETTINGS_BUTTON);
      makeDefaultServerButton = new JButton(MAKE_DEFAULT_SERVER_BUTTON);
      launchButton = new JButton(LAUNCH_BUTTON);

      setTitle(WINDOW_TITLE);
      setResizable(false);
      addWindowListener(this);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      final JComponent generalActions = createGeneralActions();
      final JComponent serverList = createServerList();
      final JComponent serverActions = createServerActions();

      final int[] rows = { generalActions.getPreferredSize().height, 0, serverList.getPreferredSize().height, 0,
          serverActions.getPreferredSize().height };
      final TableLayout layout = new TableLayout(rows, new int[] { WINDOW_WIDTH });
      final JPanel all = new JPanel(layout);
      all.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      all.add(generalActions, new Rectangle(0, 0, 1, 1));
      all.add(serverList, new Rectangle(0, 2, 1, 1));
      all.add(serverActions, new Rectangle(0, 4, 1, 1));

      updateButtons();

      getContentPane().add(all);
      getRootPane().setDefaultButton(launchButton);
      addWindowListener(this);
      pack();

      ComponentHelper.centreOnScreen(this);
      setVisible(true);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private JComponent createServerList() {
    final JLabel profileLabel = new JLabel(LABEL_SERVER);

    final JPanel serverPanel = new JPanel(
        new TableLayout(new int[] { profileLabel.getPreferredSize().height, serverPicker.getPreferredSize().height },
            new int[] { TableLayout.FILL, serverPicker.getPreferredSize().width }));
    serverPanel.add(profileLabel, new Rectangle(0, 0, 2, 1));
    serverPanel.add(serverPicker, new Rectangle(0, 1, 2, 1));

    return serverPanel;
  }

  protected JComponent createGeneralActions() {

    addServerButton.addActionListener(this);
    editProxyButton.addActionListener(this);

    final int buttonWidth = ComponentHelper.maxWidth(addServerButton, editProxyButton);
    JPanel buttonsPanel = new JPanel(new TableLayout(new int[] { addServerButton.getPreferredSize().height },
        new int[] { TableLayout.FILL, buttonWidth, buttonWidth, TableLayout.FILL }));

    buttonsPanel.add(addServerButton, new Rectangle(1, 0, 1, 1));
    buttonsPanel.add(editProxyButton, new Rectangle(2, 0, 1, 1));

    return buttonsPanel;
  }

  private JComponent createServerActions() {

    editServerButton.addActionListener(this);
    removeServerButton.addActionListener(this);
    makeDefaultServerButton.addActionListener(this);
    launchButton.addActionListener(this);

    final int buttonWidth = ComponentHelper.maxWidth(editServerButton, removeServerButton, makeDefaultServerButton,
        launchButton);

    final JPanel serverActionsPanel = new JPanel(
        new TableLayout(new int[] { editServerButton.getPreferredSize().height },
            new int[] { TableLayout.FILL, buttonWidth, buttonWidth, buttonWidth, buttonWidth, TableLayout.FILL }));

    serverActionsPanel.add(editServerButton, new Rectangle(1, 0, 1, 1));
    serverActionsPanel.add(removeServerButton, new Rectangle(2, 0, 1, 1));
    serverActionsPanel.add(makeDefaultServerButton, new Rectangle(3, 0, 1, 1));
    serverActionsPanel.add(launchButton, new Rectangle(4, 0, 1, 1));

    return serverActionsPanel;
  }

  private void updateButtons() {
    boolean enable = serverPicker.getSelectedIndex() > -1;
    launchButton.setEnabled(enable);
    editServerButton.setEnabled(enable);
    removeServerButton.setEnabled(enable);
    makeDefaultServerButton.setEnabled(enable);
  }

  private static Config readConfig() {
    try {
      return Config.readServerConfigFile();
    } catch (Exception e) {
      LOGGER.warn("Error reading configuration file", e);
      return new Config();
    }
  }

  private void launch(final ServerProfile serverProfile) {
    final SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
      @Override
      public Object doInBackground() throws Exception {
        final ProxySettings p = config.getProxy();
        if (p != null) {
          Proxy.setProxy(p.getHost(), p.getPort(), p.getUsername(), p.getPassword());
        }

        try {
          String url = serverProfile.getUrl().trim();
          if (!url.endsWith("/")) {
            url += '/';
          }
          LOGGER.info("Endpoint:  " + url);

          ClientLauncher.this.setVisible(false);

          final JarService jarService = new JarService(url);
          final ExecResult result = jarService.executeJar("adminconsole", "com.tle.admin.boot.Bootstrap",
              "-Djnlp.ENDPOINT=" + url, "-Dplugin.cache.dir=" + StorageService.getFolder("cache"));

          // FIXME: we want real-time access to out and err streams
          LOGGER.info("stdout:" + result.getStdout());
          final String stderr = result.getStderr();
          if (stderr != null && stderr.length() > 0) {
            LOGGER.error("stderr:" + stderr);
          }

          ClientLauncher.this.dispose();
          System.exit(0);
        } catch (Throwable e) {
          LOGGER.error("Error invoking admin console", e);
          System.exit(1);
        }
        return null;
      }
    };

    worker.execute();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final Object source = e.getSource();
    final int index = serverPicker.getSelectedIndex();

    boolean write = false;
    if (source == launchButton) {
      launch((ServerProfile) serverPicker.getSelectedItem());
    } else if (source == addServerButton) {
      write = handleAddServer();
    } else if (source == editServerButton) {
      write = handleEditServer(index);
    } else if (source == removeServerButton) {
      write = handleRemoveServer(index);
    } else if (source == editProxyButton) {
      write = handleEditProxy();
    } else if (source == makeDefaultServerButton) {
      write = handleMakeDefault(index);
    }

    if (write) {
      config.writeServerConfigFile();
    }
  }

  private void onExit() {
    dispose();
    System.exit(0);
  }

  protected boolean handleAddServer() {
    final ServerProfile selectedProfile = (ServerProfile) serverPicker.getSelectedItem();
    final ServerProfile newProfile = new ServerProfile();
    if (selectedProfile != null && JOptionPane.showConfirmDialog(this, PROMPT_CLONE, CLONE_SERVER_TITLE,
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      newProfile.setName(selectedProfile.getName());
      newProfile.setUrl(selectedProfile.getUrl());
    }

    final AppServerEditDialog dialog = new AppServerEditDialog(this, newProfile);
    dialog.setVisible(true);

    if (dialog.getResult() == AppServerEditDialog.RESULT_OK) {
      final ServerProfile profile = dialog.getUpdatedProfile();
      config.getServers().add(profile);

      serverPicker.addItem(profile);
      serverPicker.setSelectedIndex(serverPicker.getItemCount() - 1);
      updateButtons();
      return true;
    }
    return false;
  }

  protected boolean handleEditServer(int index) {
    if (index > -1) {
      final ServerProfile profile = (ServerProfile) serverPicker.getSelectedItem();
      final AppServerEditDialog dialog = new AppServerEditDialog(this, profile);
      dialog.setModal(true);
      dialog.setVisible(true);

      if (dialog.getResult() == AppServerEditDialog.RESULT_OK) {

        final ServerProfile editedProfile = dialog.getUpdatedProfile();
        serverPicker.removeItemAt(index);
        serverPicker.insertItemAt(editedProfile, index);
        serverPicker.setSelectedIndex(index);

        updateButtons();
        return true;
      }
    }
    return false;
  }

  protected boolean handleRemoveServer(int index) {
    if (index > -1) {
      final ServerProfile profile = (ServerProfile) serverPicker.getSelectedItem();
      final int result = JOptionPane.showConfirmDialog(this, PROMPT_REMOVE, REMOVE_SERVER_TITLE,
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

      if (result == JOptionPane.YES_OPTION) {
        serverPicker.removeItemAt(index);

        config.getServers().remove(profile);

        updateButtons();
        return true;
      }
    }
    return false;
  }

  protected boolean handleEditProxy() {
    final ProxySettingsDialog dialog = new ProxySettingsDialog(this, config.getProxy());
    dialog.setVisible(true);

    if (dialog.getResult() == ProxySettingsDialog.RESULT_OK) {
      config.setProxy(dialog.getUpdatedSettings());
      return true;
    }
    return false;
  }

  protected boolean handleMakeDefault(int index) {
    if (index > -1) {
      final ServerProfile profile = (ServerProfile) serverPicker.getSelectedItem();
      final int result = JOptionPane.showConfirmDialog(this, PROMPT_MAKE_DEFAULT, MAKE_DEFAULT_TITLE,
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

      if (result == JOptionPane.YES_OPTION) {
        config.setDefaultServerIndex(index);
        updateButtons();
        return true;
      }
    }
    return false;
  }

  @Override
  public void windowClosing(WindowEvent e) {
    onExit();
  }

  @Override
  public void windowActivated(WindowEvent e) {
    // We don't care about this event
  }

  @Override
  public void windowClosed(WindowEvent e) {
    // We don't care about this event
  }

  @Override
  public void windowDeactivated(WindowEvent e) {
    // We don't care about this event
  }

  @Override
  public void windowDeiconified(WindowEvent e) {
    // We don't care about this event
  }

  @Override
  public void windowIconified(WindowEvent e) {
    // We don't care about this event
  }

  @Override
  public void windowOpened(WindowEvent e) {
    // We don't care about this event
  }
}
