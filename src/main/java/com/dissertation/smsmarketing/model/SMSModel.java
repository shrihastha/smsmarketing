package com.dissertation.smsmarketing.model;

public class SMSModel {
	private String stockName;
	private String stockOpeningValue;

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public String getStockOpeningValue() {
		return stockOpeningValue;
	}

	public void setStockOpeningValue(String stockOpeningValue) {
		this.stockOpeningValue = stockOpeningValue;
	}

	@Override
	public String toString() {
		return stockName + " Value:" + stockOpeningValue + "\n";
	}
}
