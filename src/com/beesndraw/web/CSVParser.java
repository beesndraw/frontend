package com.beesndraw.web;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {

	int headerRow;
	List<String> rows;
	List<String> cols;
	private String header;

	public CSVParser(int parseInt, String body) {
		this.headerRow = parseInt;
		rows = new ArrayList<String>();
		cols = new ArrayList<String>();
		
		String[] rawRows = body.split(System.lineSeparator());
		for(int i = 0; i < rawRows.length; i++) {
			if( i < headerRow - 1) {
				continue;
			}else if(i == headerRow - 1) {
				String[] rawCols = rawRows[i].split(",");
				header = rawRows[i];
				System.out.println("Found Headers : " + rawRows[i]);
				for(String s: rawCols) {
					cols.add(s);
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
