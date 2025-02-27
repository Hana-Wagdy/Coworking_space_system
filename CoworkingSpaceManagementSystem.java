import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// Class for Books
class Book {
    private String title; // Title of the book
    private String id;    // Unique ID for the book
    private boolean Available; // Availability status of the book
    private float price; // Price of the book

    // Constructor to initialize a book
    public Book(String title, String id, float price) {
        this.title = title;
        this.id = id;
        this.price = price;
        this.Available = true; // By default, the book is available
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getId() { return id; }
    public boolean isAvailable() { return Available; }
    public void setAvailable(boolean Available) { this.Available = Available; }
    public float getPrice() { return price; }

    @Override
    public String toString() {
        return "Title: " + title + " | ID: " + id + " | Price: $" + price;
    }
}

// Class for Library
class Library {
    private ArrayList<Book> books; // List of books in the library
    private float rev; // Total revenue generated from book sales and borrowing

    public Library() {
        books = new ArrayList<>();
        rev = 0.0f;
    }

    // Add a book to the library
    void addBook(Book b) {
        books.add(b);
    }

    // Display all available books
    public void showBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            System.out.println("\n--- Available Books ---");
            boolean found = false;
            for (Book b : books) {
                if (b.isAvailable()) {
                    System.out.println(b);
                    found = true;
                }
            }
            if (!found) System.out.println("No available books found.");
        }
    }

    // Check if a book is available by its ID
    public boolean isBookAvailable(String id) {
        for (Book b : books) {
            if (b.getId().equals(id)) return b.isAvailable();
        }
        return false; // Book not found
    }

    // Borrow a book and calculate the fee
    public float borrowBook(String id, float start, float end) {
        for (Book b : books) {
            if (b.getId().equals(id)) {
                if (b.isAvailable()) {
                    b.setAvailable(false); // Mark the book as borrowed
                    float dur = end - start;  // Calculate duration
                    float fee = dur * 5.0f; // $5 per hour
                    System.out.println("Borrowed: " + b.getTitle());
                    System.out.println("Fee: $" + fee);
                    return fee;
                } else {
                    System.out.println("Book is already borrowed.");
                    return 0.0f;
                }
            }
        }
        System.out.println("Book not found.");
        return 0.0f;
    }

    // Return a borrowed book
    public void returnBook(String id) {
        for (Book b : books) {
            if (b.getId().equals(id)) {
                if (!b.isAvailable()) {
                    b.setAvailable(true); // Mark the book as available
                    System.out.println("Returned: " + b.getTitle());
                } else {
                    System.out.println("Book is already available.");
                }
                return;
            }
        }
        System.out.println("Book not found.");
    }

    // Buy a book and add its price to revenue
    public float buyBook(String id) {
        for (Book b : books) {
            if (b.getId().equals(id)) {
                if (b.isAvailable()) {
                    books.remove(b); // Remove the book from the library
                    rev += b.getPrice(); // Add the price to revenue
                    System.out.println("Purchased: " + b.getTitle() + " for $" + b.getPrice());
                    return b.getPrice();
                } else {
                    System.out.println("Book is borrowed and cannot be purchased.");
                }
                return 0.0f;
            }
        }
        System.out.println("Book not found.");
        return 0.0f;
    }

    // Display total revenue
    public void showRev() {
        System.out.println("Total revenue: $" + rev);
    }

    // Add to revenue
    public void addRev(float amt) {
        rev += amt;
    }
}

// Class for Table Reservation
class TableRes {
    private String name; // Name of the student reserving the table
    private float start;  // Start time of reservation
    private float end;    // End time of reservation

    public TableRes(String name, float start, float end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    // Getters
    public String getName() { return name; }
    public float getStart() { return start; }
    public float getEnd() { return end; }
}

// Class for Room Reservation
class RoomRes {
    private String name; // Name of the student reserving the room
    private float start;  // Start time of reservation
    private float end;    // End time of reservation

    public RoomRes(String name, float start, float end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    // Getters
    public String getName() { return name; }
    public float getStart() { return start; }
    public float getEnd() { return end; }
}

// Class for Coworking Space
class CoworkingSpace {
    private int tables; // Total number of tables
    private int rooms;  // Total number of rooms
    private ArrayList<ArrayList<TableRes>> T_res; // Table reservations
    private ArrayList<ArrayList<RoomRes>> R_res; // Room reservations
    private HashMap<String, ArrayList<Float>> fees; // Fees for each student

    public CoworkingSpace(int tables, int rooms) {
        this.tables = tables;
        this.rooms = rooms;
        this.T_res = new ArrayList<>();
        this.R_res = new ArrayList<>();
        this.fees = new HashMap<>();
        for (int i = 0; i < tables; i++) T_res.add(new ArrayList<>()); // Initialize table reservations
        for (int i = 0; i < rooms; i++) R_res.add(new ArrayList<>()); // Initialize room reservations
    }

