package org.example;


import java.util.List;

import static org.example.LogisticsApp.loadPackages;

public class App {
    public static void main(String[] args) {
        try {
            List<Package> packages = loadPackages ("packages.txt");
            calculateDelivery (packages);
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    private static void calculateDelivery(List<Package> packages) {
    }
}
