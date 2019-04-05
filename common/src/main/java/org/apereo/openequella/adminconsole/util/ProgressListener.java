package org.apereo.openequella.adminconsole.util;

public interface ProgressListener {
	void start();
  
	void total(int i);
  
	void add(int i);
  
	void complete();
  }