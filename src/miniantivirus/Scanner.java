
package miniantivirus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Scanner {

    private String scanPath;
    private volatile boolean scanning;
    private volatile boolean stopRequested;

    private final List<String> suspiciousFiles = new ArrayList<>();

    public Scanner(String scanPath) {
        setScanPath(scanPath);
        this.scanning = false;
        this.stopRequested = false;
    }

    public Report startScan() {
        File target = new File(scanPath);

        if (!target.exists()) {
            throw new IllegalArgumentException(
                    "The specified scan path does not exist."
            );
        }

        if (scanning) {
            throw new IllegalStateException(
                    "A scan is already running."
            );
        }

        scanning = true;
        stopRequested = false;
        suspiciousFiles.clear();

        try {
            scanFile(target);

            Report report = new Report();
            report.threats = suspiciousFiles.size();
            report.generate();

            return report;

        } finally {
            scanning = false;
        }
    }

    private void scanFile(File file) {
        if (stopRequested) {
            return;
        }

        if (file.isDirectory()) {
            File[] children = file.listFiles();

            if (children == null) {
                return;
            }

            for (File child : children) {
                if (stopRequested) {
                    break;
                }

                scanFile(child);
            }

        } else if (file.isFile()) {
            String name = file.getName().toLowerCase(Locale.ROOT);

            if (name.endsWith(".exe")) {
                suspiciousFiles.add(file.getAbsolutePath());
            }
        }
    }

    public void stopScan() {
        if (scanning) {
            stopRequested = true;
        }
    }

    public boolean isScanning() {
        return scanning;
    }

    public boolean isStopRequested() {
        return stopRequested;
    }

    public List<String> getSuspiciousFiles() {
        return new ArrayList<>(suspiciousFiles);
    }

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        if (scanPath == null || scanPath.isBlank()) {
            throw new IllegalArgumentException(
                    "Scan path cannot be empty."
            );
        }

        this.scanPath = scanPath.trim();
    }
}
