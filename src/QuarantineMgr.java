
/**
 * Manages files identified as suspicious by the antivirus system.
 *
 * This class is responsible for moving suspicious files into
 * quarantine and managing their restoration or deletion.
 *
 * It works with QuarantinedFile to keep information about
 * files that have been quarantined.
 */
public class QuarantineMgr {

    /**
     * Stores the original location of each quarantined file.
     *
     * Key: The file's location inside the quarantine folder.
     * Value: The file's original location before quarantine.
     *
     * This temporary record is stored in memory.
     */
    private final java.util.Map<java.nio.file.Path, java.nio.file.Path>
            originalFilePaths = new java.util.HashMap<>();


    /**
     * Moves a suspicious file into the quarantine directory.
     *
     * @param fileName Path of the suspicious file.
     * @return The quarantined file, or null if quarantine fails.
     */
    public java.io.File quarantine(String fileName) {

        // Reject missing or empty file paths.
        if (fileName == null || fileName.isBlank()) {
            System.out.println("Error: No file specified.");
            return null;
        }

        java.io.File suspiciousFile = new java.io.File(fileName);

        // Record the original location before moving the file.
        // This will be needed to restore the file safely.
        java.nio.file.Path originalPath =
                suspiciousFile.toPath().toAbsolutePath().normalize();


        // Check that the source is an existing regular file.
        if (!suspiciousFile.isFile()) {
            System.out.println("Error: File does not exist.");
            return null;
        }

        // Create the quarantine folder if necessary.
        java.io.File quarantineFolder = new java.io.File("quarantine");

        if (!quarantineFolder.exists() && !quarantineFolder.mkdir()) {
            System.out.println("Error: Could not create quarantine folder.");
            return null;
        }

        if (!quarantineFolder.isDirectory()) {
            System.out.println("Error: Quarantine location is not a folder.");
            return null;
        }

        // Prepare the new file location.
        java.io.File destinationFile =
                new java.io.File(quarantineFolder, suspiciousFile.getName());

        // Never overwrite an existing quarantined file.
        if (destinationFile.exists()) {
            System.out.println("Error: File already exists in quarantine.");
            return null;
        }

        try {
            // Move the suspicious file into quarantine.
            java.nio.file.Files.move(
                    suspiciousFile.toPath(),
                    destinationFile.toPath()
            );
// TODO: Persist the originalPath and destinationFile mapping.
// Restoration must use this record, not guess the original location.

            // Remember where the quarantined file came from.
            // This information will be needed during restoration.
            originalFilePaths.put(
                    destinationFile.toPath().toAbsolutePath().normalize(),
                    originalPath
            );

            System.out.println("File moved to quarantine successfully.");
            return destinationFile;

        } catch (java.io.IOException e) {
            System.out.println("Error moving file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Restores a quarantined file to its original location.
     *
     * @param file The quarantined file to restore.
     */
    public void restore(java.io.File file) {
        // TODO: Check the quarantine record before restoring.

       
        // Reject a missing file reference.
        if (file == null) {
            System.out.println("Error: No file specified for restoration.");
            return;
        }

        // Convert the file location into an absolute, normalized path.
        java.nio.file.Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Find the original location saved during quarantine.
        java.nio.file.Path originalPath =
                originalFilePaths.get(quarantinePath);

        // Reject files without a matching quarantine record.
        if (originalPath == null) {
            System.out.println("Error: No quarantine record found for this file.");
            return;
        }


        // TODO: Prevent overwriting an existing destination file.

        // Confirm that the quarantined file still exists.
        if (!java.nio.file.Files.isRegularFile(quarantinePath)) {
            System.out.println("Error: Quarantined file does not exist.");
            return;
        }

        // Never overwrite a file that already exists
        // at the original location.
        if (java.nio.file.Files.exists(originalPath)) {
            System.out.println(
                    "Error: A file already exists at the original location."
            );
            return;
        }

    
        // Attempt to restore the quarantined file.
        try {
            // Move the file back without overwriting another file.
            java.nio.file.Files.move(
                    quarantinePath,
                    originalPath
            );

            // Remove the record only after restoration succeeds.
            originalFilePaths.remove(quarantinePath);

            System.out.println("File restored successfully.");
            System.out.println("Restored to: " + originalPath);

        } catch (java.io.IOException e) {
            // Keep the record if restoration fails.
            System.out.println(
                    "Error restoring file: " + e.getMessage()
            );
        }

    }

    /**
     * Deletes a file that has been placed in quarantine.
     *
     * @param file The quarantined file to delete.
     */
    public void delete(java.io.File file) {

        // TODO: Verify that the file belongs to quarantine.
      
        // Reject a missing file reference.
        if (file == null) {
            System.out.println("Error: No file specified for deletion.");
            return;
        }
  
        // TODO: Delete only the intended quarantined file.

        // Get the absolute path of the file selected for deletion.
        java.nio.file.Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Check whether this file has a registered quarantine record.
        if (!originalFilePaths.containsKey(quarantinePath)) {
            System.out.println(
                    "Error: File is not registered in quarantine."
            );
            return;
        }


        // Find the absolute location of the quarantine directory.
        java.nio.file.Path quarantineDirectory =
                java.nio.file.Path.of("quarantine")
                        .toAbsolutePath()
                        .normalize();

        // Reject files that are not directly inside quarantine.
        if (!quarantineDirectory.equals(quarantinePath.getParent())) {
            System.out.println(
                    "Error: File is outside the quarantine directory."
            );
            return;
        }

       
       
       

        // Verify that the target is a regular file.
        // Do not follow symbolic links during this check.
        if (!java.nio.file.Files.isRegularFile(
                quarantinePath,
                java.nio.file.LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Target is not a regular quarantined file."
            );
            return;
        }

        // TODO: Remove its quarantine record after deletion.


     
        // Delete the registered quarantined file.
        try {
            java.nio.file.Files.delete(quarantinePath);

            // Remove its record only after successful deletion.
            originalFilePaths.remove(quarantinePath);

            System.out.println("Quarantined file deleted successfully.");

        } catch (java.io.IOException e) {
            // Keep the record if deletion fails.
            System.out.println(
                    "Error deleting quarantined file: " + e.getMessage()
            );
        }

    }

}
