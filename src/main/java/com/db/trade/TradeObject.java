package com.db.trade;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//Represents Trade Data
public class TradeObject {

	private String  tradeId;
	private int version;
	private String ccp;
	private String bookId;
	private String maturityDate;
	private String createdDate;
	private boolean expired;
	
	public TradeObject(String tradeId, int version, String ccp, String bookId, String maturityDate, String createdDate) {
		super();
		this.tradeId = tradeId;
		this.version = version;
		this.ccp = ccp;
		this.bookId = bookId;
		this.maturityDate = maturityDate;
		this.createdDate = (createdDate != null ? createdDate : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
		this.expired = LocalDate.parse(maturityDate,DateTimeFormatter.ofPattern("yyyyMMdd")).isBefore(LocalDate.parse(createdDate != null ? createdDate : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), DateTimeFormatter.ofPattern("yyyyMMdd")));
	}


	public String getTradeId() {
		return tradeId;
	}


	public int getVersion() {
		return version;
	}


	public String getCcp() {
		return ccp;
	}


	public String getBookId() {
		return bookId;
	}


	public String getMaturityDate() {
		return maturityDate;
	}


	public String getCreatedDate() {
		return createdDate;
	}


	public boolean isExpired() {
		return expired;
	}
	
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
}
