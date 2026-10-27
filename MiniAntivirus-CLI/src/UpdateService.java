
/**
 * Handles update-related operations for the Mini Antivirus application.
 */
public class UpdateService {

    // Stores the currently installed database version.
    private int currentVersion;

    // Stores the latest available database version.
    private int latestVersion;

    // Stores the threat database used by this update service.
    private ThreatDB database;


    /**
     * Creates an update service with its own threat database.
     *
     * @param currentVersion The installed database version.
     * @param latestVersion The latest available database version.
     */
    public UpdateService(int currentVersion, int latestVersion) {

        // Reject negative version numbers.
        if (currentVersion < 0 || latestVersion < 0) {
            throw new IllegalArgumentException(
                "Database versions cannot be negative"
            );
        }

        // Store the database versions.
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;

        // Create a database for local demonstration updates.
        this.database = new ThreatDB();
    }


    /**
     * Creates an update service using an existing shared database.
     *
     * @param currentVersion The installed database version.
     * @param latestVersion The latest available database version.
     * @param database The shared threat database.
     */
    public UpdateService(
            int currentVersion,
            int latestVersion,
            ThreatDB database) {

        // Reuse the existing constructor for version validation.
        this(currentVersion, latestVersion);

        // Reject a missing database.
        if (database == null) {
            throw new IllegalArgumentException(
                "Threat database cannot be null"
            );
        }

        // Use the supplied shared database.
        this.database = database;
    }


    /**
     * Returns the currently installed database version.
     *
     * @return The installed database version.
     */
    public int getCurrentVersion() {
        return currentVersion;
    }


    /**
     * Returns the latest available database version.
     *
     * @return The latest database version.
     */
    public int getLatestVersion() {
        return latestVersion;
    }


    /**
     * Checks whether a newer database version is available.
     *
     * @return true if an update is available; otherwise false.
     */
    public boolean check() {

        // Compare the available version with the installed version.
        return latestVersion > currentVersion;
    }


    /**
     * Installs a local demonstration threat database update.
     *
     * This method does not download or authenticate real
     * antivirus threat definitions.
     */
    public void updateDB() {

        // Stop if the database is already up to date.
        if (!check()) {
            System.out.println(
                "Antivirus database is already up to date."
            );
            return;
        }

        // Create a harmless demonstration threat definition.
        Threat newThreat = new Threat(
            "Virus",
            "High",
            "HASH003"
        );

        // Add the demonstration threat to the database.
        database.addThreat(newThreat);

        // Check whether the definition was stored successfully.
        Threat installedThreat = database.find("HASH003");

        if (installedThreat == null) {
            throw new IllegalStateException(
                "Database update failed: threat definition not found"
            );
        }

        // Change the installed version only after the update succeeds.
        currentVersion = latestVersion;

        System.out.println("Demo database updated successfully.");
        System.out.println(
            "Installed database version: " + currentVersion
        );
    }
}
