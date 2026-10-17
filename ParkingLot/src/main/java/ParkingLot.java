import java.util.Scanner;

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
    private static Vehicle[] slots = new Vehicle[TOTAL_SLOTS];   // null = free slot
    private static double totalEarnings = 0;
    private static int vehiclesServed = 0;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 6) {
            System.out.println("\n===== Parking Lot =====");
            System.out.println("1. Park vehicle");
            System.out.println("2. Exit vehicle (pay)");
            System.out.println("3. Show slots");
            System.out.println("4. Find vehicle");
            System.out.println("5. Report");
            System.out.println("6. Quit");
            System.out.print("Choose: ");
            choice = readInt();

            if (choice == 1) parkVehicle();
            else if (choice == 2) exitVehicle();
            else if (choice == 3) showSlots();
            else if (choice == 4) findVehicle();
            else if (choice == 5) showReport();
            else if (choice != 6) System.out.println("Invalid choice.");
        }
        System.out.println("Goodbye!");
    }

    static void parkVehicle() {
        int free = -1;
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) {
                free = i;
                break;
            }
        }
        if (free == -1) {
            System.out.println("Sorry, parking is full.");
            return;
        }

        System.out.print("Vehicle type (1=Bike, 2=Car, 3=Truck): ");
        int type = readInt();
        if (type < 1 || type > 3) {
            System.out.println("Invalid type.");
            return;
        }

        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim().toUpperCase();
        if (plate.isEmpty()) {
            System.out.println("Plate cannot be empty.");
            return;
        }
        if (findSlot(plate) != -1) {
            System.out.println("This vehicle is already parked.");
            return;
        }

        Vehicle v;
        if (type == 1) v = new Vehicle(plate, "Bike", 20);
        else if (type == 2) v = new Vehicle(plate, "Car", 40);
        else v = new Vehicle(plate, "Truck", 80);

        slots[free] = v;
        System.out.println(v.getType() + " " + plate + " parked at slot " + (free + 1)
                + " (Rs." + v.getRatePerHour() + " per hour)");
    }

    static void exitVehicle() {
        System.out.print("Plate number: ");
        String plate = sc.nextLine().trim().toUpperCase();
        int index = findSlot(plate);
        if (index == -1) {
            System.out.println("Vehicle not found.");
            return;
        }

        System.out.print("Hours parked: ");
        int hours = readInt();
        if (hours < 1) hours = 1;

        double fee = hours * slots[index].getRatePerHour();
        slots[index] = null;
        totalEarnings += fee;
        vehiclesServed++;
        System.out.println("Slot " + (index + 1) + " is free now. Fee for " + hours + " hour(s): Rs." + fee);
    }

    static void showSlots() {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] == null) System.out.println("Slot " + (i + 1) + ": Free");
            else System.out.println("Slot " + (i + 1) + ": " + slots[i].getType() + " - " + slots[i].getPlate());
        }
    }

    static void findVehicle() {
        System.out.print("Plate number: ");
        int index = findSlot(sc.nextLine().trim().toUpperCase());
        if (index == -1) System.out.println("Vehicle not found.");
        else System.out.println("Parked at slot " + (index + 1));
    }

    static void showReport() {
        int bikes = 0, cars = 0, trucks = 0;
        for (Vehicle v : slots) {
            if (v == null) continue;
            if (v.getType().equals("Bike")) bikes++;
            else if (v.getType().equals("Car")) cars++;
            else trucks++;
        }
        System.out.println("--- Report ---");
        System.out.println("Parked now      : " + (bikes + cars + trucks) + "/" + TOTAL_SLOTS);
        System.out.println("  Bikes: " + bikes + ", Cars: " + cars + ", Trucks: " + trucks);
        System.out.println("Vehicles served : " + vehiclesServed);
        System.out.println("Total earnings  : Rs." + totalEarnings);
    }

    // returns slot index of the plate, or -1 if not found
    static int findSlot(String plate) {
        for (int i = 0; i < TOTAL_SLOTS; i++) {
            if (slots[i] != null && slots[i].getPlate().equals(plate)) return i;
        }
        return -1;
    }

    static int readInt() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
