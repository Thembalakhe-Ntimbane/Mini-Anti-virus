

import java.util.Date;

/**
 * Represents a file that has been placed in quarantine.
 *
 * Stores the file's path and the date it was quarantined.
 */
public class QuarantinedFile {

    // Path identifying the quarantined file.
    public String path;

    // Date when the file was quarantined.
    public Date date;

    /**
     * Restores this quarantined file to its original location.
     *
     * The restoration logic will be implemented when
     * QuarantineMgr is connected to this class.
     */
    public void restore() {
        // TODO: Implement safe file restoration.
        System.out.println("File restoration is not yet implemented.");
    }



}


