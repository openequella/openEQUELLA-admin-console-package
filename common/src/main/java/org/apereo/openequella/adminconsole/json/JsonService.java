package org.apereo.openequella.adminconsole.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonService {
	
	private static final ObjectMapper jsonMapper = new ObjectMapper();
	
	public static <T> T readFile(File file, Class<T> type){
		try (final InputStream fis = new FileInputStream(file)) {
			final T obj = jsonMapper.readValue(fis, type);
			return obj;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeFile(File file, Object obj){
		try (final OutputStream fos = new FileOutputStream(file)) {
            jsonMapper.writeValue(fos, obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
}