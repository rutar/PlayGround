package com.example.playground.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

public class PlaySiteUtilization {
    @NonNull
    @JsonProperty
    String playSiteName;

    @NonNull
    @JsonProperty
    int playSiteUtilization;

    public PlaySiteUtilization(final String name, final int utilization) {
        this.playSiteName = name;
        this.playSiteUtilization = utilization;
    }
}
