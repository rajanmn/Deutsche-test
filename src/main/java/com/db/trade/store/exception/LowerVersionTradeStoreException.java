package com.db.trade.store.exception;

public class LowerVersionTradeStoreException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static String LOWER_VERSION_MSG = "Lower Version Of Trade Received";

	public LowerVersionTradeStoreException() {
		super(LOWER_VERSION_MSG);
	}

}
