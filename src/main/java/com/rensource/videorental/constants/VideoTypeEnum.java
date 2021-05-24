package com.rensource.videorental.constants;

public enum VideoTypeEnum {
    REGULAR("Regular"), CHILDREN("Children's movie"), NEW("New Release");

    private final String type;

    VideoTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
