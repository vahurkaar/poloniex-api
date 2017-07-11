package com.poloniex;

import com.poloniex.model.PoloniexMarginAccountSummary;
import com.poloniex.model.PoloniexMarginPosition;
import com.poloniex.model.PoloniexOrder;
import com.poloniex.model.PoloniexOrderType;
import com.poloniex.model.OrderResult;
import com.poloniex.model.Trade;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Test
    public void returnOpenOrders() throws Exception {
        Map<String, List<PoloniexOrder>> openOrders = poloniexRestService.returnOpenOrders();

        System.out.println(openOrders);
    }

    @Test
    public void getGetMarginPosition() throws Exception {
        String currencyPair = "BTC_LTC";

        PoloniexMarginPosition marginPosition = poloniexRestService.getMarginPosition(currencyPair);

        assertNotNull(marginPosition);
        System.out.println(marginPosition);

    }

    @Test
    public void getGetMarginPositionAll() throws Exception {
        Map<String, PoloniexMarginPosition> marginPosition = poloniexRestService.getMarginPosition();

        assertNotNull(marginPosition);
        System.out.println(marginPosition);
    }

    @Test
    public void returnTradableBalances() throws Exception {
        Map<String, Map<String, BigDecimal>> tradableBalances = poloniexRestService.returnTradableBalances();

        assertNotNull(tradableBalances);
        System.out.println(tradableBalances);
    }

    @Test
    public void returnMarginAccountSummary() throws Exception {
        PoloniexMarginAccountSummary marginAccountSummary = poloniexRestService.returnMarginAccountSummary();

        assertNotNull(marginAccountSummary);
        System.out.println(marginAccountSummary);
    }

    @Test
    public void returnOrderTrades() throws Exception {
        Trade[] trades = poloniexRestService.returnOrderTrades("30368234456");

        assertNotNull(trades);
        System.out.println(Arrays.toString(trades));
    }

    @Test
    public void moveOrder() throws Exception {
        OrderResult orderResult = poloniexRestService.moveOrder("63235912905", new BigDecimal(0.00007000));

        assertNotNull(orderResult);
        System.out.println(orderResult);
    }

}