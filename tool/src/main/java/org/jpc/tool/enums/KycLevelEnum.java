package org.jpc.tool.enums;

public enum KycLevelEnum {
    LEVEL1("Level 1"),
    LEVEL2("Level 2"),
    LEVEL3("Level 3");

    public final String text;

    private KycLevelEnum(String text) {
        this.text = text;
    }
}
