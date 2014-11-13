package com.dissertation.smsmarketing.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dissertation.smsmarketing.exception.SMSMarketingDataException;
import com.dissertation.smsmarketing.service.IStockDataService;

@Controller
public class StockController{

	@Autowired
	IStockDataService messageService;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public IStockDataService getService() {
		return messageService;
	}

	public void setService(IStockDataService service) {
		this.messageService = service;
	}

	@RequestMapping(value = "/received", method = RequestMethod.GET)
	public @ResponseBody
	byte[] processSMSrequest(@RequestParam("stockMessage") String stockMessage,@RequestParam("phoneNumber") String phoneNumber,
			HttpServletResponse response) throws SMSMarketingDataException {
		byte[] stockData = null;
		try {
			String message = stockMessage.toUpperCase().trim();
			stockData = messageService.retriveStockData(message,phoneNumber);
		} catch (Exception e) {
			stockData = e.getMessage().getBytes();
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return stockData;
	}
}