package com.example.playground.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;

import java.util.Random;

public class Kid {
    @JsonProperty
    @NonNull
    String name;

    @JsonProperty
    @NonNull
    int age;

    @JsonProperty
    @NonNull
    long ticketNumber;

    protected boolean acceptsWaiting() {
        return new Random().nextBoolean();
    }
}
