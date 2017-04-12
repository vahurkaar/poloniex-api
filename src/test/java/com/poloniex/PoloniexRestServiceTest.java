package com.poloniex;

import com.poloniex.model.PoloniexOrderType;
import com.poloniex.model.OrderResult;
import org.junit.Before;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
public class PoloniexRestServiceTest {

    private PoloniexRestService poloniexRestService = new PoloniexRestService();

    @Before
    public void setUp() throws Exception {
        poloniexRestService.setApiKey("");
        poloniexRestService.setSecret("");
    }

//    @Test
    public void testBuy() throws Exception {
        String currencyPair = "BTC_POT";
        BigDecimal rate = new BigDecimal(0.00004100);
        BigDecimal amount = new BigDecimal(250);
        PoloniexOrderType orderType = PoloniexOrderType.IMMEDIATE_OR_CANCEL;

        OrderResult trade = poloniexRestService.buy(currencyPair, rate, amount, orderType);

        assertNotNull(trade);
        assertNotNull(trade.getOrderNumber());
        assertNotNull(trade.getResultingTrades());
        System.out.println(trade);
    }

//    @Test
    public void testSell() throws Exception {
        String currencyPair = "BTC_POT";
        BigDecimal rate = new BigDecimal(0.00004100);
        BigDecimal amount = new BigDecimal(498.75);
        PoloniexOrderType orderType = PoloniexOrderType.IMMEDIATE_OR_CANCEL;

        OrderResult trade = poloniexRestService.sell(currencyPair, rate, amount, orderType);

        assertNotNull(trade);
        assertNotNull(trade.getOrderNumber());
        assertNotNull(trade.getResultingTrades());
        System.out.println(trade);
    }

}