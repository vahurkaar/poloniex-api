package com.poloniex.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * {id=245, name=StealthCoin, txFee=0.01000000, minConf=15, depositAddress=null, disabled=0, delisted=1, frozen=0}
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 26/03/2017
 */
public class PoloniexCurrency implements Serializable {

    private Integer id;
    private String name;
    private BigDecimal txFee;
    private Integer minConf;
    private String depositAddress;
    private Integer disabled;
    private Integer delisted;
    private Integer frozen;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public Integer getMinConf() {
        return minConf;
    }

    public void setMinConf(Integer minConf) {
        this.minConf = minConf;
    }

    public String getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    public Integer getDisabled() {
        return disabled;
    }

    public void setDisabled(Integer disabled) {
        this.disabled = disabled;
    }

    public Integer getDelisted() {
        return delisted;
    }

    public void setDelisted(Integer delisted) {
        this.delisted = delisted;
    }

    public Integer getFrozen() {
        return frozen;
    }

    public void setFrozen(Integer frozen) {
        this.frozen = frozen;
    }
}
