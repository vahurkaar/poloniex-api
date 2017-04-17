package com.poloniex.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.poloniex.model.CurrencyOrderBook;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 16/04/2017
 */
public class CurrencyOrderBookEntrySerializer extends JsonDeserializer<CurrencyOrderBook.Entry> {

    @Override
    public CurrencyOrderBook.Entry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ArrayNode values = p.readValueAs(ArrayNode.class);
        return new CurrencyOrderBook.Entry(new BigDecimal(values.get(0).asText()), values.get(1).decimalValue());
    }
}
