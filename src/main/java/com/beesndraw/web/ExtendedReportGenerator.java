package com.beesndraw.web;

import java.text.ParseException;
import java.util.List;

public class ExtendedReportGenerator extends ReportGenerator{

	public ExtendedReportGenerator(String path, String inputfile, String output) {
		super(path, inputfile, output);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ExtendedReport prepareReport(List<Trade> trades) throws ParseException {
		ExtendedReport report = new ExtendedReport(trades);
		return report;
	}
}
