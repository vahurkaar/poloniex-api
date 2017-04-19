package com.poloniex.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.poloniex.util.CurrencyOrderBookDeserializer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 16/04/2017
 */
@JsonDeserialize(using = CurrencyOrderBookDeserializer.class)
public class PoloniexOrderBook {

    private TreeMap<BigDecimal, BigDecimal> asks;
    private TreeMap<BigDecimal, BigDecimal> bids;

    public BigDecimal getTotalAskVolume() {
        return calculateVolumeSum(asks);
    }

    public BigDecimal getTotalBidVolume() {
        return calculateVolumeSum(bids);
    }

    private BigDecimal calculateVolumeSum(Map<BigDecimal, BigDecimal> entries) {
        BigDecimal result = BigDecimal.ZERO;
        for (BigDecimal volume : entries.values()) {
            result = result.add(volume);
        }

        return result;
    }

    public TreeMap<BigDecimal, BigDecimal> getAsks() {
        if (asks == null) {
            asks = new TreeMap<>();
        }
        return asks;
    }

    public void setAsks(TreeMap<BigDecimal, BigDecimal> asks) {
        this.asks = asks;
    }

    public TreeMap<BigDecimal, BigDecimal> getBids() {
        if (bids == null) {
            bids = new TreeMap<>((o1, o2) -> o2.compareTo(o1));
        }
        return bids;
    }

    public void setBids(TreeMap<BigDecimal, BigDecimal> bids) {
        this.bids = bids;
    }

    @Override
    public String toString() {
        return "CurrencyOrderBook{" +
                "asks=" + asks +
                ", bids=" + bids +
                '}';
    }

}
