package com.dissertation.smsmarketing.dao.objects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SMSMARKETINGSUBSCRIPTION", schema = "SMSMARKETING_DISSERTATION")
public class SMSMarketingSubscription {
	
	@Column(name = "request_number", nullable = false)
	private int request_number;

	@Column(name = "subscription_date", nullable = false)
	private Date subscription_date;

	@Column(name = "sms_subscription_flag", nullable = false)
	private String sms_subscription_flag;
	
	@Column(name="email_subscription_flag", nullable = false)
	private String email_subscription_flag;

	@Column(name="email_id")
	private String email_id;
	
	@EmbeddedId
	private SMSMarketingPhone_Ticker smsMarketingPhone_Ticker;
	
	public SMSMarketingSubscription() {
	}
	
	public int getRequest_number() {
		return request_number;
	}

	public void setRequest_number(int request_number) {
		this.request_number = request_number;
	}

	public Date getSubscription_date() {
		return subscription_date;
	}

	public void setSubscription_date(Date subscription_date) {
		this.subscription_date = subscription_date;
	}

	public String getSms_subscription_flag() {
		return sms_subscription_flag;
	}

	public void setSms_subscription_flag(String sms_subscription_flag) {
		this.sms_subscription_flag = sms_subscription_flag;
	}

	public String getEmail_subscription_flag() {
		return email_subscription_flag;
	}

	public void setEmail_subscription_flag(String email_subscription_flag) {
		this.email_subscription_flag = email_subscription_flag;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public SMSMarketingPhone_Ticker getSmsMarketingPhone_Ticker() {
		return smsMarketingPhone_Ticker;
	}

	public void setSmsMarketingPhone_Ticker(SMSMarketingPhone_Ticker smsMarketingPhone_Ticker) {
		this.smsMarketingPhone_Ticker = smsMarketingPhone_Ticker;
	}

}