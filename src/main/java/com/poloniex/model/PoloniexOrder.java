package com.poloniex.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/04/2017
 */
public class PoloniexOrder implements Serializable {

    private String orderNumber;
    private String type;
    private BigDecimal amount;
    private BigDecimal rate;

    public PoloniexOrder() {}

    public PoloniexOrder(String orderNumber, String type, BigDecimal amount, BigDecimal rate) {
        this.orderNumber = orderNumber;
        this.type = type;
        this.amount = amount;
        this.rate = rate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
