package dev.spilikin.healthid.keycloak;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Simple structure to store the pending authentication request challenges
 */
public class AuthnChallengeInfo implements Serializable {
    private final long timestamp;
    private final String challenge;
    private final String userCode;
    private final String deviceCode;
    private boolean authenticated = false;
    private String username;

    public AuthnChallengeInfo(@NotNull  String challenge, @NotNull String userCode, String deviceCode) {
        this.challenge = challenge;
        this.userCode = userCode;
        this.deviceCode = deviceCode;
        timestamp = System.currentTimeMillis();
    }

    public void changeToAuthenticated(String username) {
        this.username = username;
        this.authenticated = true;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getUserCode() {
        return userCode;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUsername() {
        return username;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthnChallengeInfo that = (AuthnChallengeInfo) o;
        return challenge.equals(that.challenge) && userCode.equals(that.userCode);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(challenge, userCode);
    }
}
