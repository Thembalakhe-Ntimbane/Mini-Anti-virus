import java.time.LocalDateTime;
import java.util.UUID;

//User Class that is used to identify user
public class User<string> {

    public string version;

    private final String userId;
    private String username;
    private LocalDateTime loginTime;
    private boolean isActive;

    public User(String username) {
        this.userId = UUID.randomUUID().toString(); // Generate unique ID
        this.username = username;
        this.isActive = true;
        this.loginTime = LocalDateTime.now();
    }

// Getters and Setters to access private fields
public String getUserId() { return userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public LocalDateTime getLoginTime() { return loginTime; }
    public void updateLoginTime() { this.loginTime = LocalDateTime.now(); }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }




}
