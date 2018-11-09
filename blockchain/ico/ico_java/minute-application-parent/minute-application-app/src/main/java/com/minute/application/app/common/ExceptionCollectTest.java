package com.minute.application.app.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ExceptionCollectTest {
	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter out = null;
		try {
			int i = 100 / 0;
			System.out.println(i);
		} catch (Exception e) {
			e.printStackTrace();
			File file = new File("D://TEMP.txt");
			out = new PrintWriter(new FileOutputStream(file));
			e.printStackTrace(out);

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
