
/**
 * Handles update-related operations for the Mini Antivirus application.
 */
public class UpdateService {

    // Stores the version of the currently installed antivirus database
    private int currentVersion = 3;

    // Stores the latest available database version (example value)
    private int latestVersion = 3;


    
/**
 * Constructor: initializes the antivirus database versions.
 *
 * @param currentVersion the installed database version
 * @param latestVersion the latest available database version
 */
public UpdateService(int currentVersion, int latestVersion) {

    // Reject negative version numbers
    if (currentVersion < 0 || latestVersion < 0) {
        throw new IllegalArgumentException(
            "Database versions cannot be negative"
        );
    }

    // Store the installed database version
    this.currentVersion = currentVersion;

    // Store the latest available database version
    this.latestVersion = latestVersion;
}


/**
 * Returns the version of the currently installed
 * antivirus database.
 *
 * @return the installed database version
 */
public int getCurrentVersion() {

    // Return the version stored in this object
    return currentVersion;
}


/**
 * Returns the latest available antivirus database version.
 *
 * @return the latest available database version
 */
public int getLatestVersion() {

    // Return the latest version stored in this object
    return latestVersion;
}

/**
 * Checks whether a newer antivirus database version is available.
 *
 * @return true if a newer database version is available,
 *         otherwise false
 */
public boolean check() {

    // Compare the latest available version
    // with the currently installed version
    return latestVersion > currentVersion;
}



    
/**
 * Attempts to update the antivirus database.
 */
public void updateDB() {

    // Check whether a newer database version is available
    boolean updateAvailable = check();

    // Stop if the database is already up to date
    if (!updateAvailable) {
        System.out.println("Antivirus database is already up to date.");
        return;
    }

    // A newer version exists, but the real update source
    // has not yet been connected to this application.
    throw new UnsupportedOperationException(
        "Database update source not configured yet"
    );
}

}
