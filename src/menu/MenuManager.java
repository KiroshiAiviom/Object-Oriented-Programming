package menu;

import exception.InvalidInputException;
import model.ClothingItem;
import model.Jacket;
import model.Shirt;

import java.util.ArrayList;
import java.util.Scanner;

public class MenuManager implements Menu {

    private final ArrayList<ClothingItem> inventory = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayMenu() {
        System.out.println("\n========================================");
        System.out.println(" CLOTHING STORE - ASSIGNMENT 3 (WEEK 6)");
        System.out.println("========================================");
        System.out.println("1. Add Shirt");
        System.out.println("2. Add Jacket");
        System.out.println("3. View All Items");
        System.out.println("4. Try On All Items (Polymorphism)");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    @Override
    public void run() {
        boolean running = true;

        // finally guarantees scanner is closed (Week 6 topic)
        try {
            while (running) {
                displayMenu();

                try {
                    int choice = Integer.parseInt(readText("Enter choice: "));

                    switch (choice) {
                        case 1:
                            addShirt();
                            break;
                        case 2:
                            addJacket();
                            break;
                        case 3:
                            viewAllItems();
                            break;
                        case 4:
                            tryOnAllItems();
                            break;
                        case 0:
                            running = false;
                            System.out.println("Goodbye!");
                            break;
                        default:
                            throw new InvalidInputException("Invalid menu choice: " + choice);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid number.");
                } catch (IllegalArgumentException e) {
                    // Thrown from setters/constructors
                    System.out.println("Error: " + e.getMessage());
                } catch (InvalidInputException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } finally {
            scanner.close();
        }
    }

    // ----------------------
    // Actions
    // ----------------------

    private void addShirt() throws InvalidInputException {
        System.out.println("\n--- ADD SHIRT ---");

        int id = Integer.parseInt(readText("Item ID: "));
        String name = readText("Name: ");
        String size = readText("Size (S/M/L): ");
        double price = Double.parseDouble(readText("Price (KZT): "));
        String sleeves = readText("Sleeve type (Short/Long): ");

        inventory.add(new Shirt(id, name, size, price, sleeves));
        System.out.println("Shirt added.");
    }

    private void addJacket() throws InvalidInputException {
        System.out.println("\n--- ADD JACKET ---");

        int id = Integer.parseInt(readText("Item ID: "));
        String name = readText("Name: ");
        String size = readText("Size (S/M/L): ");
        double price = Double.parseDouble(readText("Price (KZT): "));
        String season = readText("Season (Winter/Autumn/Spring): ");

        inventory.add(new Jacket(id, name, size, price, season));
        System.out.println("Jacket added.");
    }

    private void viewAllItems() {
        System.out.println("\n--- INVENTORY ---");

        if (inventory.isEmpty()) {
            System.out.println("No items yet.");
            return;
        }

        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
    }

    private void tryOnAllItems() {
        System.out.println("\n--- TRY ON ALL ITEMS ---");

        if (inventory.isEmpty()) {
            System.out.println("No items yet.");
            return;
        }

        // Polymorphism: calls Shirt.wear() or Jacket.wear()
        for (ClothingItem item : inventory) {
            item.wear();
        }
    }

    // ----------------------
    // Input helper
    // ----------------------

    private String readText(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String text = scanner.nextLine().trim();

        if (text.isEmpty()) {
            throw new InvalidInputException("Input cannot be empty.");
        }

        return text;
    }
}
