package com.poloniex.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * {"totalValue": "0.00346561","pl": "-0.00001220","lendingFees": "0.00000000",
 * "netValue": "0.00345341","totalBorrowedValue": "0.00123220","currentMargin": "2.80263755"}
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 10/07/2017
 */
public class PoloniexMarginAccountSummary implements Serializable {

    private BigDecimal totalValue;
    private BigDecimal pl;
    private BigDecimal lendingFees;
    private BigDecimal netValue;
    private BigDecimal totalBorrowedValue;
    private BigDecimal currentMargin;

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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

    public BigDecimal getNetValue() {
        return netValue;
    }

    public void setNetValue(BigDecimal netValue) {
        this.netValue = netValue;
    }

    public BigDecimal getTotalBorrowedValue() {
        return totalBorrowedValue;
    }

    public void setTotalBorrowedValue(BigDecimal totalBorrowedValue) {
        this.totalBorrowedValue = totalBorrowedValue;
    }

    public BigDecimal getCurrentMargin() {
        return currentMargin;
    }

    public void setCurrentMargin(BigDecimal currentMargin) {
        this.currentMargin = currentMargin;
    }

    @Override
    public String toString() {
        return "PoloniexMarginAccountSummary{" +
                "totalValue=" + totalValue +
                ", pl=" + pl +
                ", lendingFees=" + lendingFees +
                ", netValue=" + netValue +
                ", totalBorrowedValue=" + totalBorrowedValue +
                ", currentMargin=" + currentMargin +
                '}';
    }
}
