package com.poloniex.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.poloniex.model.PoloniexOrderBook;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 16/04/2017
 */
public class CurrencyOrderBookDeserializer extends JsonDeserializer<PoloniexOrderBook> {

    @Override
    public PoloniexOrderBook deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        PoloniexOrderBook orderbook = new PoloniexOrderBook();
        TreeNode treeNode = p.getCodec().readTree(p);
        ArrayNode bids = (ArrayNode) treeNode.get("bids");
        ArrayNode asks = (ArrayNode) treeNode.get("asks");

        readOrderbookRows(orderbook.getBids(), bids);
        readOrderbookRows(orderbook.getAsks(), asks);

        return orderbook;
    }

    private void readOrderbookRows(TreeMap<BigDecimal, BigDecimal> orderbookRows, ArrayNode values) {
        for (int i = 0; i < values.size(); i++) {
            ArrayNode valueRow = (ArrayNode) values.get(i);
            BigDecimal price = new BigDecimal(valueRow.get(0).textValue());
            BigDecimal volume = valueRow.get(1).decimalValue();
            orderbookRows.put(price, volume);
        }
    }
}
