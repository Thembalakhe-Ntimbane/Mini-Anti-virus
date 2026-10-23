


import java.io.File;
import java.util.Date;

/**
 * Represents a file that has been placed in quarantine.
 *
 * Stores the file's location and quarantine date.
 * Restoration is handled by QuarantineMgr.
 */
public class QuarantinedFile {

    // Location of the quarantined file.
    public String path;

    // Date when the file was quarantined.
    public Date date;

    // Manager responsible for restoring this file.
    private final QuarantineMgr quarantineManager;

    /**
     * Creates a quarantined file record.
     *
     * @param path Path of the quarantined file.
     * @param date Date when the file was quarantined.
     * @param quarantineManager Manager handling restoration.
     */
    public QuarantinedFile(
            String path,
            Date date,
            QuarantineMgr quarantineManager) {

        this.path = path;
        this.date = new Date(date.getTime());
        this.quarantineManager = quarantineManager;
    }

    /**
     * Requests restoration through QuarantineMgr.
     */
    public void restore() {

        // Reject missing file locations.
        if (path == null || path.isBlank()) {
            System.out.println("Error: No quarantined file path specified.");
            return;
        }

        // Delegate restoration to the manager.
        quarantineManager.restore(new File(path));
    }
}
