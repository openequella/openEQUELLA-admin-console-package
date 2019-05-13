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
package org.apereo.openequella.adminconsole.swing;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.apereo.openequella.adminconsole.config.Config;
import org.apereo.openequella.adminconsole.config.ServerProfile;

public class ServerPicker extends JComboBox<ServerProfile> {
	private static final long serialVersionUID = 1L;

	public ServerPicker(Config config){
		super(getServerArray(config));
        setRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    final ServerProfile server = (ServerProfile) value;
                    setText(server.getName());
                }
                return this;
            }
        });
        setPreferredSize(new Dimension(200, 20));

        if (config != null) {
            final Integer defaultServerIndex = config.getDefaultServerIndex();
            if (defaultServerIndex != null) {
                if (defaultServerIndex < getItemCount()) {
                    setSelectedIndex(defaultServerIndex);
                }
            }
        }
	}
	
	private static ServerProfile[] getServerArray(Config config){
		final ServerProfile[] serverArray;
        if (config != null) {
            serverArray = config.getServers().toArray(new ServerProfile[] {});
        } else {
            serverArray = new ServerProfile[] {};
		}
		return serverArray;
	}
}