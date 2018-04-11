package com.beesndraw.web;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class Statistics {

	private List<Trade> trades;
	private String startDate;
	private String endDate;
	private String symbol;
	private String fees;
	private long totalWeeks;
	private double profitPerWeek;
	

	public Statistics(List<Trade> trades) {
		this.trades = trades;
		symbol = System.getProperty("symbol", "NotFound");
		fees = System.getProperty("transactionFee", "2.0");
		WinLossReport sum = new WinLossReport("Summary", this.trades);
		
		Date start_Date = this.trades.get(0).getDate();
		Date end_Date = this.trades.get(this.trades.size() - 1).getDate();
		
		startDate = start_Date.toGMTString();
		endDate = end_Date.toGMTString();

		LocalDate pstartDate = start_Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	    LocalDate pendDate = end_Date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	    totalWeeks = ChronoUnit.WEEKS.between(pstartDate, pendDate);

	    if(totalWeeks > 0)
			profitPerWeek = sum.netProfitLessFees / totalWeeks;
		
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Symbol,");
		buffer.append(symbol);
		buffer.append(System.lineSeparator());
		buffer.append("Fees,");
		buffer.append(fees);
		buffer.append(System.lineSeparator());
		buffer.append("Start Date,");
		buffer.append(startDate);
		buffer.append(System.lineSeparator());
		buffer.append("End Date,");
		buffer.append(endDate);
		buffer.append(System.lineSeparator());
		buffer.append("Total Weeks,");
		buffer.append(totalWeeks);
		buffer.append(System.lineSeparator());
		buffer.append("Net P/L Per Week,");
		buffer.append(profitPerWeek);
		buffer.append(System.lineSeparator());
		return buffer.toString();
	}

}
