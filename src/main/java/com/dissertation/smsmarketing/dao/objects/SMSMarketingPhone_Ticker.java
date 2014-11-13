package com.dissertation.smsmarketing.dao.objects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SMSMarketingPhone_Ticker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "phone_number", nullable = false, unique = true)
	private String phone_number;

	@Column(name = "ticker_symbol", nullable = false, unique = true)
	private String ticker_symbol;
	
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getTicker_symbol() {
		return ticker_symbol;
	}

	public void setTicker_symbol(String ticker_symbol) {
		this.ticker_symbol = ticker_symbol;
	}
	
}