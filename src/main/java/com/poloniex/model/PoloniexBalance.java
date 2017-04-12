package com.poloniex.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
public class PoloniexBalance implements Serializable {
    private String currency;
    private BigDecimal available;
    private BigDecimal onOrders;
    private BigDecimal btcValue;

    public PoloniexBalance() {}

    public PoloniexBalance(String currency, BigDecimal available, BigDecimal onOrders, BigDecimal btcValue) {
        this.currency = currency;
        this.available = available;
        this.onOrders = onOrders;
        this.btcValue = btcValue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public void setAvailable(BigDecimal available) {
        this.available = available;
    }

    public BigDecimal getOnOrders() {
        return onOrders;
    }

    public void setOnOrders(BigDecimal onOrders) {
        this.onOrders = onOrders;
    }

    public BigDecimal getBtcValue() {
        return btcValue;
    }

    public void setBtcValue(BigDecimal btcValue) {
        this.btcValue = btcValue;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "currency='" + currency + '\'' +
                ", available=" + available +
                ", onOrders=" + onOrders +
                ", btcValue=" + btcValue +
                '}';
    }
}
