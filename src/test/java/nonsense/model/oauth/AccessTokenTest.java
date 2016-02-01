package nonsense.model.oauth;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AccessTokenTest {
    @Test
    public void createFake() {
        final AccessToken token = AccessToken.createFake();
        assertThat(token.accountId, is(notNullValue()));
        assertThat(token.accessToken, is(notNullValue()));
        assertThat(token.expiresIn, is(not(0L)));
        assertThat(token.refreshToken, is(notNullValue()));
        assertThat(token.tokenType, is(equalTo(AccessToken.TYPE_TOKEN)));
    }
}
