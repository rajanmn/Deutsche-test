package com.db.trade.store.gateway;

import java.util.List;

import com.db.trade.TradeObject;
import com.db.trade.store.exception.LowerVersionTradeStoreException;
import com.db.trade.store.exception.MaturityDateLessThenTodayException;

public interface ITradeStoreGateway {
	
	public void saveTrade(TradeObject tradeObject) throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException;
	public boolean checkIfExpired(TradeObject tradeObject);
	public List<TradeObject> getTrade(String tradeId);

}
