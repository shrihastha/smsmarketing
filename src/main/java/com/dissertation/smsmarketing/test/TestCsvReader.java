package com.dissertation.smsmarketing.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

import com.dissertation.smsmarketing.model.HistoricalPriceModel;

public class TestCsvReader {
	public static void MonthlyStockDetails() throws IOException {
		List<HistoricalPriceModel> historicalPriceModel = parseCSVFileLineByLine();
	}

	private static List<HistoricalPriceModel> parseCSVFileLineByLine() throws IOException {
		Calendar calendar = Calendar.getInstance();
		int currentMonth= calendar.get(Calendar.MONTH);
		int currentYear = calendar.get(Calendar.YEAR);
		int currentDay =calendar.get(Calendar.DATE);
		String uri ="http://real-chart.finance.yahoo.com/table.csv?s=PVOYX&a="+(currentMonth+1)+"&b=1&c="+(currentYear-1)+"&d="+currentMonth+"&e="+currentDay+"&f="+currentYear+"&g=m&ignore=.csv";
		URL stockURL = new URL(uri);
		BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
		CSVReader reader = new CSVReader(in);
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
		reader.close();
		in.close();
		return monthlyDetails;
	}

	public static void main(String a[]) throws IOException {
		MonthlyStockDetails();
	}
}
