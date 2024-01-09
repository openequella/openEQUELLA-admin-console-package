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

import java.awt.Dimension;
import java.awt.Image;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import org.apereo.openequella.adminconsole.swing.ComponentHelper;

public class LoadingDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	public LoadingDialog(JFrame parent, List<Image> icons, String title) {
		super(parent);
		setTitle(title);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		setIconImages(icons);
		setSize(new Dimension(200, 30));
		final JProgressBar loadingProgressBar = new JProgressBar();
		loadingProgressBar.setIndeterminate(true);
		add(loadingProgressBar);
	}

	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		ComponentHelper.centreOnScreen(this);
	}
}