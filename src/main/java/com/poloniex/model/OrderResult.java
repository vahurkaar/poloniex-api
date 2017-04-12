package com.poloniex.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
public class OrderResult {

    private String orderNumber;

    private List<Trade> resultingTrades;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Trade> getResultingTrades() {
        if (resultingTrades == null) {
            resultingTrades = new ArrayList<>();
        }
        return resultingTrades;
    }

    public void setResultingTrades(List<Trade> resultingTrades) {
        this.resultingTrades = resultingTrades;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "orderNumber='" + orderNumber + '\'' +
                ", resultingTrades=" + resultingTrades +
                '}';
    }
}
