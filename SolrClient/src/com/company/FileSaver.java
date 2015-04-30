package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileSaver {

	public static void save(String fileName, String content) throws IOException {
		FileWriter writer = new FileWriter(new File(fileName));
		writer.write(content);
		writer.close();
	}

}
