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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.apereo.openequella.adminconsole.JarService;
import org.apereo.openequella.adminconsole.Storage;
import org.apereo.openequella.adminconsole.config.Config;
import org.apereo.openequella.adminconsole.config.ProxySettings;
import org.apereo.openequella.adminconsole.config.ServerProfile;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.ServerPicker;
//import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
//import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;
//import org.springframework.remoting.support.RemoteInvocation;
import org.apereo.openequella.adminconsole.swing.TableLayout;
import org.apereo.openequella.adminconsole.util.ExecUtils.ExecResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientLauncher extends JFrame implements ActionListener, WindowListener {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = LoggerFactory.getLogger(ClientLauncher.class);

  private static final int WINDOW_WIDTH = 450;

  private static final String WINDOW_TITLE = "openEQUELLA administration console launcher";
  private static final String LABEL_SERVER = "Server:";
  private static final String BUTTON_LAUNCH = "Launch";
  private static final String BUTTON_EXIT = "Exit";

  private Config config;

  private final ServerPicker serverPicker;
  private final JButton connect;
  private final JButton exit;

  private TableLayout layout;

  public static void main(String args[]) throws Exception {
    new ClientLauncher();
  }

  public ClientLauncher() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      BlindSSLSocketFactory.register();

      config = Config.readServerConfigFile();
      serverPicker = new ServerPicker(config);
      connect = new JButton(BUTTON_LAUNCH);
      exit = new JButton(BUTTON_EXIT);

      final JComponent serverList = createServerList();
      final JComponent connectExit = createConnectExit();

      final int height1 = serverList.getMinimumSize().height;
      final int height2 = connectExit.getMinimumSize().height;
      final int[] rows = { height1, 0, height2, };
      final int[] cols = { WINDOW_WIDTH, };

      layout = new TableLayout(rows, cols);
      JPanel all = new JPanel(layout);
      all.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      all.add(serverList, new Rectangle(0, 0, 1, 1));
      all.add(connectExit, new Rectangle(0, 2, 1, 1));

      updateButtons();

      getContentPane().add(all);
      getRootPane().setDefaultButton(connect);

      pack();

      setTitle(WINDOW_TITLE);
      setResizable(false);
      addWindowListener(this);
      setDefaultCloseOperation(EXIT_ON_CLOSE);

      ComponentHelper.centreOnScreen(this);
      setVisible(true);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private JComponent createServerList() {
    JLabel profileLabel = new JLabel(LABEL_SERVER);

    final int height1 = profileLabel.getPreferredSize().height;
    final int height2 = serverPicker.getPreferredSize().height;

    final int[] rows = { height1, height2 };
    final int[] cols = { TableLayout.FILL, serverPicker.getPreferredSize().width };

    JPanel all = new JPanel(new TableLayout(rows, cols));
    all.add(profileLabel, new Rectangle(0, 0, 2, 1));
    all.add(serverPicker, new Rectangle(0, 1, 2, 1));
    return all;
  }

  public JComponent createConnectExit() {

    connect.addActionListener(this);
    exit.addActionListener(this);

    final int width1 = connect.getPreferredSize().width;
    final int height1 = connect.getPreferredSize().height;
    final int[] rows = { height1 };
    final int[] cols = { TableLayout.FILL, width1, width1, TableLayout.FILL };

    JPanel all = new JPanel(new TableLayout(rows, cols, 5, 5));

    all.add(connect, new Rectangle(1, 0, 1, 1));
    all.add(exit, new Rectangle(2, 0, 1, 1));

    return all;
  }

  private void updateButtons() {
    boolean enable = serverPicker.getSelectedIndex() > -1;
    connect.setEnabled(enable);
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
              "-Djnlp.ENDPOINT=" + url, "-Dplugin.cache.dir=" + Storage.getFolder("cache"));

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
    if (source == connect) {
      launch((ServerProfile) serverPicker.getSelectedItem());
    } else if (source == exit) {
      onExit();
    }
  }

  private void onExit() {
    dispose();
    System.exit(0);
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
