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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.apereo.openequella.adminconsole.config.Config;
import org.apereo.openequella.adminconsole.config.ServerProfile;
import org.apereo.openequella.adminconsole.swing.ComponentHelper;
import org.apereo.openequella.adminconsole.swing.ServerPicker;
import org.apereo.openequella.adminconsole.swing.TableLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configurator extends JFrame implements WindowListener, ActionListener {

    private static final long serialVersionUID = 4440091132569751172L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);

    private static final int WINDOW_WIDTH = 450;

    private final Config config;

    private final ServerPicker serverPicker;
    private final JButton addServerButton;
    private final JButton editServerButton;
    private final JButton removeServerButton;
    private final JButton editProxyButton;

    public static void main(String[] args) {
        new Configurator();
    }

    public Configurator() {
        try {
            config = readConfig();
            serverPicker = new ServerPicker(config);
            addServerButton = new JButton("Add Server...");
            editServerButton = new JButton("Edit Server...");
            removeServerButton = new JButton("Remove Server");
            editProxyButton = new JButton("Proxy Settings...");

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            setTitle("Admin console configurator");
            setResizable(false);
            addWindowListener(this);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            JPanel serverPanel = createServerPanel();
            JPanel buttonsPanel = createButtonsPanel();

            final int height1 = serverPanel.getMinimumSize().height;
            final int[] rows = { height1, buttonsPanel.getPreferredSize().height };
            final int[] cols = { WINDOW_WIDTH };

            final TableLayout layout = new TableLayout(rows, cols);
            JPanel all = new JPanel(layout);
            all.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            all.add(serverPanel, new Rectangle(0, 0, 1, 1));
            all.add(buttonsPanel, new Rectangle(0, 1, 1, 1));

            getContentPane().add(all);
            getRootPane().setDefaultButton(addServerButton);
            addWindowListener(this);
            pack();

            ComponentHelper.centreOnScreen(this);
            setVisible(true);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Config readConfig() {
        try {
            return Config.readServerConfigFile();
        } catch (Exception e) {
            LOGGER.warn("Error reading configuration file", e);
            return new Config();
        }
    }

    protected JPanel createServerPanel() {

        serverPicker.addActionListener(this);

        JLabel serversLabel = new JLabel("Servers:");

        final int height1 = serverPicker.getPreferredSize().height;
        final int[] rows = { height1, height1 };
        final int[] cols = { TableLayout.FILL };

        JPanel serversPanel = new JPanel(new TableLayout(rows, cols));
        serversPanel.add(serversLabel, new Rectangle(0, 0, 1, 1));
        serversPanel.add(serverPicker, new Rectangle(0, 1, 1, 1));
        return serversPanel;
    }

    protected JPanel createButtonsPanel() {

        addServerButton.addActionListener(this);
        editServerButton.addActionListener(this);
        removeServerButton.addActionListener(this);
        editProxyButton.addActionListener(this);

        final int height1 = addServerButton.getPreferredSize().height;
        final int width1 = removeServerButton.getPreferredSize().width;
        final int width2 = editProxyButton.getPreferredSize().width;
        final int width3 = Math.max(width1, width2);
        final int[] rows = { height1, height1 };
        final int[] cols = { TableLayout.FILL, width3, width3, width3, TableLayout.FILL, };

        JPanel buttonsPanel = new JPanel(new TableLayout(rows, cols));

        buttonsPanel.add(addServerButton, new Rectangle(1, 0, 1, 1));
        buttonsPanel.add(editServerButton, new Rectangle(2, 0, 1, 1));
        buttonsPanel.add(removeServerButton, new Rectangle(3, 0, 1, 1));
        buttonsPanel.add(editProxyButton, new Rectangle(2, 1, 1, 1));

        return buttonsPanel;
    }

    protected void onExit() {
        dispose();
        System.exit(0);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        onExit();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Object source = e.getSource();
        final int index = serverPicker.getSelectedIndex();

        boolean write = false;
        if (source == addServerButton) {
            write = handleAddServer();
        } else if (source == editServerButton) {
            write = handleEditServer(index);
        } else if (source == removeServerButton) {
            write = handleRemoveServer(index);
        } else if (source == editProxyButton) {
            write = handleEditProxy();
        }

        if (write) {
            config.writeServerConfigFile();
        }
    }

    private void updateButtons() {
        boolean enable = (serverPicker.getSelectedIndex() > -1);
        editServerButton.setEnabled(enable);
        removeServerButton.setEnabled(enable);
    }

    protected boolean handleAddServer() {
        final ServerProfile selectedProfile = (ServerProfile) serverPicker.getSelectedItem();
        final ServerProfile newProfile = new ServerProfile();
        if (selectedProfile != null
                && JOptionPane.showConfirmDialog(this, "Do you want to clone the currently selected server?", "Clone?",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            newProfile.setName(selectedProfile.getName());
            newProfile.setUrl(selectedProfile.getUrl());
        }

        final AppServerEditDialog dialog = new AppServerEditDialog(this, newProfile);
        dialog.setModal(true);
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
            AppServerEditDialog dialog = new AppServerEditDialog(this, profile);
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
            final int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this server?",
                    "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

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
}
