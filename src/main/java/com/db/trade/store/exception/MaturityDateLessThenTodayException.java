package com.db.trade.store.exception;

public class MaturityDateLessThenTodayException  extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String MATURITY_DATE_MSG = "Maturity Date Less Then Today";

	public MaturityDateLessThenTodayException() {
		super(MATURITY_DATE_MSG);
	}

}
