package com.db.trade.store;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.db.trade.TradeObject;
import com.db.trade.store.exception.LowerVersionTradeStoreException;
import com.db.trade.store.exception.MaturityDateLessThenTodayException;

public class InMemTradeStore implements ITradeStore, Runnable{
	
	private final Map<String, TreeMap<Integer, TradeObject>> tradeStore = new ConcurrentHashMap<String, TreeMap<Integer, TradeObject>>();

	//Saves data in memory, has maturity date validation and lower version validation
	@Override
	public void saveDateToStore(TradeObject object) throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException {
		
		if(isMaturityDateBeforeToday(object)) {
			throw new MaturityDateLessThenTodayException();
		}
		
		if(tradeStore.containsKey(object.getTradeId())){
			if(object.getVersion() < tradeStore.get(object.getTradeId()).entrySet().stream().findFirst().get().getKey()) {
				throw new LowerVersionTradeStoreException();
			}
			else {
				tradeStore.get(object.getTradeId()).put(object.getVersion(), object);
			}
		}else {
			TreeMap<Integer, TradeObject> map = new TreeMap<Integer, TradeObject>();
			map.put(object.getVersion(), object);
			tradeStore.put(object.getTradeId(), map);
		}
		
	}

	//get list of trades (multiple versions) for the given id
	@Override
	public List<TradeObject> getDataFromStore(String tradeId) {
		return tradeStore.get(tradeId)!=null ? tradeStore.get(tradeId).values().stream().collect(Collectors.toList()) : null;
	}
	
	//get trade for the given tradeid and version number
	@Override
	public TradeObject getDataFromStore(String tradeId, Integer version) {
		 return tradeStore.get(tradeId)!=null ? tradeStore.get(tradeId).get(version) : null;
	}
	
	
	/*
	 * thread to update the expiry flag based on the maturity and today's date
	 * check.. this thread needs to be invoked to have this //functionlity working..
	 * can be done at startup
	 */	
	@Override
	public void run() {
		
		for (Entry<String, TreeMap<Integer, TradeObject>> entryObj : tradeStore.entrySet()) {
			List<TradeObject> expiredTrade = entryObj.getValue().values().parallelStream()
					.filter(t -> isTradeExpired(t)).map(t -> updatedExpired(t)).collect(Collectors.toList());

			expiredTrade.stream().collect(Collectors.toMap(TradeObject::getVersion, tradeObject -> tradeObject));

			tradeStore.put(entryObj.getKey(), new TreeMap<Integer, TradeObject>(expiredTrade.stream()
					.collect(Collectors.toMap(TradeObject::getVersion, tradeObject -> tradeObject))));
		}
	}
	
	private boolean isTradeExpired(TradeObject tradeObject) {
		return LocalDate.parse(tradeObject.getMaturityDate(),DateTimeFormatter.ofPattern("yyyyMMdd")).isBefore(LocalDate.now());
	}
	
	private TradeObject updatedExpired(TradeObject tradeObject) {
		tradeObject.setExpired(true);
		return tradeObject;
	}
	
	private boolean isMaturityDateBeforeToday(TradeObject tradeObject) {
		return LocalDate.parse(tradeObject.getMaturityDate(),DateTimeFormatter.ofPattern("yyyyMMdd")).isBefore(LocalDate.now());
	}
	
}
