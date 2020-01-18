package lab4;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @Column(name="login")
    private String login;
    @Column(name="password")
    private String passwordHash;
    @Column(name="token")
    private String authToken;

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getAuthToken() {
        return authToken;
    }

    public UserEntity(String login, String passwordHash, String authToken) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.authToken = authToken;
    }

    public UserEntity() {
    }
}