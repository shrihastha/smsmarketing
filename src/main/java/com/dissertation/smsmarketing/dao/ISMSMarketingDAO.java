package com.dissertation.smsmarketing.dao;

import java.util.List;

import com.dissertation.smsmarketing.exception.SMSMarketingDAOException;

public interface ISMSMarketingDAO {
	List<String> stockOptions() throws SMSMarketingDAOException;

	boolean subscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail,String email_id) throws SMSMarketingDAOException;

	boolean unsubscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail) throws SMSMarketingDAOException;
}
