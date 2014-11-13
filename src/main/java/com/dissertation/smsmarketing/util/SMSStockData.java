package com.dissertation.smsmarketing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dissertation.smsmarketing.exception.InvalidTickerException;
import com.dissertation.smsmarketing.model.SMSModel;

public class SMSStockData {
	private static Logger log = LoggerFactory.getLogger(SMSStockData.class);

	public static List<SMSModel> fetchSMSCSV(String stockSymbol)throws IOException,InvalidTickerException {
		try{
			String uri = "http://finance.yahoo.com/d/quotes.csv?s=" + stockSymbol + "&f=no";
			log.info("stock URL {}",uri);
			URL stockURL = new URL(uri);
			BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
			List<SMSModel> listSMS = new ArrayList<SMSModel>();
			String record = "";
			String[] eachRecord = null;
			while ((record = in.readLine()) != null) {
				record = record.replace("\"", "");
				eachRecord = record.split(",");
			}
			SMSModel smsModel = new SMSModel();
			smsModel.setStockName(eachRecord[0]);
			smsModel.setStockOpeningValue(eachRecord[1]);
			listSMS.add(smsModel);
			in.close();
			return listSMS;
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("Exception {}", e);
			throw new InvalidTickerException("Invalid Ticker Symbol");
		}
	}
}