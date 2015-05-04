package file.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class InfileGenerator {

	public static void main(String[] args) throws IOException {
		infileGenerator();
	}
	
	public static void infileGenerator() throws IOException {
		String saveTo = "/Users/ruishan/infile.txt";

		String path = "/Users/ruishan/book_covers/Reference/0";
		String sufix = ".jpg";

		JsonArray jsonArray = new JsonArray();

		for (int i = 1; i < 100; i++) {
			JsonObject obj = new JsonObject();
			String temp;
			if (i < 10) {
				temp = "0" + String.valueOf(i);
			} else {
				temp = String.valueOf(i);
			}
			obj.addProperty("path", path + temp + sufix);
			obj.addProperty("ISBN", i);
			jsonArray.add(obj);
		}
		File out = new File(saveTo);
		FileWriter jsonFileWriter = new FileWriter(out);
		jsonFileWriter.write(jsonArray.toString());
		jsonFileWriter.flush();
		jsonFileWriter.close();

	}

}
