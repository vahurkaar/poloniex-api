package com.poloniex.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.poloniex.util.OrderResultDeserializer;

import java.util.Arrays;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
@JsonDeserialize(using = OrderResultDeserializer.class)
public class OrderResult {

    private Integer success;

    private String message;

    private String error;

    private String orderNumber;

    private Trade[] resultingTrades;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Trade[] getResultingTrades() {
        return resultingTrades;
    }

    public void setResultingTrades(Trade[] resultingTrades) {
        this.resultingTrades = resultingTrades;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OrderResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", resultingTrades=" + Arrays.toString(resultingTrades) +
                '}';
    }
}
