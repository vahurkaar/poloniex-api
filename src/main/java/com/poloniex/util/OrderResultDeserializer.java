package com.poloniex.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.poloniex.model.OrderResult;
import com.poloniex.model.Trade;

import java.io.IOException;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 11/07/2017
 */
public class OrderResultDeserializer extends JsonDeserializer<OrderResult> {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderResult deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        OrderResult orderResult = new OrderResult();
        TreeNode treeNode = p.getCodec().readTree(p);

        orderResult.setSuccess(readIntValue(treeNode, "success"));
        orderResult.setOrderNumber(readStringValue(treeNode, "orderNumber"));
        orderResult.setMessage(readStringValue(treeNode, "message"));
        orderResult.setError(readStringValue(treeNode, "error"));
        orderResult.setResultingTrades(readTrades(treeNode, "resultingTrades"));

        return orderResult;
    }

    private Trade[] readTrades(TreeNode treeNode, String key) {
        TreeNode tradesNode = treeNode.get(key);
        if (tradesNode != null) {
            if (tradesNode.isObject()) {
                return objectMapper.convertValue(tradesNode.get(0), Trade[].class);
            } else if (tradesNode.isArray()) {
                return objectMapper.convertValue(tradesNode, Trade[].class);
            }
        }

        return null;
    }

    private String readStringValue(TreeNode treeNode, String key) {
        if (treeNode.get(key) != null) {
            return ((TextNode) treeNode.get(key)).asText();
        }

        return null;
    }

    private Integer readIntValue(TreeNode treeNode, String key) {
        if (treeNode.get(key) != null) {
            return ((IntNode) treeNode.get(key)).intValue();
        }

        return null;
    }
}
