
public class AntivirusApp {

    // Stores the antivirus application version
    private String version;

    // Stores the User object created by the user system
    private User<?> user;

    // Constructor: initializes the User object and application version
    public AntivirusApp(User<?> user, String version) {

        // Check that the User object is not null
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        // Check that the username is not null or blank
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("User cannot be empty");
        }

        // Check that the version is not null or blank
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version cannot be empty");
        }

        // Store the User object and trimmed version
        this.user = user;
        this.version = version.trim();
    }

    // Displays a message when the scanner is requested
    // TODO: Connect this method to the actual Scanner class
    public void startScanner() {
        System.out.println("Starting antivirus scanner...");
    }

    // TODO: Enable this method when Report.java is available
    // The real scanner will return a Report object
    /*
    public Report scan() {
        startScanner();
        return null;
    }
    */

    // TODO: Connect this method to QuarantineMgr.java
    // Prince's quarantine implementation will be integrated here
    public void quarantine() {
        throw new UnsupportedOperationException(
            "Quarantine functionality not implemented yet"
        );
    }

    // TODO: Connect this method to UpdateService.java
    // Prince's update implementation will be integrated here
    public void update() {
        throw new UnsupportedOperationException(
            "Update functionality not implemented yet"
        );
    }

    // Returns the current antivirus version
    public String getVersion() {
        return version;
    }

    // Returns the current username from the User object
    public String getUser() {
        return user.getUsername();
    }

    // Displays basic information about the antivirus application
    public void displayAppInfo() {
        System.out.println("Mini Antivirus");
        System.out.println("User: " + user.getUsername());
        System.out.println("Version: " + version);
    }

    // Checks whether the current version matches the supplied version
    public boolean isVersion(String versionToCheck) {
        return version.equals(versionToCheck);
    }

    // Checks whether the supplied username matches the current user
    public boolean isUser(String usernameToCheck) {
        return user.getUsername().equals(usernameToCheck);
    }
}

