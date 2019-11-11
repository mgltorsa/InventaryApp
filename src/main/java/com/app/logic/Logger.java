package com.app.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;

public class Logger {

	
	private static final String dir = "./data/logs.log";
	
	private static Logger logger;
	
	private PrintWriter writer;
	
	private Logger() {
		try {
		File file = new File(dir);
		if(!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw;
		
			fw = new FileWriter(file, true);
			writer = new PrintWriter(fw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Logger getLogger() {
		if(logger==null) {
			logger = new Logger();
		}
		return logger;
	}
	
	public void log(Exception e) {
		
		writer.write("Logs on: "+LocalDate.now().toString()+"\n");
		e.printStackTrace(writer);
		writer.write("\n");
		writer.flush();
	}

	public void close() {
		writer.close();
		
	}
	
	
}
