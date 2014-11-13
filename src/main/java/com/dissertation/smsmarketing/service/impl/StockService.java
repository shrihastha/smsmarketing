package com.dissertation.smsmarketing.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissertation.smsmarketing.constants.SMSMarketingConstants;
import com.dissertation.smsmarketing.dao.ISMSMarketingDAO;
import com.dissertation.smsmarketing.exception.InvalidCodeException;
import com.dissertation.smsmarketing.exception.InvalidTickerException;
import com.dissertation.smsmarketing.exception.SMSMarketingDAOException;
import com.dissertation.smsmarketing.exception.SMSMarketingDataException;
import com.dissertation.smsmarketing.exception.SMSMarketingServiceException;
import com.dissertation.smsmarketing.model.HistoricalPriceModel;
import com.dissertation.smsmarketing.model.SMSModel;
import com.dissertation.smsmarketing.service.IStockDataService;
import com.dissertation.smsmarketing.service.ISubscriptionDataService;
import com.dissertation.smsmarketing.util.ChartUtil;
import com.dissertation.smsmarketing.util.SMSStockData;
import com.dissertation.smsmarketing.util.StockDetails;

@Service("messageResponseService")
@Transactional
public class StockService implements IStockDataService,ISubscriptionDataService {
	@Autowired
	ISMSMarketingDAO iSMSMarketingDAO;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	private BufferedImage mmsStockDetails(List<String> stockNames) throws InvalidTickerException {
		BufferedImage mmschart = null;
		List<HistoricalPriceModel> historicalPriceModel = null;
		HashMap<String,List<HistoricalPriceModel>> stockData = new HashMap<String,List<HistoricalPriceModel>>();
		try {
			stockData.clear();
			for (String eachStockName : stockNames) {
				log.info("eachStockName"+eachStockName);
				historicalPriceModel = StockDetails.getHistoricalStockDetails(eachStockName);
				log.info("historicalPriceModel"+historicalPriceModel.size());
				stockData.put(eachStockName,historicalPriceModel);
			}
			mmschart = ChartUtil.createDataset(stockData);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to get chart {}",e.getMessage());
			throw new InvalidTickerException("Unable to get chart");
		}
		return mmschart;
	}

	private String smsStockDetails(String stockName) throws InvalidTickerException{
		List<SMSModel> stockDataList = null;
		try {
			stockDataList = SMSStockData.fetchSMSCSV(stockName);
			if(stockDataList.get(0).toString().contains("N/A")){
				return "Invalid stock ticker";
			}
		} catch (Exception e) {
			log.error("Error in SMS service", e);
			throw new InvalidTickerException("Invalid ticker symbol");
		}
		return stockDataList.get(0).toString();
	}

