package com.beesndraw.web;

import java.util.ArrayList;
import java.util.List;

public class WinLossReport{
	protected List<Trade> trades;
	int totalTrades;
	int totalRounds;
	int totalWins;
	int totalLoss;
	double winlossPercentage;
	double totalPL;
	String name;
	double fee;
	double avgWinsPerRound;
	double avgLossPerRound;
	double profitFactor;
	double maxWin;
	double maxLoss;
	double netProfitLessFees;
	double totalFees;
	
	public WinLossReport(String name, List<Trade> trades) {
		this.name = name;
		this.trades = trades;
		totalWins = 0; 
		totalLoss = 0;
		totalTrades = trades.size();
		fee = Double.parseDouble(System.getProperty("transactionFee", "2.0"));
		
		totalPL = 0;
		double sumWin = 0; double sumLoss = 0;
		List<Trade> rounds = new ArrayList<>();
		for(Trade t: trades) {
			if(t.getTradePl() > 0) {
				rounds.add(t);
			}
		}
		totalRounds = rounds.size();
		
		for(Trade t: rounds) {
			if(t.getTradePl() > 0) {
				totalWins++;
				sumWin += t.getTradePl();
			}else {
				totalLoss++;
				sumLoss += t.getTradePl();
			}
			totalPL += t.getTradePl();
			//System.out.println(t.getId() + " -> " + t.getTradePl() + " -> Cumulative " + totalPL + " From CSV " + t.getProfileLoss());
		}
		
		
		
		if(totalRounds > 0)
			winlossPercentage = (totalWins* 100.00)/totalRounds;

		if(totalWins > 0)
			avgWinsPerRound = sumWin / (totalWins * 1.0);
		if(totalLoss > 0)
			avgLossPerRound = sumLoss / (totalLoss * 1.0);
		if(avgLossPerRound != 0)
			profitFactor = avgWinsPerRound / avgLossPerRound;
		maxWin = getMaxWin(trades);
		maxLoss = getMaxLoss(trades);
		totalFees = (fee * totalTrades);
		netProfitLessFees = totalPL - totalFees;
	}


	public String headers() {
		return ("Name, Total Rounds, Total Wins, Total Loss, Total PL, WinLoss%, Avg Wins Per Round, Avg Loss Per Round, Profit Factor, Max Win Gross P/L, Max Loss Gross P/L,Total Orders, Total Fees, Total Net P/L");
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(name);
		buffer.append(",");
		buffer.append(totalRounds);
		buffer.append(",");
		buffer.append(totalWins);
		buffer.append(",");
		buffer.append(totalLoss);
		buffer.append(",");
		buffer.append(totalPL);
		buffer.append(",");
		buffer.append(winlossPercentage);
		buffer.append(",");
		buffer.append(avgWinsPerRound);
		buffer.append(",");
		buffer.append(avgLossPerRound);
		buffer.append(",");
		buffer.append(profitFactor);
		buffer.append(",");
		buffer.append(maxWin);
		buffer.append(",");
		buffer.append(maxLoss);
		buffer.append(",");
		buffer.append(totalTrades);
		buffer.append(",");
		buffer.append(totalFees);
		buffer.append(",");
		buffer.append(netProfitLessFees);
		return buffer.toString();
	}


	private double getMaxLoss(List<Trade> trades2) {
		double maxLoss = 0;
		for(Trade t: trades) {
			if(t.getTradePl() < maxLoss)
				maxLoss = t.getTradePl();
		}
		return maxLoss;
	}

	private double getMaxWin(List<Trade> trades2) {
		double maxWin = 0;
		for(Trade t: trades) {
			if(t.getTradePl() > maxWin)
				maxWin = t.getTradePl();
		}
		return maxWin;
	}
	

}
