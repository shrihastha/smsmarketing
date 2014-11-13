package com.dissertation.smsmarketing.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.dissertation.smsmarketing.exception.InvalidTickerException;
import com.dissertation.smsmarketing.model.HistoricalPriceModel;

public class StockDetails {
	private static Logger log = LoggerFactory.getLogger("StockDetails");

	private static String createURL(String stockName) {
		final Calendar calendar = Calendar.getInstance();
		final int currentMonth = calendar.get(Calendar.MONTH);
		final int currentYear = calendar.get(Calendar.YEAR);
		final int currentDay = calendar.get(Calendar.DATE);
		String url = "http://real-chart.finance.yahoo.com/table.csv?s="
				+ stockName + "&a=" + (currentMonth + 1) + "&b=1&c="
				+ (currentYear - 1) + "&d=" + currentMonth + "&e=" + currentDay
				+ "&f=" + currentYear + "&g=m&ignore=.csv";
		log.info(url);
		return url;
	}
	public static List<HistoricalPriceModel> getHistoricalStockDetails(String stockName) throws InvalidTickerException, IOException {
		try{
			String stockURL = createURL(stockName);
			URL url = new URL(stockURL);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
			CSVReader reader = new CSVReader(bufferedReader);
			List<HistoricalPriceModel> monthlyDetails = new ArrayList<HistoricalPriceModel>();
			// read line by line
			String[] record = null;
			// skip header row
			reader.readNext();
			while ((record = reader.readNext()) != null) {
				HistoricalPriceModel details = new HistoricalPriceModel();
				details.setAllDate(record[0]);
				details.setOpen(record[1]);
				details.setHigh(record[2]);
				details.setLow(record[3]);
				details.setClose(record[4]);
				details.setAvgVolume(record[5]);
				details.setAdjClose(record[6]);
				monthlyDetails.add(details);
			}
			log.info("records" + monthlyDetails.size());
			reader.close();
			bufferedReader.close();
			return monthlyDetails;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Invalid ticker symbol {}",e.getMessage());
			throw new InvalidTickerException("Invalid ticker symbol");
		}
	}
	
	public static boolean validStockUrl(String stockSymbol) throws InvalidTickerException{
		String stockURL = "http://real-chart.finance.yahoo.com/table.csv?s="+ stockSymbol;
		boolean validStock = false;
		try {
			URL url = new URL(stockURL);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
			validStock = true;
		}
		catch(Exception e){
			throw new InvalidTickerException("Invalid ticker symbol");
		}
		return validStock;
	}
}