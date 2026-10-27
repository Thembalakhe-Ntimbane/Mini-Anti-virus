package miniantivirus;

import java.util.Date;
import java.util.HashMap;

public class ThreatDB {

    private Date lastUpdated;
    private HashMap<String,Threat> threats;

    public ThreatDB() {
        threats = new HashMap<>();
        lastUpdated = new Date();

        // Example threats
        threats.put("HASH001",
                new Threat("Virus", "High", "HASH001"));

        threats.put("HASH002",
                new Threat("Trojan", "Medium", "HASH002"));
    }

    public Threat find(String hash) {
        return threats.get(hash);
    }

    /**
 * Adds a new threat definition to the database.
 *
 * @param threat The threat definition to store.
 */
public void addThreat(Threat threat) {

    // Prevent null threats from being added.
    if (threat == null) {
        throw new IllegalArgumentException("Threat cannot be null");
    }

    // Get the signature used to identify the threat.
    String signature = threat.getSignature();

    // Reject threats without a valid signature.
    if (signature == null || signature.isBlank()) {
        throw new IllegalArgumentException("Threat signature cannot be empty");
    }

    // Store the threat using its signature as the key.
    threats.put(signature, threat);

    // Record the time of the database modification.
    lastUpdated = new Date();
}
    public void update() {
        lastUpdated = new Date();
        System.out.println("Threat database updated.");
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}