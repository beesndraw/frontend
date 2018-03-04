package com.beesndraw.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Report {
	enum TradingHours{
		 ONE_FIFTEEN_2_THREE ("01:15 PM PST to 3 pm PST"),
		 THREE_2_MIDNIGHT ("3:00 PM PST to 12 AM PST"),
		 MIDNIGHT_2_SIXTHIRTY("12 AM PST to 6:30 AM PST"),
		 SIXTHIRTY_2_ONE_FIFTEEN ("6:30 AM PST to 01:15 PM");
		
		String v;
		TradingHours(String v) {
			this.v = v;
		}
		
		@Override
		public String toString() {
			return v;
		}
	}

	private List<Trade> trades;
	private Map<String, List<Trade>> tradesByStrategy;
	private Map<String, List<Trade>> afterHourTrades;
	private Map<String, List<Trade>> regularHourTrades;
	public static SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
	Date sixthirity;
	Date onefifteen;
	Date threepm;
	Date midnight;
	
	//id, strategy, side, quantize, amount, price, date/time, datetime, trad pl, pl, position
	//win if -> trad pl > 0
	//total win = sum all wins
	//total loss = total trades - win
	// %
	//max trade pl = 
	public Report(List<Trade> trades) throws ParseException {
		this.trades = trades;
		afterHourTrades = new HashMap<>();
		regularHourTrades = new HashMap<>();
		tradesByStrategy = new LinkedHashMap<>();
		sixthirity = parser.parse("06:30");
		onefifteen = parser.parse("13:15");	
		threepm = parser.parse("15:00");
		midnight = parser.parse("23:59");
		
		processTrades();
	}

	private void processTrades() {
		int totalTrades = this.trades.size();
		if(totalTrades == 0)
			return;
		System.out.println("Total trades: " + totalTrades);

		//6:30 AM PST to 1:15 PM PST, 3:00 PM PST to 12 AM PST, 12 AM PST to 6:30 AM PST
		List<Trade> ah = new ArrayList<Trade>();
		tradesByStrategy.put(TradingHours.MIDNIGHT_2_SIXTHIRTY.toString(), ah);

		List<Trade> rh = new ArrayList<Trade>();
		tradesByStrategy.put(TradingHours.THREE_2_MIDNIGHT.toString(), rh);

		List<Trade> rh2 = new ArrayList<Trade>();
		tradesByStrategy.put(TradingHours.ONE_FIFTEEN_2_THREE.toString(), rh2);

		List<Trade> rh3 = new ArrayList<Trade>();
		tradesByStrategy.put(TradingHours.SIXTHIRTY_2_ONE_FIFTEEN.toString(), rh3);

		for(Trade t: trades) {
			String strategy = t.getStrategy();
			if(strategy.contains("(")) {
				strategy = strategy.substring(0, strategy.indexOf("("));
			}
			if(tradesByStrategy.containsKey(strategy)) {
				tradesByStrategy.get(strategy).add(t);
			}else {
				List<Trade> tt = new ArrayList<Trade>();
				tt.add(t);
				tradesByStrategy.put(strategy, tt);
			}
			TradingHours hours = getHoursStrategy(t.date);
			//System.out.println(t.date.toString() + " -> " + hours);
			tradesByStrategy.get(hours.toString()).add(t);
		}
		System.out.println("===================================");
		System.out.println("Name, Total Trades, Total Wins, Total PL, WinLoss%");
		WinLossReport sum = new WinLossReport("Summary", this.trades);
		System.out.println(String.format("%s,%s,%s,%s,%s", sum.name, sum.totalTrades, sum.totalWins, sum.totalPL, sum.winlossPercentage));
		System.out.println("===================================");
		System.out.println("Break down by strategy");
		System.out.println("===================================");

		for(String name : tradesByStrategy.keySet()) {
			List<Trade> outs = tradesByStrategy.get(name);
			WinLossReport wlr = new WinLossReport(name, outs);
			System.out.println(String.format("%s,%s,%s,%s,%s", name, wlr.totalTrades, wlr.totalWins, wlr.totalPL, wlr.winlossPercentage));
		}
		System.out.println("===================================");
	}
	
	public TradingHours getHoursStrategy(Date d) {
		int hr = d.getHours();
		int min = d.getMinutes();
		
		TradingHours th = TradingHours.SIXTHIRTY_2_ONE_FIFTEEN;
		if(hr == sixthirity.getHours()) {
			//Based on minutes either we are 12 to 6 or 6:30 to 1
			if(min >= 0 && min <= sixthirity.getMinutes()) {
				//System.out.println(d.toString() + " matches 12 AM PST to 6:30 AM PST");
				th = TradingHours.MIDNIGHT_2_SIXTHIRTY;
				return th;
			}
			else {
				//System.out.println(d.toString() + " matches 6:30 AM PST to 01:15 PM");
				th = TradingHours.SIXTHIRTY_2_ONE_FIFTEEN;
				return th;
			}
		}
		
		if(hr == onefifteen.getHours()) {
			if(min >= 0 && min <= onefifteen.getMinutes()) {
				//System.out.println(d.toString() + " matches 6:30 AM PST to 01:15 PM");
				th = TradingHours.MIDNIGHT_2_SIXTHIRTY;
				return th;
			}
			else {
				//System.out.println(d.toString() + " matches 01:15 PM PST to 3 pm PST");
				th = TradingHours.SIXTHIRTY_2_ONE_FIFTEEN;
				return th;
			}
		}
		if( hr >= 0 && hr <= (sixthirity.getHours())) {
			//System.out.println(d.toString() + " matches 12 AM PST to 6:30 AM PST");
			th = TradingHours.MIDNIGHT_2_SIXTHIRTY;
			return th;
		}
		if (hr >= onefifteen.getHours() && hr <= (threepm.getHours())) {
			//System.out.println(d.toString() + " matches 01:15 PM PST to 3 pm PST");
			th = TradingHours.ONE_FIFTEEN_2_THREE;
			return th;
		}
		if ( hr >= (sixthirity.getHours()) && hr <= (onefifteen.getHours())) {
			//System.out.println(d.toString() + " matches 6:30 AM PST to 01:15 PM");
			th = TradingHours.SIXTHIRTY_2_ONE_FIFTEEN;
			return th;
		}
		if ( hr >= (threepm.getHours()) && hr <= 23) {
			//System.out.println(d.toString() + " matches 3:00 PM PST to 12 AM PST");
			th = TradingHours.THREE_2_MIDNIGHT;
			return th;

		}
		return th;
		

		
	}

	public String getReportAsCSV() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Name, Total Trades, Total Wins, Total PL, WinLoss%");
		buffer.append(System.lineSeparator());
		WinLossReport sum = new WinLossReport("Summary", this.trades);
		buffer.append(String.format("%s,%s,%s,%s,%s", sum.name, sum.totalTrades, sum.totalWins, sum.totalPL, sum.winlossPercentage));
		buffer.append(System.lineSeparator());
		for(String name : tradesByStrategy.keySet()) {
			List<Trade> outs = tradesByStrategy.get(name);
			WinLossReport wlr = new WinLossReport(name, outs);
			buffer.append(String.format("%s,%s,%s,%s,%s", name, wlr.totalTrades, wlr.totalWins, wlr.totalPL, wlr.winlossPercentage));
			buffer.append(System.lineSeparator());
		}
		return buffer.toString();
	}

}

class WinLossReport{
	private List<Trade> trades;
	int totalTrades;
	int totalWins;
	int totalLoss;
	double winlossPercentage;
	double totalPL;
	String name;
	
	public WinLossReport(String name, List<Trade> trades) {
		this.name = name;
		this.trades = trades;
		totalWins = 0; 
		totalLoss = 0;
		totalTrades = trades.size();

		totalPL = 0;
		for(Trade t: trades) {
			if(t.getTradePl() > 0) {
				totalWins++;
			}else {
				totalLoss++;
			}
			totalPL += t.getTradePl();
			//System.out.println(t.getId() + " -> " + t.getTradePl() + " -> Cumulative " + totalPL + " From CSV " + t.getProfileLoss());
		}
		winlossPercentage = (totalWins* 100.00)/totalTrades;
	}
	

}
