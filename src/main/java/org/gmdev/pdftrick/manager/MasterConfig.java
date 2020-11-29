package org.gmdev.pdftrick.manager;

public enum MasterConfig {
    LEVEL_1("config"),
    LEVEL_2("run");

    private final String value;

    MasterConfig(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
