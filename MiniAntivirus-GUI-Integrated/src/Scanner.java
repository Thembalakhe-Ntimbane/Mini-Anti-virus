
import java.io.File;

public class Scanner {

    // Path of the directory or file to scan
    private String scanPath;

    private boolean scanning;

    public Scanner(String scanPath) {
        if (scanPath == null || scanPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Scan path must  not be empty"
            );
        }

        this.scanPath = scanPath.trim();
        this.scanning = false;
    }


    /**
     * Scans a folder for files with suspicious extensions.
     *
     * @return a report containing the number of flagged files
     */
    public Report startScan() {

        // Get the file or folder selected for scanning
        File target = new File(scanPath);

        // Check whether the selected path exists
        if (!target.exists()) {
            throw new IllegalArgumentException(
                "The specified scan path does not exist"
            );
        }

        // Prevent another scan from starting simultaneously
        if (scanning) {
            throw new IllegalStateException(
                    "A scan is already running"
            );
        }

        scanning = true;

        try {
            // Create a report for the scan results
            Report report = new Report();

            // Start with zero suspicious files
            int threatCount = 0;

            // Get files from the selected location
            File[] files;

            if (target.isDirectory()) {
                files = target.listFiles();
            } else {
                files = new File[] {target};
            }

            // Stop if the folder cannot be read
            if (files == null) {
                throw new IllegalStateException(
                    "Cannot read the selected folder"
                );
            }

            // Check each regular file
            for (File file : files) {

                if (file.isFile()) {

                    // Convert filename to lowercase
                    String name = file.getName().toLowerCase();

                    // Flag files ending with .exe
                    if (name.endsWith(".exe")) {
                        System.out.println(
                            "Suspicious file found: " + file.getName()
                        );

                        threatCount++;
                    }
                }
            }

            // Store the number of flagged files
            report.threats = threatCount;

            // Generate the report date
            report.generate();

            System.out.println("Scan completed.");
            System.out.println("Files flagged: " + threatCount);

            return report;

        } finally {

            // Reset the scanning status
            scanning = false;
        }
    }

    // Stops the scan
    public void stopScan() {
        if (scanning) {
            scanning = false;
            System.out.println("Scan stopped.");
        } else {
            System.out.println("No scan is currently running.");
        }
    }

    // Returns the path being scanned
    public String getScanPath() {
        return scanPath;
    }

    // Updates the scan path
    public void setScanPath(String scanPath) {
        if (scanPath == null || scanPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Scan path cannot be empty"
            );
        }

        this.scanPath = scanPath.trim();
    }
}
