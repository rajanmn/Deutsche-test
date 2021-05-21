package com.db.trade.store;

import java.util.List;

import com.db.trade.TradeObject;
import com.db.trade.store.exception.LowerVersionTradeStoreException;
import com.db.trade.store.exception.MaturityDateLessThenTodayException;

public interface ITradeStore {
	
	public void saveDateToStore(TradeObject object) throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException;
	public List<TradeObject> getDataFromStore(String tradeId);
	public TradeObject getDataFromStore(String tradeId, Integer version);
	
}