    // Check if a table is available during the requested time
    private boolean isTableAvailable(int idx, float start, float end) {
        for (TableRes res : T_res.get(idx)) {
            if (!(end <= res.getStart() || start >= res.getEnd())) return false; // Overlapping reservation
        }
        return true; // Table is available
    }

    // Reserve a table and calculate the fee
    public float reserveTable(String name, float start, float end) {
        for (int i = 0; i < tables; i++) {
            if (isTableAvailable(i, start, end)) {
                T_res.get(i).add(new TableRes(name, start, end)); // Add reservation
                float fee = (end - start >= 4.0f) ? 60.0f : (end - start) * 25.0f; // Calculate fee
                addFee(name, fee); // Add fee to student's record
                System.out.println("Table " + (i + 1) + " reserved for " + name + " from " + formatTime(start) + " to " + formatTime(end) + ".");
                System.out.println("Fee: $" + fee);
                return fee;
            }
        }
        System.out.println("No tables available.");
        return 0.0f;
    }

    // Check if a room is available during the requested time
    private boolean AvailableRoom(int idx, float start, float end) {
        for (RoomRes res : R_res.get(idx)) {
            if (!(end <= res.getStart() || start >= res.getEnd())) return false; // Overlapping reservation
        }
        return true; // Room is available
    }

    // Reserve a room and calculate the fee
    public float reserveRoom(String name, float start, float end) {
        for (int i = 0; i < rooms; i++) {
            if (AvailableRoom(i, start, end)) {
                R_res.get(i).add(new RoomRes(name, start, end)); // Add reservation
                float fee = (end - start) * 100.0f; // $100 per hour
                addFee(name, fee); // Add fee to student's record
                System.out.println("Room " + (i + 1) + " reserved for " + name + " from " + formatTime(start) + " to " + formatTime(end) + ".");
                System.out.println("Fee: $" + fee);
                return fee;
            }
        }
        System.out.println("No rooms available.");
        return 0.0f;
    }

    // Add fee for a student
    public void addFee(String name, float fee) {
        if (!fees.containsKey(name)) fees.put(name, new ArrayList<>()); // Initialize fee list if not exists
        fees.get(name).add(fee); // Add fee to the list
    }

    // Calculate total fee for a student
    public float calcTotalFee(String name) {
        if (fees.containsKey(name)) {
            float total = 0.0f;
            for (float f : fees.get(name)) total += f; // Sum all fees
            return total;
        } else {
            System.out.println("No fees recorded for " + name);
            return 0.0f;
        }
    }

    // Remove all reservations for a student
    public void removeRes(String name) {
        for (ArrayList<TableRes> table : T_res) table.removeIf(res -> res.getName().equals(name)); // Remove table reservations
        for (ArrayList<RoomRes> room : R_res) room.removeIf(res -> res.getName().equals(name)); // Remove room reservations
        System.out.println("Reservations removed for " + name);
    }

    // Clear fees for a student
    public void clearFees(String name) {
        fees.remove(name); // Remove student's fee record
    }

    // Clear all reservations and calculate total fees
    public float clearAllRes() {
        float total = 0.0f;
        for (ArrayList<Float> f : fees.values()) for (float fee : f) total += fee; // Sum all fees
        for (ArrayList<TableRes> table : T_res) table.clear(); // Clear table reservations
        for (ArrayList<RoomRes> room : R_res) room.clear(); // Clear room reservations
        fees.clear(); // Clear all fee records
        System.out.println("All reservations cleared. Total fees: $" + total);
        return total;
    }

    // Display coworking space status
    public void showStatus() {
        System.out.println("\n--- Coworking Space Status ---");
        System.out.println("Tables:");
        for (int i = 0; i < tables; i++) {
            System.out.println("  Table " + (i + 1) + " Reservations:");
            if (T_res.get(i).isEmpty()) System.out.println("    No reservations.");
            else for (TableRes res : T_res.get(i)) System.out.println("    " + res.getName() + " - " + formatTime(res.getStart()) + " to " + formatTime(res.getEnd()));
        }
        System.out.println("Rooms:");
        for (int i = 0; i < rooms; i++) {
            System.out.println("  Room " + (i + 1) + " Reservations:");
            if (R_res.get(i).isEmpty()) System.out.println("    No reservations.");
            else for (RoomRes res : R_res.get(i)) System.out.println("    " + res.getName() + " - " + formatTime(res.getStart()) + " to " + formatTime(res.getEnd()));
        }
    }

