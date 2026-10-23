
// Security Pakages that i found online that  may work. This requires testing cause im not sure it might work.

public class ScanEngine {

    private ThreatDB database;

    public ScanEngine(ThreatDB database) {
        this.database = database;
    }

    // Finds a threat using its hash
    public Threat scan(String hash) {
        return database.find(hash);
    }

    // Checks whether a threat was detected
    public boolean analyze(String hash) {
        Threat threat = scan(hash);
        return threat != null;
    }
}
