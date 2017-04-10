package com.poloniex.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by vahurkaar on 20/03/2017.
 */

public class UnixTimestampDeserializer extends JsonDeserializer<Date> {

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        long timestamp = jp.getLongValue();

        try {
            return new Timestamp(timestamp * 1000);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}