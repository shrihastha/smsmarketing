package com.dissertation.smsmarketing.model;

public class HistoricalPriceModel {

	private String allDate;
	private String open;
	private String high;
	private String low;
	private String close;
	private String avgVolume;
	private String adj;
	private String adjClose;

	public String getAllDate() {
		return allDate;
	}

	public void setAllDate(String allDate) {
		this.allDate = allDate;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public String getAvgVolume() {
		return avgVolume;
	}

	public void setAvgVolume(String avgVolume) {
		this.avgVolume = avgVolume;
	}

	public String getAdj() {
		return adj;
	}

	public void setAdj(String adj) {
		this.adj = adj;
	}

	public String getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(String adjClose) {
		this.adjClose = adjClose;
	}
	
	@Override
	public String toString(){
		//return "Historical Date"+allDate+" Open"+open+" High"+high+" Low"+low+" Close"+close+" Average Volume"+avgVolume+" Adjust Close"+adjClose+"\n";
		return allDate+" "+adjClose;
	}
}
