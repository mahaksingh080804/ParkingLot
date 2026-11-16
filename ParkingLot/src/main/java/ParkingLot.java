import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class ParkingException extends Exception {
    ParkingException(String message) {
        super(message);
    }
}

class Vehicle {
    private String plate;
    private String type;
    private double ratePerHour;

    Vehicle(String plate, String type, double ratePerHour) {
        this.plate = plate;
        this.type = type;
        this.ratePerHour = ratePerHour;
    }

    String getPlate() { return plate; }
    String getType() { return type; }
    double getRatePerHour() { return ratePerHour; }
}

public class ParkingLot {
    private static final int TOTAL_SLOTS = 10;

    private static HashMap<Integer, Vehicle> slots = new HashMap<>();

    private static ArrayList<String> history = new ArrayList<>();

    private static double totalEarnings = 0;
    private static int vehiclesServed = 0;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 7) {
            System.out.println("\n===== Parking Lot =====");
            System.out.println("1. Park vehicle");
            System.out.println("2. Exit vehicle (pay)");
            System.out.println("3. Show slots");
            System.out.println("4. Find vehicle");
            System.out.println("5. Report");
            System.out.println("6. Parking history");
            System.out.println("7. Quit");
            System.out.print("Choose: ");

            try {
                choice = readInt();

                if (choice == 1) parkVehicle();
                else if (choice == 2) exitVehicle();
                else if (choice == 3) showSlots();
                else if (choice == 4) findVehicle();
                else if (choice == 5) showReport();
                else if (choice == 6) showHistory();
                else if (choice != 7) throw new ParkingException("Invalid choice.");
            } catch (ParkingException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    static void parkVehicle() throws ParkingException {
        int freeSlot = findFreeSlot();
        if (freeSlot == -1) {
            throw new ParkingException("Sorry, parking is full.");
        }

        System.out.print("Vehicle type (1=Bike, 2=Car, 3=Truck): ");
        int type = readInt();
        if (type < 1 || type > 3) {
            throw new ParkingException("Invalid vehicle type.");
        }

        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim().toUpperCase();
        if (plate.isEmpty()) {
            throw new ParkingException("Plate cannot be empty.");
        }
        if (findSlot(plate) != -1) {
            throw new ParkingException("This vehicle is already parked.");
        }

        Vehicle v;
        if (type == 1) v = new Vehicle(plate, "Bike", 20);
        else if (type == 2) v = new Vehicle(plate, "Car", 40);
        else v = new Vehicle(plate, "Truck", 80);

        slots.put(freeSlot, v);
        System.out.println(v.getType() + " " + plate + " parked at slot " + freeSlot
                + " (Rs." + v.getRatePerHour() + " per hour)");
    }

    static void exitVehicle() throws ParkingException {
        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim().toUpperCase();
        int slotNo = findSlot(plate);
        if (slotNo == -1) {
            throw new ParkingException("Vehicle not found.");
        }

        System.out.print("Hours parked: ");
        int hours = readInt();
        if (hours < 1) hours = 1;

        Vehicle v = slots.get(slotNo);
        double fee = hours * v.getRatePerHour();

        slots.remove(slotNo);
        totalEarnings += fee;
        vehiclesServed++;
        history.add(v.getType() + " " + v.getPlate() + " | " + hours + " hour(s) | Rs." + fee);

        System.out.println("Slot " + slotNo + " is free now. Fee for " + hours + " hour(s): Rs." + fee);
    }

    static void showSlots() {
        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            Vehicle v = slots.get(i);
            if (v == null) System.out.println("Slot " + i + ": Free");
            else System.out.println("Slot " + i + ": " + v.getType() + " - " + v.getPlate());
        }
    }

    static void findVehicle() throws ParkingException {
        System.out.print("Plate number: ");
        int slotNo = findSlot(sc.nextLine().trim().toUpperCase());
        if (slotNo == -1) {
            throw new ParkingException("Vehicle not found.");
        }
        System.out.println("Parked at slot " + slotNo);
    }

    static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No completed parkings yet.");
            return;
        }
        System.out.println("--- Parking History ---");
        for (String record : history) {
            System.out.println(record);
        }
    }

    static void showReport() {
        int bikes = 0, cars = 0, trucks = 0;
        for (Vehicle v : slots.values()) {
            if (v.getType().equals("Bike")) bikes++;
            else if (v.getType().equals("Car")) cars++;
            else trucks++;
        }
        System.out.println("--- Report ---");
        System.out.println("Parked now      : " + slots.size() + "/" + TOTAL_SLOTS);
        System.out.println("  Bikes: " + bikes + ", Cars: " + cars + ", Trucks: " + trucks);
        System.out.println("Vehicles served : " + vehiclesServed);
        System.out.println("Total earnings  : Rs." + totalEarnings);
    }

    static int findFreeSlot() {
        for (int i = 1; i <= TOTAL_SLOTS; i++) {
            if (!slots.containsKey(i)) return i;
        }
        return -1;
    }

    static int findSlot(String plate) {
        for (int slotNo : slots.keySet()) {
            if (slots.get(slotNo).getPlate().equals(plate)) return slotNo;
        }
        return -1;
    }

    static int readInt() throws ParkingException {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new ParkingException("Please enter a valid number.");
        }
    }
}