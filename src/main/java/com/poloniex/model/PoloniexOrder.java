package com.poloniex.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.poloniex.util.UnixTimestampDeserializer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 20/04/2017
 */
public class PoloniexOrder implements Serializable {

    private String orderNumber;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp date;
    private String type;
    private BigDecimal startingAmount;
    private BigDecimal amount;
    private BigDecimal total;
    private BigDecimal rate;
    private BigDecimal margin;

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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getStartingAmount() {
        return startingAmount;
    }

    public void setStartingAmount(BigDecimal startingAmount) {
        this.startingAmount = startingAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public void setMargin(BigDecimal margin) {
        this.margin = margin;
    }

    @Override
    public String toString() {
        return "PoloniexOrder{" +
                "orderNumber='" + orderNumber + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", startingAmount=" + startingAmount +
                ", amount=" + amount +
                ", total=" + total +
                ", rate=" + rate +
                ", margin=" + margin +
                '}';
    }
}
