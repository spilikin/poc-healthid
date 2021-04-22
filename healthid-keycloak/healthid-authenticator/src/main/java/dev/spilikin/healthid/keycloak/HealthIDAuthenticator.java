package dev.spilikin.healthid.keycloak;

import com.webauthn4j.data.client.challenge.DefaultChallenge;
import org.jboss.logging.Logger;
import org.keycloak.WebAuthnConstants;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.common.util.Base64Url;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.KeycloakModelUtils;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;

/**
 * Custom authenticator for Keycloak to explore the customizing possibilities.
 *
 * WARNING: This Authenticator is intended solely for demonstration of app based authentication.
 * Do not try this at home or at all.
 */
public class HealthIDAuthenticator implements Authenticator {
    private static final Logger LOGGER = Logger.getLogger(HealthIDAuthenticator.class);

    HealthIDAuthenticator(KeycloakSession session) {

    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        LoginFormsProvider form = context.form();

        String challengeValue = generateChallenge();

        context.getAuthenticationSession().setAuthNote(WebAuthnConstants.AUTH_CHALLENGE_NOTE, challengeValue);
        form.setAttribute(WebAuthnConstants.CHALLENGE, challengeValue);

        Response response = form.createForm("healthid-challenge.ftl");
        context.challenge(response);
        
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        String username = params.getFirst("username");
        UserModel user = context.getSession().users().getUserByUsername(username, context.getRealm());
        if (username == null) {
            LoginFormsProvider form =  context.form();
            form.setError("InvalidCredentials");
            form.setAttribute(WebAuthnConstants.CHALLENGE, generateChallenge());
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, form.createForm("healthid-challenge.ftl"));
            return;
        }

        context.setUser(user);
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }

    private String generateChallenge() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        return Base64Url.encode(bytes);
    }
}
