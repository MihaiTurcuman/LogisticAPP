package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Package {
    private String targetLocation;
    private int distance;
    private int value;
    private LocalDate deliveryDate;

    public Package(String targetLocation, int distance, int value, LocalDate deliveryDate) {
        this.targetLocation = targetLocation;
        this.distance = distance;
        this.value = value;
        this.deliveryDate = deliveryDate;
    }

    public String getTargetLocation() {
        return targetLocation;
    }

    public int getDistance() {
        return distance;
    }

    public int getValue() {
        return value;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }
}