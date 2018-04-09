package com.beesndraw.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ExtendedReport extends Report{

	public ExtendedReport(List<Trade> trades) throws ParseException {
		super(trades);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getReportAsCSV() {
		StringBuffer buffer = new StringBuffer();
		WinLossReport sum = new WinLossReport("Summary", this.trades);
		buffer.append(sum.headers());
		buffer.append(System.lineSeparator());
		buffer.append(sum.toString());
		buffer.append(System.lineSeparator());
		for(String name : tradesByStrategy.keySet()) {
			List<Trade> outs = tradesByStrategy.get(name);
			WinLossReport wlr = new WinLossReport(name, outs);
			buffer.append(wlr.toString());
			buffer.append(System.lineSeparator());
		}
		buffer.append(System.lineSeparator());
		buffer.append("Statistics, Value");
		buffer.append(System.lineSeparator());
		Statistics statistics = new Statistics(trades);
		buffer.append(statistics.toString());
		buffer.append(System.lineSeparator());

		return buffer.toString();
	}
	
	@Override
	protected void processTrades() {
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
		WinLossReport sum = new WinLossReport("Summary", this.trades);
		System.out.println(sum.headers());
		System.out.println(sum.toString());

		for(String name : tradesByStrategy.keySet()) {
			List<Trade> outs = tradesByStrategy.get(name);
			WinLossReport wlr = new WinLossReport(name, outs);
			System.out.println(wlr.toString());
		}
		
		Statistics statistics = new Statistics(trades);
		System.out.println(statistics.toString());
		System.out.println("===================================");
	}
	
}

