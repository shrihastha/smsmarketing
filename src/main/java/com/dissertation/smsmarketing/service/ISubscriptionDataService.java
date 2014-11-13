package com.dissertation.smsmarketing.service;

import java.util.List;

import com.dissertation.smsmarketing.exception.SMSMarketingServiceException;

public interface ISubscriptionDataService {
	List<String> stockOptions() throws SMSMarketingServiceException;

	boolean subscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail,String email_id) throws SMSMarketingServiceException;

	boolean unsubscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail) throws SMSMarketingServiceException;
}
