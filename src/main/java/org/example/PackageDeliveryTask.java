package org.example;

import java.time.LocalDate;
import java.util.List;

class PackageDeliveryTask implements Runnable {
    private List<Package> packages;
    private String location;
    private LocalDate deliveryDate;

    public PackageDeliveryTask(List<Package> packages, String location, LocalDate deliveryDate) {
        this.packages = packages;
        this.location = location;
        this.deliveryDate = deliveryDate;
    }

    @Override
    public void run() {
        int groupValue = 0;
        int groupRevenue = 0;
        for (Package pkg : packages) {
            if (pkg.getTargetLocation().equals(location) && pkg.getDeliveryDate().equals(deliveryDate)) {
                int deliveryTimeInSeconds = pkg.getDistance();
                try {
                    Thread.sleep(deliveryTimeInSeconds * 1000);
                    groupValue += pkg.getValue();
                    groupRevenue += pkg.getDistance();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(String.format("[Delivering for %s and date %s in %d seconds]", (Object) location, (Object) deliveryDate, groupRevenue));
    }

    public int getGroupValue() {
        int groupValue = 0;
        for (Package pkg : packages) {
            if (pkg.getTargetLocation().equals(location) && pkg.getDeliveryDate().equals(deliveryDate)) {
                groupValue += pkg.getValue();
            }
        }
        return groupValue;
    }

    public int getGroupRevenue() {
        int groupRevenue = 0;
        for (Package pkg : packages) {
            if (pkg.getTargetLocation().equals(location) && pkg.getDeliveryDate().equals(deliveryDate)) {
                groupRevenue += pkg.getDistance();
            }
        }
        return groupRevenue;
    }
}
