package com.beesndraw.web;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

	static DateFormat DATE_FORMAT;
	private String inputfile;
	private String output; 

	public ReportGenerator(String path, String inputfile, String output) {
		this.inputfile = inputfile;
		this.output = output;
	}



//	public static void main(String[] args) throws Exception {
//		//System.setProperty("inputFile", "/Users/i837717/Desktop/beesndraw/input.csv");
//		//System.setProperty("outputFile", "/Users/i837717/bijenproject/ReportGenerator/src/main/output.csv");
//
//		System.out.println(System.getProperty("inputFile"));
//
//		if(System.getProperty("inputFile") == null || System.getProperty("inputFile").isEmpty()) 
//			printUsage();
//		else {
//			DATE_FORMAT = new SimpleDateFormat(ReportGenerator.getString("dateFormat","MM/dd/yyyy HH:mm"));
//			System.out.println("Reading data from : " + getString("inputFile","input.csv"));
//			System.out.println("Header row is at  : " + getInteger("headerRow",6));
//			CSVParser parsedFile = parseFile(getString("inputFile","input.csv"));
//			String header = parsedFile.getHeader();
//			System.out.println("Header row is  : " + header);			
//			List<Trade> trades = getTrades(parsedFile.getRecords());
//			Report report = prepareReport(trades);
//			if(getString("outputFile", null) != null) {
//				try {
//					saveReport(report, getString("outputFile",null));
//				} catch (Exception e) {
//					System.err.println("Failed to save file ");
//					e.printStackTrace();
//				}
//			}else {
//				//printReport(report);
//			}
//		}
//		System.exit(0);
//	}



	public void saveReport(Report report, String string) throws Exception {
		System.out.println("Saving to file " + string);
		StringBuffer buffer = new StringBuffer();
		buffer.append(report.getReportAsCSV());
		FileSystemUtils.saveFile(string, buffer.toString());
	}

	public Report prepareReport(List<Trade> trades) throws ParseException {
		Report report = new Report(trades);
		return report;
	}

	public List<Trade> getTrades(List<String> records) {
		List<Trade> trades = new ArrayList<Trade>();
		for(String string: records) {
			Trade t = Trade.parseString(string);
			if(t != null)
				trades.add(t);
		}
		return trades;
	}

	public CSVParser parseFile(String string) throws Exception {
		File file = new File(string);
		if(!file.exists())
			throw new Exception("File not found: Please provide full path. " + string);
		String body = FileSystemUtils.readFile(string);
		CSVParser parser = new CSVParser(body);
		return parser;
	}

//	private static void printUsage() {
//		System.out.println("===================================");
//		System.out.println("ReportGenerator");
//		System.out.println("===================================");
//		System.out.println("Inputs:");
//		System.out.println("inputFile: File to read Trading Record from");
//		System.out.println("outputFile: Files to write Trading Record from");
//		System.out.println("===================================");
//		System.out.println("java -DinputFile=./parsed.csv -DoutputFile=./output.csv -jar ReportGenerator.jar ");
//		System.out.println("\n\n\t\t Reads the csv file, and write output to output.csv in current directlry");
//		System.out.println("===================================");
//
//	}

	public static boolean isSet(String key) {
		return getProperty(key) != null;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		if (isSet(key)) {
			return Boolean.getBoolean(key);
		} else {
			return defaultValue;
		}
	}

	public static Integer getInteger(String key, Integer defaultValue) {
		if (isSet(key)) {
			return Integer.getInteger(key);
		} else {
			return defaultValue;
		}
	}
	public static String getString(String key, String defaultValue) {
		if (isSet(key)) {
			return System.getProperty(key);
		} else {
			return defaultValue;
		}
	}
	public static Object getProperty(String key) {
		if (System.getenv(key) != null) {
			String env = System.getenv(key);
			System.setProperty(key, env);
		} else if (System.getProperty(key) != null) {
			return System.getProperty(key);
		}
		return null;
	}



	public void generate() throws Exception {
		DATE_FORMAT = new SimpleDateFormat(ReportGenerator.getString("dateFormat","MM/dd/yyyy HH:mm"));
		System.out.println("Reading data from : " + inputfile);
		CSVParser parsedFile = parseFile(inputfile);
		String header = parsedFile.getHeader();
		System.out.println("Header row is  : " + header);			
		List<Trade> trades = getTrades(parsedFile.getRecords());
		Report report = prepareReport(trades);
		if(output != null) {
			try {
				saveReport(report, output);
			} catch (Exception e) {
				throw e;
			}
		}else {
			throw new Exception("Missing output file");
		}
	}

}