    // Helper method to format time (e.g., 12.5 -> 12:30)
    private String formatTime(float time) {
        int hours = (int) time;
        int minutes = (int) ((time - hours) * 60+1);
        return String.format("%02d:%02d", hours, minutes);
    }
}

// Main class
public class CoworkingSpaceManagementSystem {
    public static void main(String[] args) {
        Library lib = new Library();
        CoworkingSpace space = new CoworkingSpace(10, 5); // 10 tables, 5 rooms
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        // Display fee rules
        System.out.println("--- Fee Rules ---");
        System.out.println("1. Borrowing a book: $5 per hour.");
        System.out.println("2. Reserving a table: $25 per hour (flat $60 for 4+ hours).");
        System.out.println("3. Reserving a room: $100 per hour.");
        System.out.println("4. Buying a book: Price of the book.");
        System.out.println("-----------------");

        while (running) {
            System.out.println("\n--- Co-working Space and Library System ---");
            System.out.println("1. Add a Book");
            System.out.println("2. Show Books");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. Buy a Book");
            System.out.println("6. Show Revenue");
            System.out.println("7. Reserve a Table");
            System.out.println("8. Reserve a Room");
            System.out.println("9. Show Space Status");
            System.out.println("10. Student Exit");
            System.out.println("11. Clear All Reservations");
            System.out.println("12. Exit");
            System.out.print("Choose an option: \n\n");

            int opt = sc.nextInt();
            sc.nextLine();  

            switch (opt) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter book ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter book price: ");
                    float price = sc.nextFloat();
                    sc.nextLine(); // Consume newline
                    if (title.isEmpty() || id.isEmpty() || price <= 0) {
                        System.out.println("Invalid input. Book not added.");
                    } else {
                        lib.addBook(new Book(title, id, price));
                        System.out.println("Book added.");
                    }
                    break;

                case 2:
                    lib.showBooks();
                    break;

                case 3:
                    System.out.print("Enter student name: ");
                    String sName = sc.nextLine();
                    System.out.print("Enter book ID: ");
                    String bId = sc.nextLine();
                    if (lib.isBookAvailable(bId)) {
                        float start = getTime("From (HH:MM and 24 format): ");
                        float end = getTime("To (HH:MM format): ");
                        if (end > start) {
                            float fee = lib.borrowBook(bId, start, end);
                            space.addFee(sName, fee);
                        } else {
                            System.out.println("Invalid time range.");
                        }
                    } else {
                        System.out.println("Book not available.");
                    }
                    break;

                case 4:
                    System.out.print("Enter book ID: ");
                    String rId = sc.nextLine();
                    lib.returnBook(rId);
                    break;

                case 5:
                    System.out.print("Enter student name: ");
                    String buyName = sc.nextLine();
                    System.out.print("Enter book ID: ");
                    String buyId = sc.nextLine();
                    float fee = lib.buyBook(buyId);
                    if (fee > 0) space.addFee(buyName, fee);
                    break;

                case 6:
                    lib.showRev();
                    break;

                case 7:
                    System.out.print("Enter student name: ");
                    String tName = sc.nextLine();
                    float tStart = getTime("Start time (HH:MM and 24 format): ");
                    float tEnd = getTime("End time: ");
                    if (tEnd > tStart) space.reserveTable(tName, tStart, tEnd);
                    else System.out.println("Invalid time range.");
                    break;

                case 8:
                    System.out.print("Enter student name: ");
                    String rName = sc.nextLine();
                    float rStart = getTime("Start time (HH:MM and 24 format): ");
                    float rEnd = getTime("End time: ");
                    if (rEnd > rStart) space.reserveRoom(rName, rStart, rEnd);
                    else System.out.println("Invalid time range.");
                    break;

                case 9:
                    space.showStatus();
                    break;

                case 10:
                    System.out.print("Enter student name: ");
                    String exitName = sc.nextLine();
                    float totalFee = space.calcTotalFee(exitName);
                    lib.addRev(totalFee);
                    System.out.println("Total fee for " + exitName + ": $" + totalFee);
                    space.removeRes(exitName);
                    space.clearFees(exitName);
                    break;

                case 11:
                    float total = space.clearAllRes();
                    lib.addRev(total);
                    System.out.println("Fees added to revenue.");
                    break;

                case 12:
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
        sc.close();
    }

    // Helper method to get valid time in HH:MM format and convert to float
    private static float getTime(String prompt) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine();
            try {
                String[] parts = input.split(":");
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                    System.out.println("Invalid time. Enter time in HH:MM format (0-23 hours, 0-59 minutes).");
                    continue;
                }
                return hours + (minutes / 60.0f); // Convert to decimal hours
            } catch (Exception e) {
                System.out.println("Invalid format. Enter time in HH:MM format.");
            }
        }
    }
}