	public byte[] retriveStockData(String stockMessage,String phoneNumber) throws SMSMarketingDataException,InvalidCodeException,InvalidTickerException {
		try{
			//check if the string contains only space
			if (stockMessage.trim().length() <= 0) {
				throw new InvalidCodeException("Blank Code");
			}
			//split the message with space
			String[] splitMessage = stockMessage.split(" ");
			//check the length of the string
			if (splitMessage.length <= 0) {
				throw new InvalidCodeException("Invalid number of parameters");
			}
			byte [] responseData = null;
			//check for various input options
			if(splitMessage[0].equals(SMSMarketingConstants.GET_MESSAGE) || splitMessage[0].equals(SMSMarketingConstants.GET_CHART)){
				if(splitMessage.length==2){
					List<String> fundNamesList = new ArrayList<String>();
					String[] fundNames = splitMessage[1].split(",");
					//check whether there is more than one ticker symbol
					if(fundNames.length == 2){
						for (String fundName : fundNames) {
							fundNamesList.add(fundName);
						}
					}
					else if(fundNames.length == 1){
						fundNamesList.add(splitMessage[1].toString());
					}
					else{
						throw new InvalidCodeException("Invalid input format");
					}
					
					if(splitMessage[0].equals(SMSMarketingConstants.GET_MESSAGE)){
						String smsStockDetail = "";
						for (String eachStockName : fundNamesList) {
							smsStockDetail = smsStockDetail + smsStockDetails(eachStockName);
						}
						responseData = smsStockDetail.getBytes();
					}
					else if(splitMessage[0].equals(SMSMarketingConstants.GET_CHART)){
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						BufferedImage bufferedimage = mmsStockDetails(fundNamesList);
						ImageIO.write(bufferedimage, "png", bao);
						bao.close();
						bufferedimage.flush();
						responseData =  bao.toByteArray();
					}
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}
			else if(splitMessage[0].equals(SMSMarketingConstants.SUBSCRIBE_MESSAGE)){
				if(splitMessage.length == 2){
					String ticker = splitMessage[1];
					boolean checkValidTicker = StockDetails.validStockUrl(ticker);
					if(checkValidTicker){
						boolean subscribed = subscribeStock(phoneNumber, ticker.toUpperCase(), false, null);
						if (subscribed) {
							responseData = SMSMarketingConstants.SUBSCRIPTION_MESSAGE.replace("{{ticker}}", ticker).replace("{{details}}", phoneNumber).getBytes();
						}
					}
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}	
			else if(splitMessage[0].equals(SMSMarketingConstants.SUBSCRIBE_MAIL)){
				 if (splitMessage.length == 3){
					String ticker = splitMessage[1];
					String email = splitMessage[2];
					boolean checkValidTicker = StockDetails.validStockUrl(ticker);
					if(checkValidTicker){
						if (validateEmail(email)) {
							boolean subscribed = subscribeStock(phoneNumber, ticker.toUpperCase(), true, email);
							if (subscribed) {
								responseData = SMSMarketingConstants.EMAIL_SUBSCRIBE_MESSAGE.replace("{{ticker}}", ticker).replace("{{details}}", email).getBytes();
							}
						} else {
							throw new InvalidCodeException("Invalid email address.");
						}
					}	
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}
			else if(splitMessage[0].equals(SMSMarketingConstants.UNSUBSCRIBE_MESSAGE)){ 
				if(splitMessage.length==2){
					String ticker = splitMessage[1];
					boolean checkValidTicker = StockDetails.validStockUrl(ticker);
					if(checkValidTicker){
						boolean unsubscribed = unsubscribeStock(phoneNumber,ticker.toUpperCase(), false);
						if (unsubscribed) {
							responseData = SMSMarketingConstants.PHONE_UNSUBSCRIBE_MESSAGE.replace("{{ticker}}", ticker).replace("{{details}}", phoneNumber).getBytes();
						}
					}	
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}
			else if(splitMessage[0].equals(SMSMarketingConstants.UNSUBSCRIBE_MAIL)){
				if(splitMessage.length==2){
					String ticker = splitMessage[1];
					boolean checkValidTicker = StockDetails.validStockUrl(ticker);
					if(checkValidTicker){
						boolean unsubscribed = unsubscribeStock(phoneNumber,ticker.toUpperCase(), true);
						if (unsubscribed) {
							responseData = SMSMarketingConstants.EMAIL_UNSUBSCRIBE_MESSAGE.replace("{{ticker}}", ticker).replace("{{details}}", phoneNumber).getBytes();
						}
					}	
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}
			else if(splitMessage[0].equals(SMSMarketingConstants.OPTIONS_MESSAGE)){
				if(splitMessage.length == 1){
					List<String> allAvailableOptions = stockOptions();
					String responseOptions="";
					for (String eachOption : allAvailableOptions) {
						responseOptions = responseOptions + eachOption + "\n"; 
					}
					responseData = responseOptions.getBytes();
				}
				else{
					throw new InvalidCodeException("Invalid input format.");
				}
			}
			else{
				throw new InvalidCodeException("Invalid Code");
			}
			return responseData;
		}	
		catch(Exception e){
			e.printStackTrace();
			log.error("Exception {}",e);
			throw new SMSMarketingDataException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public List<String> stockOptions() throws SMSMarketingServiceException {
		try {
			return getiSMSMarketingDAO().stockOptions();
		} catch (SMSMarketingDAOException e) {
			e.printStackTrace();
			log.error("Exception {}",e);
			throw new SMSMarketingServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public boolean subscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail,String email_id) throws SMSMarketingServiceException {
		try {
			return getiSMSMarketingDAO().subscribeStock(phoneNumber,tickerSymbol,isEmail,email_id);
			} catch (SMSMarketingDAOException e) {
			e.printStackTrace();
			log.error("Exception {}",e);
			throw new SMSMarketingServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public boolean unsubscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail) throws SMSMarketingServiceException {
		try {
			return getiSMSMarketingDAO().unsubscribeStock(phoneNumber,tickerSymbol,isEmail);
			
		} catch (SMSMarketingDAOException e) {
			e.printStackTrace();
			log.error("Exception {}",e);
			throw new SMSMarketingServiceException(e.getMessage());
		}
	}
	
	public ISMSMarketingDAO getiSMSMarketingDAO() {
		return iSMSMarketingDAO;
	}

	public void setiSMSMarketingDAO(ISMSMarketingDAO iSMSMarketingDAO) {
		this.iSMSMarketingDAO = iSMSMarketingDAO;
	}
	
	private static boolean validateEmail(String emailAddress) {
		String emailRegex = "[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}";
		return Pattern.matches(emailRegex, emailAddress);
	}
}