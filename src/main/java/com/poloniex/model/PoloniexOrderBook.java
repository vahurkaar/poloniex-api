package com.poloniex.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.poloniex.util.CurrencyOrderBookEntrySerializer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 16/04/2017
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoloniexOrderBook {

    private List<Entry> asks;
    private List<Entry> bids;

    public BigDecimal getTotalAskVolume() {
        return calculateVolumeSum(asks);
    }

    public BigDecimal getTotalBidVolume() {
        return calculateVolumeSum(bids);
    }

    private BigDecimal calculateVolumeSum(List<Entry> entries) {
        BigDecimal result = BigDecimal.ZERO;
        for (Entry bid : entries) {
            result = result.add(bid.getVolume());
        }

        return result;
    }

    public List<Entry> getAsks() {
        if (asks == null) {
            asks = new ArrayList<>();
        }
        return asks;
    }

    public void setAsks(List<Entry> asks) {
        this.asks = asks;
    }

    public List<Entry> getBids() {
        if (bids == null) {
            bids = new ArrayList<>();
        }
        return bids;
    }

    public void setBids(List<Entry> bids) {
        this.bids = bids;
        for (Entry bid : bids) {
            bid.volume = bid.volume.multiply(bid.price);
        }
    }

    @Override
    public String toString() {
        return "CurrencyOrderBook{" +
                "asks=" + asks +
                ", bids=" + bids +
                '}';
    }

    @JsonDeserialize(using = CurrencyOrderBookEntrySerializer.class)
    public static class Entry {
        private BigDecimal price;
        private BigDecimal volume;

        public Entry(BigDecimal price, BigDecimal volume) {
            this.price = price;
            this.volume = volume;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "price=" + price +
                    ", volume=" + volume +
                    '}';
        }
    }
}
