package com.beesndraw.web;

import java.text.ParseException;
import java.util.Date;

public class Trade {
	int id;
	String strategy;
	String side;
	double quantity;
	double amount;
	double price;
	Date date;
	double tradePl;
	double profileLoss;
	String position;
	boolean round;

	public Trade(int id, String strategy, String side, double quantity, double amount, double price, Date date, double tradePl,
			double profileLoss, String position, boolean round) {
		super();
		this.id = id;
		this.strategy = strategy;
		this.side = side;
		this.quantity = quantity;
		this.amount = amount;
		this.price = price;
		this.date = date;
		this.tradePl = tradePl;
		this.profileLoss = profileLoss;
		this.position = position;
		this.round = round;
	}

	public boolean isRound() {
		return this.round;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public double getTradePl() {
		return tradePl;
	}
	public double getProfileLoss() {
		return profileLoss;
	}
	public void setProfileLoss(double profileLoss) {
		this.profileLoss = profileLoss;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	
	public static Trade parseString(String string) {
		int i = 0; 
		try {
			int id;
			String strategy;
			String side;
			double quantity;
			double amount;
			double price;
			Date date;
			double tradePl;
			double profileLoss;
			String position;
			//Id,Strategy,Side,Quantity,Amount,Price,Date/Time,Trade P/L,P/L,Position,,,,,
			//[1, Double_D_Strat_Alert_1(Sell @1.0324), Sell to Open, -1, -125000, $1.03 , 6/20/17 19:55, , $12.50 , -125000]
			String seperator = ",";
			if(string.contains(";")) {
				seperator = ";";
			}
			String [] rawData = string.split(seperator);
			if(rawData.length == 0) {
				System.out.println("Ingonred : " +  string);
				return null; //This row is proabaly not valid data. log this so we dont' loose this infor
			}
			try {
				id = Integer.parseInt(rawData[i++]);
			}catch(NumberFormatException e) {
				System.out.println("Ingonred : " +  string);
				return null; //This row is proabaly not valid data.
			}
			strategy = rawData[i++];
			side = rawData[i++];
			try {
				quantity = Double.parseDouble(rawData[i++]);
			}catch(NumberFormatException e) {
				throw e;
			}
			try{
				amount = Double.parseDouble(rawData[i++]);
			}catch(NumberFormatException e) {
				throw e;
			}

			String priceString = rawData[i++];
			price = parseDouble(priceString);

			date = null; //6/20/17 19:55
			String dateStr = rawData[i++];
			try {
				date = ReportGenerator.DATE_FORMAT.parse(dateStr);
			} catch (ParseException e) {
			    e.printStackTrace();
			    date = null;
			}
			String tradePLString = rawData[i++];
//			if(tradePLString.isEmpty())
//				return null;
			tradePl = parseDouble(tradePLString);
			boolean round = false;
			if(!tradePLString.isEmpty())
				round = true;
			String profiltLossString = rawData[i++];

			profileLoss = parseDouble(profiltLossString);
			position = rawData[i++];;
			return new Trade(id, strategy, side,quantity, amount, price, date, tradePl, profileLoss, position, round);			
		}catch(Exception e) {
			System.err.println("Skipping this row. Failed to parse record: " + string);
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	static double parseDouble(String data) {
		if(!data.isEmpty()) {
			try {
				double multipler = 1;
				if(data.contains(","))
					data = data.replaceAll(",", "");
				if(data.startsWith("(")) {
					data = data.substring(1);
					multipler = -1;
				}
				if(data.startsWith("\"")) {
					data = data.substring(1);
				}
				if(data.startsWith("$")) {
					data = data.substring(1);
				}
				
				if(data.endsWith(")")) {
					data = data.substring(0, data.length() - 1);
				}
				if(data.endsWith("\"")) {
					data = data.substring(0, data.length() - 1);
				}
				 return Double.parseDouble(data) * multipler;
			}catch(NumberFormatException e) {
				throw e;
			}
		}
		else {
			return 0.0;
		}
	}

	@Override
	public String toString() {
		return "Trade [id=" + id + ", strategy=" + strategy + ", side=" + side + ", quantity=" + quantity + ", amount="
				+ amount + ", price=" + price + ", date=" + date + ", tradePl=" + tradePl + ", profileLoss="
				+ profileLoss + ", position=" + position + "]";
	}


}
