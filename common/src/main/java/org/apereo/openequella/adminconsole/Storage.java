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