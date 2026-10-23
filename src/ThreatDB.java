
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

    public void update() {
        lastUpdated = new Date();
        System.out.println("Threat database updated.");
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}
