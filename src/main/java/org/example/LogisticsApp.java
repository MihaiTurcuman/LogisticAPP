package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LogisticsApp {

    static List<Package> loadPackages(String filename) throws Exception {
        List<Package> packages = new ArrayList<> ();
        InputStream inputStream = LogisticsApp.class.getClassLoader ().getResourceAsStream (filename);
        BufferedReader reader = new BufferedReader (new InputStreamReader (inputStream));
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy-MM-dd");
        while ((line = reader.readLine ()) != null) {
            String[] parts = line.split (",");
            String targetLocation = parts[0];
            int distance = Integer.parseInt (parts[1]);
            int value = Integer.parseInt (parts[2]);
            LocalDate deliveryDate = LocalDate.parse (parts[3], formatter);
            packages.add (new Package (targetLocation, distance, value, deliveryDate));
        }
        reader.close ();
        return packages;
    }

    public static void main(String[] args) throws Exception {
        List<Package> packages = loadPackages ("packages.txt");

        // Group packages by location and delivery date
        Map<String, Map<LocalDate, List<Package>>> packageGroups;
// Group packages by location and delivery date
        Map<String, Map<LocalDate, List<Package>>> packageGroups = new ConcurrentHashMap<> ();
        for (Package pkg : packages) {
            String targetLocation = pkg.getTargetLocation ();
            LocalDate deliveryDate = pkg.getDeliveryDate ();
            packageGroups.computeIfAbsent (targetLocation, k -> new ConcurrentHashMap<> ())
                    .computeIfAbsent (deliveryDate, k -> new ArrayList<> ())
                    .add (pkg);
        }

        // Create thread pool to deliver packages
        ExecutorService executorService = Executors.newCachedThreadPool ();

        // Deliver packages for each group
        for (Map.Entry<String, Map<LocalDate, List<Package>>> locationEntry : packageGroups.entrySet ()) {
            String location = locationEntry.getKey ();
            for (Map.Entry<LocalDate, List<Package>> dateEntry : locationEntry.getValue ().entrySet ()) {
                LocalDate deliveryDate = dateEntry.getKey ();
                List<Package> packagesToDeliver = dateEntry.getValue ();
                PackageDeliveryTask deliveryTask = new PackageDeliveryTask (packagesToDeliver, location, deliveryDate);
                executorService.submit (deliveryTask);
            }
        }

        // Shutdown thread pool and wait for all threads to finish
        executorService.shutdown ();
        try {
            executorService.awaitTermination (Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        // Calculate total value and revenue
        int totalValue = 0;
        int totalRevenue = 0;
        for (Map.Entry<String, Map<LocalDate, List<Package>>> locationEntry : packageGroups.entrySet ()) {
            for (Map.Entry<LocalDate, List<Package>> dateEntry : locationEntry.getValue ().entrySet ()) {
                List<Package> packagesDelivered = dateEntry.getValue ();
                PackageDeliveryTask deliveryTask = new PackageDeliveryTask (packagesDelivered, locationEntry.getKey (), dateEntry.getKey ());
                totalValue += deliveryTask.getGroupValue ();
                totalRevenue += deliveryTask.getGroupRevenue ();
            }
        }

        // Print total value and revenue
        System.out.println ("Total value of all delivered packages: " + totalValue);
        System.out.println ("Total revenue computed for all groups delivered: " + totalRevenue);
    }
}