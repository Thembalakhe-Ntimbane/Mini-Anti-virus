package miniantivirus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages suspicious files detected by the antivirus system.
 *
 * This class moves files into quarantine and allows them
 * to be restored or deleted.
 */
public class QuarantineMgr {

    /**
     * Stores the original location of quarantined files.
     *
     * Key: Path inside the quarantine folder.
     * Value: Original file path.
     *
     * Records are kept in memory and saved to a file.
     */
    private final Map<Path, Path> originalFilePaths = new HashMap<>();

    /**
     * File used to store the quarantine records.
     */
    private final Path recordsFile =
            Path.of("quarantine", "records.txt");

    /**
     * Moves a suspicious file into the quarantine folder.
     *
     * @param fileName Path of the suspicious file.
     * @return The quarantined file, or null if the operation fails.
     */
    public File quarantine(String fileName) {

        // Check that a filename was provided.
        if (fileName == null || fileName.isBlank()) {
            System.out.println("Error: No file specified.");
            return null;
        }

        File suspiciousFile = new File(fileName);

        // Remember the original file location.
        Path originalPath =
                suspiciousFile.toPath().toAbsolutePath().normalize();

        // Confirm that the source is an existing file.
        if (!suspiciousFile.isFile()) {
            System.out.println("Error: File does not exist.");
            return null;
        }

        // Create the quarantine folder if necessary.
        File quarantineFolder = new File("quarantine");

        if (!quarantineFolder.exists() && !quarantineFolder.mkdir()) {
            System.out.println("Error: Could not create quarantine folder.");
            return null;
        }

        if (!quarantineFolder.isDirectory()) {
            System.out.println("Error: Quarantine location is not a folder.");
            return null;
        }

        // Prepare the destination file.
        File destinationFile =
                new File(quarantineFolder, suspiciousFile.getName());

        // Do not replace an existing quarantined file.
        if (destinationFile.exists()) {
            System.out.println("Error: File already exists in quarantine.");
            return null;
        }

        Path quarantinePath =
                destinationFile.toPath().toAbsolutePath().normalize();

        try {
            // Move the suspicious file into quarantine.
            Files.move(
                    suspiciousFile.toPath(),
                    destinationFile.toPath()
            );

            // Record the original location in memory.
            originalFilePaths.put(quarantinePath, originalPath);

            // Save the record permanently before reporting success.
            if (!saveRecords()) {
                System.out.println(
                        "Error: Quarantine record could not be saved."
                );

                // Attempt to undo the quarantine operation.
                try {
                    Files.move(
                            destinationFile.toPath(),
                            originalPath
                    );

                    // Remove the record after successful rollback.
                    originalFilePaths.remove(quarantinePath);

                    System.out.println(
                            "File returned to its original location."
                    );

                } catch (IOException e) {
                    // Retain the in-memory record if rollback fails.
                    System.out.println(
                            "WARNING: File remains in quarantine."
                    );
                    System.out.println(
                            "Original location: " + originalPath
                    );
                    System.out.println(
                            "Rollback error: " + e.getMessage()
                    );
                }

                return null;
            }

            System.out.println("File moved to quarantine successfully.");
            return destinationFile;

        } catch (IOException e) {
            System.out.println("Error moving file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Restores a quarantined file to its original location.
     *
     * @param file The quarantined file to restore.
     */
    public void restore(File file) {

        // Reject a missing file reference.
        if (file == null) {
            System.out.println("Error: No file specified for restoration.");
            return;
        }

        // Identify the quarantined file location.
        Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Find the recorded original location.
        Path originalPath =
                originalFilePaths.get(quarantinePath);

        if (originalPath == null) {
            System.out.println(
                    "Error: No quarantine record found for this file."
            );
            return;
        }

        // Make sure the quarantined file exists.
        if (!Files.isRegularFile(
                quarantinePath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println("Error: Quarantined file does not exist.");
            return;
        }

        // Do not overwrite an existing destination file.
        if (Files.exists(originalPath)) {
            System.out.println(
                    "Error: A file already exists at the original location."
            );
            return;
        }

        try {
            // Move the file back to its original location.
            Files.move(
                    quarantinePath,
                    originalPath
            );

            // Remove its in-memory record after restoration.
            originalFilePaths.remove(quarantinePath);

            System.out.println("File restored successfully.");
            System.out.println("Restored to: " + originalPath);

        } catch (IOException e) {
            System.out.println(
                    "Error restoring file: " + e.getMessage()
            );
        }
    }

    /**
     * Deletes a registered quarantined file.
     *
     * @param file The quarantined file to delete.
     */
    public void delete(File file) {

        // Reject a missing file reference.
        if (file == null) {
            System.out.println("Error: No file specified for deletion.");
            return;
        }

        // Identify the quarantined file location.
        Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Check that the file has a quarantine record.
        if (!originalFilePaths.containsKey(quarantinePath)) {
            System.out.println(
                    "Error: File is not registered in quarantine."
            );
            return;
        }

        // Identify the quarantine directory.
        Path quarantineDirectory =
                Path.of("quarantine").toAbsolutePath().normalize();

        // Reject files outside the quarantine directory.
        if (!quarantineDirectory.equals(quarantinePath.getParent())) {
            System.out.println(
                    "Error: File is outside the quarantine directory."
            );
            return;
        }

        // Reject directories and symbolic links.
        if (!Files.isRegularFile(
                quarantinePath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Target is not a regular quarantined file."
            );
            return;
        }

        try {
            // Delete the registered file from quarantine.
            Files.delete(quarantinePath);

            // Remove its in-memory record after deletion.
            originalFilePaths.remove(quarantinePath);

            System.out.println(
                    "Quarantined file deleted successfully."
            );

        } catch (IOException e) {
            System.out.println(
                    "Error deleting quarantined file: " + e.getMessage()
            );
        }
    }

    /**
     * Saves the current quarantine records to records.txt.
     *
     * The paths are Base64-encoded to preserve special characters.
     * The records are written to a temporary file first.
     *
     * @return true if saving succeeds, false otherwise.
     */
    private boolean saveRecords() {

        // Prepare a temporary file beside records.txt.
        Path temporaryRecordsFile =
                recordsFile.resolveSibling("records.tmp");

        // Prepare the text records.
        List<String> records = new ArrayList<>();

        for (Map.Entry<Path, Path> entry : originalFilePaths.entrySet()) {

            // Encode the quarantine location.
            String quarantineLocation = Base64.getEncoder()
                    .encodeToString(
                            entry.getKey().toString()
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            // Encode the original file location.
            String originalLocation = Base64.getEncoder()
                    .encodeToString(
                            entry.getValue().toString()
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            // Store both paths in one record.
            records.add(quarantineLocation + "\t" + originalLocation);
        }

        try {
            // Ensure the quarantine directory exists.
            Files.createDirectories(recordsFile.getParent());

            // Write the records to a temporary file.
            Files.write(
                    temporaryRecordsFile,
                    records,
                    StandardCharsets.UTF_8
            );

            // Replace records.txt only after writing succeeds.
            Files.move(
                    temporaryRecordsFile,
                    recordsFile,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("Quarantine records saved successfully.");
            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error saving quarantine records: " + e.getMessage()
            );

            return false;
        }
    }
    public List<File> getQuarantinedFiles() {
    List<File> files = new ArrayList<>();

    for (Path path : originalFilePaths.keySet()) {
        files.add(path.toFile());
    }

    return files;
}
}