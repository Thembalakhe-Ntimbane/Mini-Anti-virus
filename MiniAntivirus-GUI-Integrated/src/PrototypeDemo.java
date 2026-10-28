public class PrototypeDemo {

    public static void main(String[] args) {

        // Create the user and antivirus application
        User<?> user = new User<>("Kamogelo");
        AntivirusApp app = new AntivirusApp(user, "1.0.00");

        // Use Java's console input scanner
        java.util.Scanner input = new java.util.Scanner(System.in);

        int choice = 0;

        // Display the menu until Exit is selected
        while (choice != 4) {

            System.out.println("\n===== MINI ANTIVIRUS =====");
            System.out.println("1. Scan test folder");
            System.out.println("2. Quarantine test file");
            System.out.println("3. Check for updates");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            // Check that the input is a number
            if (!input.hasNextInt()) {
                System.out.println("Please enter a number.");
                input.next();
                continue;
            }

            choice = input.nextInt();

            if (choice == 1) {

                // Scan and display the report
                Report report = app.scan();

                System.out.println("Scan report:");
                System.out.println("Date: " + report.date);
                System.out.println("Files flagged: " + report.threats);

            } else if (choice == 2) {

                // Move the selected harmless test file
                System.out.print("Enter file path: ");
                input.nextLine();
                String filePath = input.nextLine();
                app.quarantine(filePath);

            } else if (choice == 3) {

                // Check the demonstration database version
                app.update();

            } else if (choice == 4) {

                System.out.println("Exiting Mini Antivirus.");

            } else {

                System.out.println("Invalid menu choice.");
            }
        }

        input.close();
    }
}
