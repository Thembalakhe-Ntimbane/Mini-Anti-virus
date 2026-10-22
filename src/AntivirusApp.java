
public class AntivirusApp {

    // Stores the antivirus application version
    private String version;

    // Stores the name of the current user
    private String user;

    // Constructor: initializes the user and application version
    public AntivirusApp(String user, String version) {

        // Check that the username is not null or blank
        if (user == null || user.isBlank()) {
            throw new IllegalArgumentException("User cannot be empty");
        }

        // Check that the version is not null or blank
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version cannot be empty");
        }


     // Store the user and version without surrounding spaces
     this.user = user.trim();
     this.version = version.trim();

    }

    // Displays a message when the scanner is requested
    // TODO: Connect this method to the actual Scanner class
    public void startScanner() {
        System.out.println("Starting antivirus scanner...");
    }

    // TODO: Enable this method when Report.java is available.
    // The real scanner will return a Report object.
    /*
    public Report scan() {
        startScanner();
        return null;
    }
    */


    // Returns the current antivirus version
    public String getVersion() {
        return version;
    }

    // Returns the current username
    public String getUser() {
        return user;
    }

    // Displays basic information about the antivirus application
    public void displayAppInfo() {
        System.out.println("Mini Antivirus");
        System.out.println("User: " + user);
        System.out.println("Version: " + version);
    }

    // Checks whether the current version matches the supplied version
    public boolean isVersion(String versionToCheck) {
        return version.equals(versionToCheck);
    }
  
    // Checks whether the supplied username matches the current user
    public boolean isUser(String usernameToCheck) {
        return user.equals(usernameToCheck);
    }


    
}
