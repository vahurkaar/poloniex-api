package com.poloniex.model;

import java.math.BigDecimal;

/**
 * @author Vahur Kaar (<a href="mailto:vahur.kaar@tieto.com">vahur.kaar@tieto.com</a>)
 * @since 18.04.2017.
 */
public class PoloniexOrderBookTickerData {

    public static final String MODIFY = "orderBookModify";
    public static final String REMOVE = "orderBookRemove";

    private String currencyPair; // Transient
    private String type;
    private Data data;

    public PoloniexOrderBookTickerData() {}

    public PoloniexOrderBookTickerData(String currencyPair, String type, Data data) {
        this.type = type;
        this.data = data;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PoloniexOrderBookTickerData{");
        sb.append("type='").append(type).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    public static class Data {
        private String type;
        private BigDecimal rate;
        private BigDecimal amount;

        public Data() {}

        public Data(String type, BigDecimal rate, BigDecimal amount) {
            this.type = type;
            this.rate = rate;
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public void setRate(BigDecimal rate) {
            this.rate = rate;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Data{");
            sb.append("type='").append(type).append('\'');
            sb.append(", rate=").append(rate);
            sb.append(", amount=").append(amount);
            sb.append('}');
            return sb.toString();
        }
    }

}
