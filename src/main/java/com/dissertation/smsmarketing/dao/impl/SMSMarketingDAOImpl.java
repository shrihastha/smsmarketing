package com.dissertation.smsmarketing.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.dissertation.smsmarketing.dao.ISMSMarketingDAO;
import com.dissertation.smsmarketing.dao.objects.SMSMarketingPhone_Ticker;
import com.dissertation.smsmarketing.dao.objects.SMSMarketingSubscription;
import com.dissertation.smsmarketing.exception.SMSMarketingDAOException;


@Repository("smsMarketingDAO")
public class SMSMarketingDAOImpl extends HibernateDaoSupport implements ISMSMarketingDAO{
	
	@Autowired
	public void setStockSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public List<String> stockOptions() throws SMSMarketingDAOException{
		Session session = null;
		try{
			log.info("inside stockoptions DAO");
			session = getSessionFactory().openSession();
			Query query = session.createQuery("select options from SMSMarketingOptions");
			List<String> options = query.list();
			if (options.isEmpty()){
				throw new SMSMarketingDAOException("Unable to retrieve the options.");
			}
			session.flush();
			return options;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Exception while fetching available options {}" , e);
			throw new SMSMarketingDAOException("Exception while fetching data from database.");
		}
		finally{
			if(session !=null){
				log.info("Closing the session.");
				session.close();
			}
		}
	}
	
	@Override
	public boolean subscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail,String email_id) throws SMSMarketingDAOException{
		SMSMarketingSubscription smsMarketingSubscription = null;
		Session session = null;
		SMSMarketingPhone_Ticker smsMarketingPhone_Ticker =  null; 
		try{
			session = getSessionFactory().openSession();
			Query subscribeQuery = session.createQuery("from SMSMarketingSubscription where phone_number = :phone_number and ticker_symbol = :ticker_symbol " +
					" and (sms_subscription_flag='N' or email_subscription_flag='N') ");
			subscribeQuery.setString("phone_number",phoneNumber);
			subscribeQuery.setString("ticker_symbol",tickerSymbol);
			smsMarketingSubscription = (SMSMarketingSubscription) subscribeQuery.uniqueResult();
			
			if(smsMarketingSubscription != null){
				//update
				if(isEmail){
					if(smsMarketingSubscription.getEmail_subscription_flag().equals("Y")){
						throw new SMSMarketingDAOException("Already email subscription is available for the ticker.");
					}
					else{
						smsMarketingSubscription.setEmail_subscription_flag("Y");
						smsMarketingSubscription.setEmail_id(email_id);
					}	
				}
				else{
					if(smsMarketingSubscription.getSms_subscription_flag().equals("Y"))
						throw new SMSMarketingDAOException("Already subscription is available for the ticker.");
					else
						smsMarketingSubscription.setSms_subscription_flag("Y");
				}	
			}
			else{
				//insert
				smsMarketingSubscription = new SMSMarketingSubscription();
				smsMarketingPhone_Ticker = new SMSMarketingPhone_Ticker();
				smsMarketingPhone_Ticker.setPhone_number(phoneNumber);
				smsMarketingPhone_Ticker.setTicker_symbol(tickerSymbol);
				smsMarketingSubscription.setRequest_number(100);
				smsMarketingSubscription.setSubscription_date(Calendar.getInstance().getTime());
				smsMarketingSubscription.setSmsMarketingPhone_Ticker(smsMarketingPhone_Ticker);
				if(isEmail){
					smsMarketingSubscription.setEmail_subscription_flag("Y");
					smsMarketingSubscription.setEmail_id(email_id);
					smsMarketingSubscription.setSms_subscription_flag("N");
					
				}	
				else {
					smsMarketingSubscription.setSms_subscription_flag("Y");
					smsMarketingSubscription.setEmail_subscription_flag("N");
				}	
			}
			session.saveOrUpdate(smsMarketingSubscription);
			session.flush();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Error while updating subscription {}" , e);
			throw new SMSMarketingDAOException(e.getMessage());
		}
		finally{
			if (session !=null){
				log.info("Closing the session");
				session.close();
			}
		}
	}
	
	@Override
	public boolean unsubscribeStock(String phoneNumber,String tickerSymbol,boolean isEmail) throws SMSMarketingDAOException{
		SMSMarketingSubscription smsMarketingSubscription = null;
		Session session = null;
		try{
			session = getSessionFactory().openSession();
			Query query = session.createQuery("from SMSMarketingSubscription where phone_number = :phone_number and ticker_symbol = :ticker_symbol " +
					" and (sms_subscription_flag ='Y' or email_subscription_flag = 'Y') ");
			query.setString("phone_number",phoneNumber);
			query.setString("ticker_symbol",tickerSymbol);
			
			smsMarketingSubscription = (SMSMarketingSubscription) query.uniqueResult();
			log.info("fetched result from query");
			if(smsMarketingSubscription != null){
				if(isEmail){
					if(smsMarketingSubscription.getEmail_subscription_flag().equals("N"))
						throw new SMSMarketingDAOException("Already email unsubscribed for the ticker.");
					else
						smsMarketingSubscription.setEmail_subscription_flag("N");
				}	
				else
					if(smsMarketingSubscription.getSms_subscription_flag().equals("N"))
						throw new SMSMarketingDAOException("Already unsubscribed for the ticker.");
					else
						smsMarketingSubscription.setSms_subscription_flag("N");
			}
			else{
				throw new SMSMarketingDAOException("Subscription not found for the ticker.");
			}
			session.saveOrUpdate(smsMarketingSubscription);
			session.flush();
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			log.error("Error while updating unsubscription {}" , e);
			throw new SMSMarketingDAOException(e.getMessage());
		}
		finally{
			if (session !=null){
				log.info("Closing the session");
				session.close();
			}
		}
	}
}