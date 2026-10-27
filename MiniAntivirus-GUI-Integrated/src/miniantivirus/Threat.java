
package miniantivirus;

public class Threat {

    private String type;
    private String severity;
    private String signature;

    public Threat(String type, String severity, String signature) {
        this.type = type;
        this.severity = severity;
        this.signature = signature;
    }

    public String getType() {
        return type;
    }

    public String getSeverity() {
        return severity;
    }

    public String getSignature() {
        return signature;
    }
}