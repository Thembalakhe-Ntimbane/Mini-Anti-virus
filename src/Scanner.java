
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

    // Starts the antivirus scan and returns a Report
    public Report startScan() {
        File target = new File(scanPath);

        if (!target.exists()) {
            throw new IllegalArgumentException(
                    "The specified scan path does not exist"
            );
        }

        if (scanning) {
            throw new IllegalStateException(
                    "A scan is already running"
            );
        }

        scanning = true;
        System.out.println("Starting scan: " + scanPath);

        try {

            System.out.println("Scan completed.");
            return null; // Replace when Report integration is ready.

        } finally {
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
