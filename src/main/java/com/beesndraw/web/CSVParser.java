package com.beesndraw.web;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	List<String> rows;
	List<String> cols;
	private String header;

	public CSVParser(String body) {
		rows = new ArrayList<String>();
		cols = new ArrayList<String>();
		
		String[] rawRows = body.split(System.lineSeparator());
		boolean foundHeader = false;
		boolean useAllStringAsHeaderTest = Boolean.valueOf(System.getProperty("useAllStringAsHeaderRequirement","true"));
		boolean useSpecificHeaderMatch = Boolean.getBoolean("useSpecificHeaderMatch");
		
		if(useSpecificHeaderMatch) {
			useAllStringAsHeaderTest = false;
		}
		
		String defaultHeaders = System.getProperty("defaultHeaders", "Id,Strategy,Side,Quantity,Amount");
		for(int i = 0; i < rawRows.length; i++) {
			if(!foundHeader) {
				String headerColumnsTesting = rawRows[i];
				System.out.println("Testing: " + headerColumnsTesting);
				if(useSpecificHeaderMatch) {
					//Id,Strategy,Side,Quantity,Amount,Price,Date/Time,Trade P/L,P/L,Position,
					if(headerColumnsTesting.startsWith(defaultHeaders)) {
						foundHeader = true;
						System.out.println(String.format("Header found due to (%s) : %s", "useSpecificHeaderMatch", headerColumnsTesting));
						header = headerColumnsTesting;
					}
				}else if(useAllStringAsHeaderTest) {
					String[] rawCols = null;
					if(rawRows[i].contains(";")) {
						rawCols = rawRows[i].split(";");
					}else {
						rawCols = rawRows[i].split(",");
					}
					int stringCount = 0;
					int nonEmptyString = 0;
					for(String s: rawCols) {
						if(!s.isEmpty()) {
							nonEmptyString++;
							try {
								Integer.parseInt(s);
							}catch(NumberFormatException nfe) {
								stringCount++;
							}
						}
					}
					if(nonEmptyString == stringCount && nonEmptyString > 3) { //Minimum 3 string needed to qualify for this.
							foundHeader = true;
						System.out.println(String.format("Header found due to (%s) : %s", "useAllStringAsHeaderTest", headerColumnsTesting));
						header = headerColumnsTesting;
					}

				}else {
					throw new RuntimeException("useAllStringAsHeaderRequirement || useSpecificHeaderMatch should be set to true or provided.");
				}
			}else {
				rows.add(rawRows[i]);
			}
		}		
	}

	public List<String> getRecords() {
		return this.rows;
	}
	public List<String> getColumns() {
		return this.cols;
	}
	public String getHeader() {
		return header;
	}

}
