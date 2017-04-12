package com.poloniex.model;

/**
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 12/04/2017
 */
public enum PoloniexOrderType {

    FILL_OR_KILL("fillOrKill"), IMMEDIATE_OR_CANCEL("immediateOrCancel"), POST_ONLY("postOnly");

    private String param;

    PoloniexOrderType(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}
