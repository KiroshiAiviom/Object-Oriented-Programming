package menu;

import dao.ClothingItemDAO;
import exception.InvalidInputException;
import model.ClothingItem;
import model.Jacket;
import model.Shirt;

import java.util.List;
import java.util.Scanner;

/**
 * MenuManager:
 * - Implements Menu (Week 6 requirement)
 * - Loads data from database on demand (NO in-memory inventory ArrayList field)
 * - Contains CRUD + search options required for Week 7/8 defence
 */
public class MenuManager implements Menu {

    private final ClothingItemDAO dao = new ClothingItemDAO();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void displayMenu() {
        System.out.println("\n=== CLOTHING STORE (Assignment 4 / Week 7-8) ===");
        System.out.println("1. Add Shirt (INSERT)");
        System.out.println("2. Add Jacket (INSERT)");
        System.out.println("3. View All Items (SELECT)");
        System.out.println("4. View Shirts Only (Filtered SELECT)");
        System.out.println("5. View Jackets Only (Filtered SELECT)");
        System.out.println("6. Get Item by ID (getById SELECT)");
        System.out.println("7. Update Shirt by ID (UPDATE)");
        System.out.println("8. Update Jacket by ID (UPDATE)");
        System.out.println("9. Delete Item by ID (DELETE)");
        System.out.println("10. Search by Name (ILIKE)");
        System.out.println("11. Search by Price Range (BETWEEN)");
        System.out.println("12. Search by Min Price (>=)");
        System.out.println("0. Exit");
    }

    @Override
    public void run() {
        boolean running = true;

        try {
            while (running) {
                displayMenu();

                try {
                    int choice = readInt("Choice: ");

                    switch (choice) {
                        case 1: addShirt(); break;
                        case 2: addJacket(); break;
                        case 3: printItems(dao.getAll()); break;
                        case 4: printItems(dao.getByType("SHIRT")); break;
                        case 5: printItems(dao.getByType("JACKET")); break;
                        case 6: getById(); break;
                        case 7: updateShirt(); break;
                        case 8: updateJacket(); break;
                        case 9: deleteItem(); break;
                        case 10: searchByName(); break;
                        case 11: searchByRange(); break;
                        case 12: searchByMin(); break;
                        case 0:
                            running = false;
                            System.out.println("Goodbye!");
                            break;
                        default:
                            throw new InvalidInputException("Invalid menu choice.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error: enter a valid number.");
                } catch (InvalidInputException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    // Thrown by model validation (setters)
                    System.out.println("Error: " + e.getMessage());
                }

                if (running) pause();
            }
        } finally {
            scanner.close();
        }
    }

    // ------------------ INSERT ------------------

    private void addShirt() throws InvalidInputException {
        int id = readInt("Item ID: ");
        String name = readText("Name: ");
        String size = readText("Size: ");
        double price = readDouble("Price: ");
        String sleeves = readText("Sleeve type: ");

        Shirt shirt = new Shirt(id, name, size, price, sleeves);

        boolean ok = dao.insertShirt(shirt);
        System.out.println(ok ? "Inserted." : "Insert failed.");
    }

    private void addJacket() throws InvalidInputException {
        int id = readInt("Item ID: ");
        String name = readText("Name: ");
        String size = readText("Size: ");
        double price = readDouble("Price: ");
        String season = readText("Season: ");

        Jacket jacket = new Jacket(id, name, size, price, season);

        boolean ok = dao.insertJacket(jacket);
        System.out.println(ok ? "Inserted." : "Insert failed.");
    }

    // ------------------ READ (getById) ------------------

    private void getById() throws InvalidInputException {
        int id = readInt("Enter item ID: ");
        ClothingItem item = dao.getById(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println(item);
        System.out.println("10% off price: " + String.format("%.2f KZT", item.getDiscountedPrice(10)));
    }

    // ------------------ UPDATE ------------------

    private void updateShirt() throws InvalidInputException {
        int id = readInt("Shirt ID: ");

        ClothingItem existing = dao.getById(id);
        if (existing == null) {
            System.out.println("Item not found.");
            return;
        }
        if (!(existing instanceof Shirt)) {
            System.out.println("This ID is not a SHIRT.");
            return;
        }

        String name = readText("New name: ");
        String size = readText("New size: ");
        double price = readDouble("New price: ");
        String sleeves = readText("New sleeve type: ");

        // Validate via constructor
        new Shirt(id, name, size, price, sleeves);

        boolean ok = dao.updateShirt(id, name, size, price, sleeves);
        System.out.println(ok ? "Updated." : "Update failed.");
    }

    private void updateJacket() throws InvalidInputException {
        int id = readInt("Jacket ID: ");

        ClothingItem existing = dao.getById(id);
        if (existing == null) {
            System.out.println("Item not found.");
            return;
        }
        if (!(existing instanceof Jacket)) {
            System.out.println("This ID is not a JACKET.");
            return;
        }

        String name = readText("New name: ");
        String size = readText("New size: ");
        double price = readDouble("New price: ");
        String season = readText("New season: ");

        new Jacket(id, name, size, price, season);

        boolean ok = dao.updateJacket(id, name, size, price, season);
        System.out.println(ok ? "Updated." : "Update failed.");
    }

    // ------------------ DELETE ------------------

    private void deleteItem() throws InvalidInputException {
        int id = readInt("Item ID to delete: ");

        // "Safe deletion pattern": show the record, then confirm delete.
        ClothingItem item = dao.getById(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("Found: " + item);
        String confirm = readText("Type YES to confirm deletion: ");

        if (!"YES".equalsIgnoreCase(confirm)) {
            System.out.println("Cancelled.");
            return;
        }

        boolean ok = dao.deleteById(id);
        System.out.println(ok ? "Deleted." : "Delete failed.");
    }

    // ------------------ SEARCH ------------------

    private void searchByName() throws InvalidInputException {
        String part = readText("Name contains: ");
        printItems(dao.searchByName(part));
    }

    private void searchByRange() throws InvalidInputException {
        double min = readDouble("Min price: ");
        double max = readDouble("Max price: ");
        if (min > max) throw new IllegalArgumentException("Min cannot be greater than max.");
        printItems(dao.searchByPriceRange(min, max));
    }

    private void searchByMin() throws InvalidInputException {
        double min = readDouble("Min price: ");
        printItems(dao.searchByMinPrice(min));
    }

    // ------------------ Output helper ------------------

    private void printItems(List<ClothingItem> items) {
        if (items.isEmpty()) {
            System.out.println("No results.");
            return;
        }
        for (ClothingItem item : items) {
            System.out.println(item);
        }
    }

    // ------------------ Input helpers ------------------

    private int readInt(String prompt) throws InvalidInputException {
        return Integer.parseInt(readText(prompt));
    }

    private double readDouble(String prompt) throws InvalidInputException {
        return Double.parseDouble(readText(prompt));
    }

    private String readText(String prompt) throws InvalidInputException {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) throw new InvalidInputException("Input cannot be empty.");
        return s;
    }

    private void pause() {
        System.out.print("\nPress Enter...");
        scanner.nextLine();
    }
}
