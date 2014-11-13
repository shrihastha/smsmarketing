package com.dissertation.smsmarketing.dao.objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK_OPTIONS", schema = "SMSMARKETING_DISSERTATION")
public class SMSMarketingOptions {

	@Id
	@Column(name = "options")
	private String options;

	public SMSMarketingOptions() {
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
}
