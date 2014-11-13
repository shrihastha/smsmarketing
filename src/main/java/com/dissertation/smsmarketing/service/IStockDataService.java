package com.dissertation.smsmarketing.service;

import com.dissertation.smsmarketing.exception.InvalidCodeException;
import com.dissertation.smsmarketing.exception.InvalidTickerException;
import com.dissertation.smsmarketing.exception.SMSMarketingDataException;

public interface IStockDataService {

	public byte [] retriveStockData(String stockMessage,String phoneNumber) throws SMSMarketingDataException,InvalidCodeException,InvalidTickerException;
}
