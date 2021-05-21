package com.db.trade.store;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.db.trade.TradeObject;
import com.db.trade.store.exception.LowerVersionTradeStoreException;
import com.db.trade.store.exception.MaturityDateLessThenTodayException;
import com.db.trade.store.gateway.ITradeStoreGateway;
import com.db.trade.store.gateway.TradeStoreGateway;

@RunWith(JUnit4.class)
public class TradeStoreTest {

	private ITradeStoreGateway gateway;
	private ITradeStore tradeStore;
	private String date;
	
	@Before
	public void Setup() {
		tradeStore = new InMemTradeStore();
		gateway = new TradeStoreGateway(tradeStore);
		date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	}
	
	
	@Test	
	public void testTradeStore() {
	
		TradeObject t1 = new TradeObject("T1", 1, "CP-1", "B1", date, null);
		TradeObject t2 = new TradeObject("T2", 1, "CP-2", "B1", date, null);
		TradeObject t3 = new TradeObject("T2", 2, "CP-1", "B1", date, null);
		TradeObject t4 = new TradeObject("T3", 2, "CP-1", "B1", date, null);
		TradeObject t5 = new TradeObject("T4", 1, "CP-1", "B1", date, null);
		TradeObject t6 = new TradeObject("T4", 2, "CP-1", "B1", date, null);
		TradeObject t7 = new TradeObject("T4", 3, "CP-1", "B1", date, null);
		
		try {
			gateway.saveTrade(t1);
			gateway.saveTrade(t2);
			gateway.saveTrade(t3);
			gateway.saveTrade(t4);
			gateway.saveTrade(t5);
			gateway.saveTrade(t6);
			gateway.saveTrade(t7);
			gateway.saveTrade(t5);
			
			assertEquals(2, gateway.getTrade("T2").size());
			assertEquals(3, gateway.getTrade("T4").size());
			
			
		} catch (Exception e) {
			e.printStackTrace();			
		}
	}
	
	@Test(expected = LowerVersionTradeStoreException.class)
	public void testLowerVersionTrade() throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException {
		TradeObject t1 = new TradeObject("T3", 2, "CP-1", "B1", date, null);
		TradeObject t2 = new TradeObject("T3", 1, "CP-1", "B1", date, null);
		gateway.saveTrade(t1);
		gateway.saveTrade(t2);
	}
	
	@Test(expected = MaturityDateLessThenTodayException.class)
	public void testMaturityDateLessThenToday() throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException {
		TradeObject t1 = new TradeObject("T1", 1, "CP-1", "B1", LocalDate.of(2021, 04, 30).format(DateTimeFormatter.ofPattern("yyyyMMdd")), null);
		gateway.saveTrade(t1);
		
	}
	
	@Test
	public void testExpityUpdate() throws LowerVersionTradeStoreException, MaturityDateLessThenTodayException {
		TradeObject t1 = new TradeObject("T1", 1, "CP-1", "B1", date, "20210522");
		gateway.saveTrade(t1);
		
		assertEquals(false, gateway.getTrade("T1").get(0).isExpired());
		
		Thread thread = new Thread((Runnable) tradeStore);
		thread.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//assertEquals(true, gateway.getTrade("T1").get(0).isExpired());
	}
	
	
}

