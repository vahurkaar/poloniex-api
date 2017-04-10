package com.poloniex.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.poloniex.util.NumericBooleanDeserializer;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * currencyPair, last, lowestAsk, highestBid, percentChange, baseVolume, quoteVolume, getIsFrozen, 24hrHigh, 24hrLow
 * ['BTC_BBR','0.00069501','0.00074346','0.00069501','-0.00742634','8.63286802','11983.47150109',0,'0.00107920','0.00045422']
 *
 * Created by vahurkaar on 19/03/2017.
 */

public class PoloniexTickerData implements Serializable     {

    private Integer id;
    private String currencyPair;
    @JsonProperty("last")
    private BigDecimal lastValue;
    private BigDecimal lowestAsk;
    private BigDecimal highestBid;
    private BigDecimal percentChange;
    private BigDecimal baseVolume;
    private BigDecimal quoteVolume;
    @JsonDeserialize(using = NumericBooleanDeserializer.class)
    private Boolean isFrozen;
    private BigDecimal high24hr;
    private BigDecimal low24hr;

    public PoloniexTickerData() {}

    public PoloniexTickerData(Integer id, String currencyPair, BigDecimal lastValue, BigDecimal lowestAsk, BigDecimal highestBid, BigDecimal percentChange, BigDecimal baseVolume, BigDecimal quoteVolume, boolean isFrozen, BigDecimal high24hr, BigDecimal low24hr) {
        this.id = id;
        this.currencyPair = currencyPair;
        this.lastValue = lastValue;
        this.lowestAsk = lowestAsk;
        this.highestBid = highestBid;
        setPercentChange(percentChange);
        this.baseVolume = baseVolume;
        this.quoteVolume = quoteVolume;
        this.isFrozen = isFrozen;
        this.high24hr = high24hr;
        this.low24hr = low24hr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public BigDecimal getLastValue() {
        return lastValue;
    }

    public void setLastValue(BigDecimal lastValue) {
        this.lastValue = lastValue;
    }

    public BigDecimal getLowestAsk() {
        return lowestAsk;
    }

    public void setLowestAsk(BigDecimal lowestAsk) {
        this.lowestAsk = lowestAsk;
    }

    public BigDecimal getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(BigDecimal highestBid) {
        this.highestBid = highestBid;
    }

    public BigDecimal getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(BigDecimal percentChange) {
        this.percentChange = percentChange.multiply(new BigDecimal(100));
    }

    public BigDecimal getBaseVolume() {
        return baseVolume;
    }

    public void setBaseVolume(BigDecimal baseVolume) {
        this.baseVolume = baseVolume;
    }

    public BigDecimal getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(BigDecimal quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public boolean getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public BigDecimal getHigh24hr() {
        return high24hr;
    }

    public void setHigh24hr(BigDecimal high24hr) {
        this.high24hr = high24hr;
    }

    public BigDecimal getLow24hr() {
        return low24hr;
    }

    public void setLow24hr(BigDecimal low24hr) {
        this.low24hr = low24hr;
    }

    @Override
    public String toString() {
        return "PoloniexTickerData{" +
                "id='" + id + '\'' +
                ", currencyPair='" + currencyPair + '\'' +
                ", lastValue=" + lastValue +
                ", lowestAsk=" + lowestAsk +
                ", highestBid=" + highestBid +
                ", percentChange=" + percentChange +
                ", baseVolume=" + baseVolume +
                ", quoteVolume=" + quoteVolume +
                ", isFrozen=" + isFrozen +
                ", high24hr=" + high24hr +
                ", low24hr=" + low24hr +
                '}';
    }
}
