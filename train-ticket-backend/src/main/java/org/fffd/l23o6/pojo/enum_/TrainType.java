package org.fffd.l23o6.pojo.enum_;


import com.fasterxml.jackson.annotation.JsonProperty;

public enum TrainType {
    @JsonProperty("高铁") HIGH_SPEED("高铁"), @JsonProperty("普通列车") NORMAL_SPEED("普通列车");

    private String text;

    TrainType(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

}
