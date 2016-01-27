package nonsense.model.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AccessToken {
    @JsonProperty("access_token")
    public final String accessToken;

    @JsonProperty("expires_in")
    public final long expiresIn;

    @JsonProperty("refresh_token")
    public final String refreshToken;

    @JsonProperty("token_type")
    public final String tokenType;

    @JsonProperty("account_id")
    public final String accountId;


    public AccessToken(@JsonProperty("access_token") String accessToken,
                       @JsonProperty("expires_in") long expiresIn,
                       @JsonProperty("refresh_token") String refreshToken,
                       @JsonProperty("token_type") String tokenType,
                       @JsonProperty("account_id") String accountId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.accountId = accountId;
    }

    public static AccessToken generate() {
        return new AccessToken(UUID.randomUUID().toString(),
                               Long.MAX_VALUE,
                               UUID.randomUUID().toString(),
                               "token",
                               "not-a-real-account");
    }


    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}
