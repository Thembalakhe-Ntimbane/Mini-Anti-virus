

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages suspicious files detected by the antivirus system.
 *
 * Supports quarantining, restoring and deleting files.
 * Original file locations are saved so they can be
 * recovered after restarting the application.
 */
public class QuarantineMgr {

    /**
     * Stores the original location of each quarantined file.
     *
     * Key: Absolute path of the quarantined file.
     * Value: Absolute path of the original file.
     */
    private final Map<Path, Path> originalFilePaths =
            new HashMap<>();

    // Directory containing quarantined files.
    private final Path quarantineDirectory =
            Path.of("quarantine").toAbsolutePath().normalize();

    // File containing persistent quarantine records.
    private final Path recordsFile =
            quarantineDirectory.resolve("records.txt");

    // Prevent operations if record loading or saving fails.
    private boolean recordsAvailable = true;

    /**
     * Creates a quarantine manager.
     *
     * Previously saved records are loaded automatically.
     */
    public QuarantineMgr() {

        recordsAvailable = loadRecords();
    }

    /**
     * Moves a suspicious file into quarantine.
     *
     * @param fileName Path of the suspicious file.
     * @return The quarantined file, or null on failure.
     */
    public File quarantine(String fileName) {

        if (!recordsAvailable) {
            System.out.println(
                    "Error: Quarantine records are unavailable."
            );
            return null;
        }

        // Reject missing or empty filenames.
        if (fileName == null || fileName.isBlank()) {
            System.out.println("Error: No file specified.");
            return null;
        }

        // Remember the original file location.
        Path originalPath =
                Path.of(fileName).toAbsolutePath().normalize();

        // Reject missing files, directories and symbolic links.
        if (!Files.isRegularFile(
                originalPath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Source is not a regular file."
            );
            return null;
        }

        // Do not quarantine a file already inside quarantine.
        if (originalPath.startsWith(quarantineDirectory)) {
            System.out.println(
                    "Error: File is already inside quarantine."
            );
            return null;
        }

        try {
            // Create the quarantine folder if necessary.
            Files.createDirectories(quarantineDirectory);

            // Do not accept a symbolic-link quarantine folder.
            if (Files.isSymbolicLink(quarantineDirectory)) {
                System.out.println(
                        "Error: Invalid quarantine directory."
                );
                return null;
            }

            // Prepare the quarantine destination.
            Path quarantinePath =
                    quarantineDirectory.resolve(
                            originalPath.getFileName()
                    ).normalize();

            // Never replace an existing quarantined file.
            if (Files.exists(
                    quarantinePath,
                    LinkOption.NOFOLLOW_LINKS)) {

                System.out.println(
                        "Error: File already exists in quarantine."
                );
                return null;
            }

            // Move the file into quarantine.
            Files.move(originalPath, quarantinePath);

            // Store the original path in memory.
            originalFilePaths.put(
                    quarantinePath,
                    originalPath
            );

            // Save the record permanently.
            if (!saveRecords()) {

                System.out.println(
                        "Error: Could not save quarantine record."
                );

                // Attempt to reverse the move safely.
                try {
                    Files.move(
                            quarantinePath,
                            originalPath
                    );

                    originalFilePaths.remove(quarantinePath);

                    System.out.println(
                            "File returned to its original location."
                    );

                } catch (IOException rollbackError) {

                    // Keep the in-memory record if rollback fails.
                    System.out.println(
                            "WARNING: File remains in quarantine."
                    );

                    System.out.println(
                            "Original location: " + originalPath
                    );

                    System.out.println(
                            "Rollback error: "
                                    + rollbackError.getMessage()
                    );
                }

                // Stop further changes until records are checked.
                recordsAvailable = false;
                return null;
            }

            System.out.println(
                    "File moved to quarantine successfully."
            );

            return quarantinePath.toFile();

        } catch (IOException e) {

            System.out.println(
                    "Error moving file: " + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Restores a quarantined file to its original location.
     *
     * The saved record is removed after successful restoration.
     *
     * @param file The quarantined file to restore.
     */
    public void restore(File file) {

        if (!recordsAvailable) {
            System.out.println(
                    "Error: Quarantine records are unavailable."
            );
            return;
        }

        if (file == null) {
            System.out.println(
                    "Error: No file specified for restoration."
            );
            return;
        }

        // Find the absolute path of the quarantined file.
        Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Find its recorded original location.
        Path originalPath =
                originalFilePaths.get(quarantinePath);

        if (originalPath == null) {
            System.out.println(
                    "Error: No quarantine record found for this file."
            );
            return;
        }

        // Only allow files directly inside quarantine.
        if (!quarantineDirectory.equals(
                quarantinePath.getParent())) {

            System.out.println(
                    "Error: File is outside the quarantine directory."
            );
            return;
        }

        // Reject missing files and symbolic links.
        if (!Files.isRegularFile(
                quarantinePath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Quarantined file does not exist."
            );
            return;
        }

        // Never overwrite the original location.
        if (Files.exists(
                originalPath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: A file already exists at the original location."
            );
            return;
        }

        try {
            // Move the file back to its original location.
            Files.move(quarantinePath, originalPath);

            // Remove the record from memory.
            originalFilePaths.remove(quarantinePath);

            System.out.println("File restored successfully.");
            System.out.println("Restored to: " + originalPath);

            // NEW: Synchronize the saved records.
            if (saveRecords()) {

                System.out.println(
                        "Restoration record removed successfully."
                );

            } else {

                // The file was restored, but records.txt is outdated.
                recordsAvailable = false;

                System.out.println(
                        "WARNING: File restored, but records.txt "
                                + "was not updated."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error restoring file: " + e.getMessage()
            );
        }
    }

    /**
     * Deletes a registered quarantined file.
     *
     * The saved record is removed after successful deletion.
     *
     * @param file The quarantined file to delete.
     */
    public void delete(File file) {

        if (!recordsAvailable) {
            System.out.println(
                    "Error: Quarantine records are unavailable."
            );
            return;
        }

        if (file == null) {
            System.out.println(
                    "Error: No file specified for deletion."
            );
            return;
        }

        // Determine the target file path.
        Path quarantinePath =
                file.toPath().toAbsolutePath().normalize();

        // Only accept registered quarantine files.
        if (!originalFilePaths.containsKey(quarantinePath)) {
            System.out.println(
                    "Error: File is not registered in quarantine."
            );
            return;
        }

        // Ensure the file belongs directly to quarantine.
        if (!quarantineDirectory.equals(
                quarantinePath.getParent())) {

            System.out.println(
                    "Error: File is outside the quarantine directory."
            );
            return;
        }

        // Reject directories, missing files and symbolic links.
        if (!Files.isRegularFile(
                quarantinePath,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Target is not a regular quarantined file."
            );
            return;
        }

        try {
            // Delete the registered quarantined file.
            Files.delete(quarantinePath);

            // Remove its record from memory.
            originalFilePaths.remove(quarantinePath);

            System.out.println(
                    "Quarantined file deleted successfully."
            );

            // NEW: Synchronize the saved records.
            if (saveRecords()) {

                System.out.println(
                        "Deletion record removed successfully."
                );

            } else {

                recordsAvailable = false;

                System.out.println(
                        "WARNING: File deleted, but records.txt "
                                + "was not updated."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Error deleting quarantined file: " + e.getMessage()
            );
        }
    }

    /**
     * Saves active quarantine records to records.txt.
     *
     * File paths are Base64-encoded to preserve characters.
     * A temporary file is written before replacing records.txt.
     *
     * @return true if saving succeeded.
     */
    private boolean saveRecords() {

        // Prepare the temporary records file.
        Path temporaryRecordsFile =
                recordsFile.resolveSibling("records.tmp");

        List<String> records = new ArrayList<>();

        // Encode all currently active quarantine records.
        for (Map.Entry<Path, Path> entry
                : originalFilePaths.entrySet()) {

            String quarantineLocation =
                    Base64.getEncoder().encodeToString(
                            entry.getKey().toString()
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            String originalLocation =
                    Base64.getEncoder().encodeToString(
                            entry.getValue().toString()
                                    .getBytes(StandardCharsets.UTF_8)
                    );

            // Store both paths separated by a tab.
            records.add(
                    quarantineLocation + "\t" + originalLocation
            );
        }

        try {
            // Ensure the quarantine folder exists.
            Files.createDirectories(quarantineDirectory);

            // Write the updated records to the temporary file.
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

            System.out.println(
                    "Quarantine records saved successfully."
            );

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error saving quarantine records: "
                            + e.getMessage()
            );

            return false;
        }
    }

    /**
     * Loads saved quarantine records from records.txt.
     *
     * Valid records are loaded into memory.
     * Stale records referring to missing quarantine files
     * are skipped.
     *
     * The records file is not rewritten during loading.
     *
     * @return true if loading completed successfully.
     */
    private boolean loadRecords() {

        // A new installation may not have saved records yet.
        if (!Files.exists(
                recordsFile,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "No previous quarantine records found."
            );

            return true;
        }

        // Reject invalid or symbolic-link records files.
        if (!Files.isRegularFile(
                recordsFile,
                LinkOption.NOFOLLOW_LINKS)) {

            System.out.println(
                    "Error: Invalid quarantine records file."
            );

            return false;
        }

        // Load records into a temporary map first.
        Map<Path, Path> loadedRecords = new HashMap<>();

        int staleRecords = 0;

        try {
            // Read all saved records.
            List<String> lines = Files.readAllLines(
                    recordsFile,
                    StandardCharsets.UTF_8
            );

            for (String line : lines) {

                // Each record must contain exactly two fields.
                String[] parts = line.split("\t", -1);

                if (parts.length != 2) {
                    throw new IllegalArgumentException(
                            "Invalid quarantine record format."
                    );
                }

                // Decode the quarantined file path.
                String quarantineLocation = new String(
                        Base64.getDecoder().decode(parts[0]),
                        StandardCharsets.UTF_8
                );

                // Decode the original file path.
                String originalLocation = new String(
                        Base64.getDecoder().decode(parts[1]),
                        StandardCharsets.UTF_8
                );

                // Convert the decoded strings to absolute paths.
                Path quarantinePath =
                        Path.of(quarantineLocation)
                                .toAbsolutePath()
                                .normalize();

                Path originalPath =
                        Path.of(originalLocation)
                                .toAbsolutePath()
                                .normalize();

                // A quarantined file must be directly inside quarantine.
                if (!quarantineDirectory.equals(
                        quarantinePath.getParent())) {

                    throw new IllegalArgumentException(
                            "Record points outside quarantine."
                    );
                }

                // Original and quarantine paths must differ.
                if (quarantinePath.equals(originalPath)) {
                    throw new IllegalArgumentException(
                            "Invalid original file location."
                    );
                }

                // Reject original paths pointing into quarantine.
                if (originalPath.startsWith(quarantineDirectory)) {
                    throw new IllegalArgumentException(
                            "Invalid original file location."
                    );
                }

                // Reject duplicate quarantine records.
                if (loadedRecords.containsKey(quarantinePath)) {
                    throw new IllegalArgumentException(
                            "Duplicate quarantine record."
                    );
                }

                // Skip records for files no longer in quarantine.
                if (!Files.isRegularFile(
                        quarantinePath,
                        LinkOption.NOFOLLOW_LINKS)) {

                    staleRecords++;

                    System.out.println(
                            "Warning: Stale quarantine record skipped: "
                                    + quarantinePath.getFileName()
                    );

                    continue;
                }

                // Add this validated record to the temporary map.
                loadedRecords.put(
                        quarantinePath,
                        originalPath
                );
            }

            // Accept the records after all entries are processed.
            originalFilePaths.clear();
            originalFilePaths.putAll(loadedRecords);

            System.out.println(
                    "Quarantine records loaded successfully."
            );

            System.out.println(
                    "Records loaded: " + originalFilePaths.size()
            );

            System.out.println(
                    "Stale records skipped: " + staleRecords
            );

            return true;

        } catch (IOException | IllegalArgumentException e) {

            System.out.println(
                    "Error loading quarantine records: "
                            + e.getMessage()
            );

            return false;
        }
    }
}
