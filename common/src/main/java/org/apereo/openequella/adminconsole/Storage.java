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
package org.apereo.openequella.adminconsole;

import java.io.File;

public class Storage {
	private static File getBaseFolder(){
		final File homeFolder = new File(System.getProperty("user.home"));
        final File adminConsoleHomeFolder = new File(homeFolder, "openequella-admin-console");
        if (!adminConsoleHomeFolder.exists()){
            adminConsoleHomeFolder.mkdir();
		}
		return adminConsoleHomeFolder;
	}

	public static File getFile(String fileName){
		return new File(getBaseFolder(), fileName);
	}

	public static File getFile(File folder, String fileName){
		return new File(folder, fileName);
	}

	public static File getFolder(String folderName){
		final File folder = new File(getBaseFolder(), folderName);
		if (!folder.exists()){
			folder.mkdir();
		}
		return folder;
	}
}