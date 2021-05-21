package com.db.trade.store.gateway;

import java.util.List;

import com.db.trade.TradeObject;
import com.db.trade.store.ITradeStore;
import com.db.trade.store.exception.LowerVersionTradeStoreException;
import com.db.trade.store.exception.MaturityDateLessThenTodayException;


//This Facade Api exposed to the User
public class TradeStoreGateway implements ITradeStoreGateway{

	private final ITradeStore tradeStore;
	
	public TradeStoreGateway(ITradeStore tradeStore) {
		super();
		this.tradeStore = tradeStore;
	}

	@Override
	public void saveTrade(TradeObject tradeObject) throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException {
		tradeStore.saveDateToStore(tradeObject);
	}

	@Override
	public boolean checkIfExpired(TradeObject tradeObject) {
		return tradeStore.getDataFromStore(tradeObject.getTradeId(), tradeObject.getVersion()).isExpired();
	}

	@Override
	public List<TradeObject> getTrade(String tradeId) {
		return tradeStore.getDataFromStore(tradeId);
	}

}
