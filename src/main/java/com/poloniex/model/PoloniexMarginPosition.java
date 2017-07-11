package com.poloniex.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * {"amount":"40.94717831","total":"-0.09671314",""basePrice":"0.00236190","liquidationPrice":-1,
 * "pl":"-0.00058655", "lendingFees":"-0.00000038","type":"long"}
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 09/07/2017
 */
public class PoloniexMarginPosition implements Serializable {

    private BigDecimal amount;
    private BigDecimal total;
    private BigDecimal basePrice;
    private BigDecimal liquidationPrice;
    private BigDecimal pl;
    private BigDecimal lendingFees;
    private String type;

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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getLiquidationPrice() {
        return liquidationPrice;
    }

    public void setLiquidationPrice(BigDecimal liquidationPrice) {
        this.liquidationPrice = liquidationPrice;
    }

    public BigDecimal getPl() {
        return pl;
    }

    public void setPl(BigDecimal pl) {
        this.pl = pl;
    }

    public BigDecimal getLendingFees() {
        return lendingFees;
    }

    public void setLendingFees(BigDecimal lendingFees) {
        this.lendingFees = lendingFees;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PoloniexMarginPosition{" +
                "amount=" + amount +
                ", total=" + total +
                ", basePrice=" + basePrice +
                ", liquidationPrice=" + liquidationPrice +
                ", pl=" + pl +
                ", lendingFees=" + lendingFees +
                ", type='" + type + '\'' +
                '}';
    }
}
