import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Campus Lost and Found Management System - Console (Terminal) Edition.
 * No GUI. Pure text menu driven using java.util.Scanner.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final LostFoundManager manager = new LostFoundManager();
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        seedSampleData();
        boolean running = true;
        printBanner();

        while (running) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1": registerLostItemFlow(); break;
                    case "2": registerFoundItemFlow(); break;
                    case "3": itemSearchMenu(); break;
                    case "4": claimManagementMenu(); break;
                    case "5": ownershipVerificationFlow(); break;
                    case "6": returnProcessingFlow(); break;
                    case "7": statusUpdateFlow(); break;
                    case "8": historyMenu(); break;
                    case "9": reportsMenu(); break;
                    case "10": registerStudentFlow(); break;
                    case "11": viewAllItemsSorted(); break;
                    case "0":
                        running = false;
                        System.out.println("\nExiting Campus Lost and Found Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (ValidationException | ItemNotFoundException | InvalidClaimException e) {
                // Java Concept: Exception Handling
                System.out.println("\n[ERROR] " + e.getMessage());
            } catch (Exception e) {
                System.out.println("\n[UNEXPECTED ERROR] " + e.getMessage());
            }
        }
        sc.close();
    }

    // ================= MENUS =================

    private static void printBanner() {
        System.out.println("=======================================================");
        System.out.println("   CAMPUS LOST AND FOUND MANAGEMENT SYSTEM (Terminal)");
        System.out.println("=======================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n--------------------- MAIN MENU ----------------------");
        System.out.println(" 1. Lost Item Registration");
        System.out.println(" 2. Found Item Registration");
        System.out.println(" 3. Item Search");
        System.out.println(" 4. Claim Management (file a claim)");
        System.out.println(" 5. Ownership Verification");
        System.out.println(" 6. Return Processing");
        System.out.println(" 7. Status Update");
        System.out.println(" 8. History (Claims / Returns)");
        System.out.println(" 9. Reports");
        System.out.println("10. Register Student");
        System.out.println("11. View All Items (sorted)");
        System.out.println(" 0. Exit");
        System.out.print("Enter your choice: ");
    }

    // ================= 1. LOST ITEM REGISTRATION =================

    private static void registerLostItemFlow() throws ValidationException {
        System.out.println("\n-- Register Lost Item --");
        String desc = prompt("Description: ");
        String category = promptCategory();
        String location = prompt("Location last seen: ");
        LocalDate date = promptDate("Date lost (yyyy-MM-dd): ");
        String name = prompt("Reporter name: ");
        String contact = prompt("Reporter contact: ");

        LostItem item = manager.registerLostItem(desc, category, location, date, name, contact);
        System.out.println("Lost item registered successfully with ID: " + item.getItemId());
    }

    // ================= 2. FOUND ITEM REGISTRATION =================

    private static void registerFoundItemFlow() throws ValidationException {
        System.out.println("\n-- Register Found Item --");
        String desc = prompt("Description: ");
        String category = promptCategory();
        String location = prompt("Location found: ");
        LocalDate date = promptDate("Date found (yyyy-MM-dd): ");
        String name = prompt("Finder name: ");
        String contact = prompt("Finder contact: ");

        FoundItem item = manager.registerFoundItem(desc, category, location, date, name, contact);
        System.out.println("Found item registered successfully with ID: " + item.getItemId());
    }

    // ================= 3. ITEM SEARCH =================

    private static void itemSearchMenu() throws ItemNotFoundException {
        System.out.println("\n-- Item Search --");
        System.out.println("1. Search by Item ID");
        System.out.println("2. Search by Category");
        System.out.println("3. Search by Location");
        String opt = prompt("Choose: ");
        switch (opt) {
            case "1": {
                String id = prompt("Enter Item ID: ");
                Item item = manager.searchById(id);
                printItemTableHeader();
                System.out.println(item);
                break;
            }
            case "2": {
                String category = promptCategory();
                List<Item> results = manager.searchByCategory(category);
                printItemList(results);
                break;
            }
            case "3": {
                String loc = prompt("Enter location keyword: ");
                List<Item> results = manager.searchByLocation(loc);
                printItemList(results);
                break;
            }
            default:
                System.out.println("Invalid option.");
        }
    }

    // ================= 4. CLAIM MANAGEMENT =================

    private static void claimManagementMenu() throws ItemNotFoundException, InvalidClaimException, ValidationException {
        System.out.println("\n-- File a Claim --");
        String itemId = prompt("Enter Found Item ID to claim: ");
        String studentId = prompt("Enter your Student ID: ");
        Claim claim = manager.fileClaim(itemId, studentId);
        System.out.println("Claim filed successfully. Claim ID: " + claim.getClaimId()
                + " (status set to CLAIMED). Proceed to Ownership Verification next.");
    }

    // ================= 5. OWNERSHIP VERIFICATION =================

    private static void ownershipVerificationFlow() throws InvalidClaimException {
        System.out.println("\n-- Ownership Verification --");
        String claimId = prompt("Enter Claim ID to verify: ");
        manager.verifyOwnership(claimId);
        System.out.println("Ownership verified for claim " + claimId + ". Ready for return processing.");
    }

    // ================= 6. RETURN PROCESSING =================

    private static void returnProcessingFlow() throws InvalidClaimException, ItemNotFoundException {
        System.out.println("\n-- Return Processing --");
        String claimId = prompt("Enter Claim ID to process return: ");
        manager.processReturn(claimId);
        System.out.println("Item returned successfully. Claim " + claimId + " closed.");
    }

    // ================= 7. STATUS UPDATE =================

    private static void statusUpdateFlow() throws ItemNotFoundException, ValidationException {
        System.out.println("\n-- Status Update --");
        String itemId = prompt("Enter Item ID: ");
        Item item = manager.getItem(itemId);
        System.out.println("Current status: " + item.getStatus());
        System.out.println("Allowed statuses: " + Arrays.toString(Item.STATUSES));
        String newStatus = prompt("Enter new status: ").toUpperCase();
        if (!Item.isValidStatus(newStatus)) {
            throw new ValidationException("Invalid status: " + newStatus);
        }
        item.setStatus(newStatus);
        System.out.println("Status updated to " + newStatus + " for item " + itemId + ".");
    }

    // ================= 8. HISTORY =================

    private static void historyMenu() {
        System.out.println("\n-- History --");
        System.out.println("1. Full Claim/Return History");
        System.out.println("2. History for a specific Item ID");
        String opt = prompt("Choose: ");
        List<Claim> claims;
        if ("2".equals(opt)) {
            String itemId = prompt("Enter Item ID: ");
            claims = manager.getClaimsForItem(itemId);
        } else {
            claims = manager.getClaimHistory();
        }
        if (claims.isEmpty()) {
            System.out.println("No claim history found.");
            return;
        }
        System.out.println(String.format("%-8s | %-6s | %-20s | %-11s | %-14s | %-14s",
                "ClaimID", "ItemID", "Claimant", "ClaimDate", "Verified", "Returned"));
        for (Claim c : claims) System.out.println(c);
    }

    // ================= 9. REPORTS =================

    private static void reportsMenu() {
        System.out.println("\n-- Lost and Found Report --");
        System.out.println("Total Items    : " + manager.totalItems());
        System.out.println("Total Claims   : " + manager.totalClaims());
        System.out.println("Total Students : " + manager.totalStudents());

        System.out.println("\nItems by Status:");
        for (Map.Entry<String, Long> e : manager.reportCountByStatus().entrySet()) {
            System.out.println("  " + e.getKey() + " : " + e.getValue());
        }

        System.out.println("\nItems by Category:");
        for (Map.Entry<String, Integer> e : manager.reportCountByCategory().entrySet()) {
            System.out.println("  " + e.getKey() + " : " + e.getValue());
        }
    }

    // ================= 10. REGISTER STUDENT =================

    private static void registerStudentFlow() throws ValidationException {
        System.out.println("\n-- Register Student --");
        String id = prompt("Student ID: ");
        String name = prompt("Name: ");
        String contact = prompt("Contact: ");
        String dept = prompt("Department: ");
        manager.registerStudent(id, name, contact, dept);
        System.out.println("Student registered successfully.");
    }

    // ================= 11. VIEW ALL ITEMS SORTED =================

    private static void viewAllItemsSorted() {
        System.out.println("\n-- View All Items --");
        System.out.println("1. Sort by Date");
        System.out.println("2. Sort by Category");
        System.out.println("3. Sort by Status");
        String opt = prompt("Choose: ");
        List<Item> result;
        switch (opt) {
            case "2": result = manager.sortByCategory(); break;
            case "3": result = manager.sortByStatus(); break;
            default: result = manager.sortByDate();
        }
        printItemList(result);
    }

    // ================= HELPERS =================

    private static void printItemList(List<Item> results) {
        if (results.isEmpty()) {
            System.out.println("No items found.");
            return;
        }
        printItemTableHeader();
        for (Item item : results) System.out.println(item);
    }

    private static void printItemTableHeader() {
        System.out.println(String.format("%-6s | %-6s | %-11s | %-25s | %-15s | %-11s | %-9s",
                "ID", "Type", "Category", "Description", "Location", "Date", "Status"));
    }

    private static String prompt(String label) {
        System.out.print(label);
        return sc.nextLine().trim();
    }

    private static String promptCategory() {
        while (true) {
            String category = prompt("Category " + Arrays.toString(Item.CATEGORIES) + ": ");
            if (Item.isValidCategory(category)) return category;
            System.out.println("Invalid category, please choose from the list.");
        }
    }

    private static LocalDate promptDate(String label) {
        while (true) {
            String input = prompt(label);
            try {
                LocalDate date = LocalDate.parse(input, FMT);
                if (date.isAfter(LocalDate.now())) {
                    System.out.println("Date cannot be in the future. Try again.");
                    continue;
                }
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }

    // ================= SAMPLE DATA =================

    private static void seedSampleData() {
        try {
            manager.registerStudent("S001", "Aarav Sharma", "9876543210", "Computer Science");
            manager.registerStudent("S002", "Diya Patel", "9876500000", "Electronics");

            manager.registerLostItem("Black leather wallet", "Accessories", "Library Block A",
                    LocalDate.now().minusDays(3), "Rohan Mehta", "9998887770");

            manager.registerFoundItem("Blue umbrella", "Others", "Canteen",
                    LocalDate.now().minusDays(1), "Security Desk", "0000000000");

            manager.registerFoundItem("Wired earphones", "Electronics", "Room 204",
                    LocalDate.now().minusDays(2), "Diya Patel", "9876500000");
        } catch (ValidationException e) {
            // sample data is known-good; ignore
        }
    }
}
