package dev.spilikin.healthid.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

/**
 * Custom authenticator for Keycloak to explore the customizing possibilities.
 *
 * WARNING: This Authenticator is intended solely for demonstration of app based authentication.
 * Do not try this at home or at all.
 */
public class HealthIDAuthenticator implements Authenticator {
    private static final AuthnChallengeProvider challenges = new AuthnChallengeProvider();
    private static final Logger LOGGER = Logger.getLogger(HealthIDAuthenticator.class);

    private static final String ATTR_CHALLENGE = "challenge";
    private static final String ATTR_USER_CODE = "userCode";
    private static final String ATTR_IS_AUTHENTICATED = "isAuthenticated";
    private static final String AUTH_CHALLENGE_NOTE = "HEALTHID_CHALLENGE";
    private static final String PARAM_SIGNATURE = "signature";
    private static final String PARAM_COMMAND = "command";
    private static final String COMMAND_POLL = "poll";
    private static final String COMMAND_FINISH = "finish";


    HealthIDAuthenticator(KeycloakSession session) {

    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        AuthnChallengeInfo info = challenges.next();
        context.challenge(createCchallengeForm(context, info));
    }

    private Response createCchallengeForm(AuthenticationFlowContext context, AuthnChallengeInfo info) {
        challenges.expire();
        challenges.addAuthnChallengeInfo(info);

        context.getAuthenticationSession().setAuthNote(AUTH_CHALLENGE_NOTE, info.getChallenge());

        LoginFormsProvider form = context.form();
        form.setAttribute(ATTR_CHALLENGE, info.getChallenge());
        form.setAttribute(ATTR_USER_CODE, info.getUserCode());

       return form.createForm("healthid-challenge.ftl");
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();

        LOGGER.info(params);

        if (params.getFirst(PARAM_SIGNATURE) != null) {
            // see if username parameter ist provided
            String username = params.getFirst("username");
            UserModel user = context.getSession().users().getUserByUsername(username, context.getRealm());
            if (username == null) {
                context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, createCchallengeForm(context, challenges.next()));
                return;
            }

            // for simplicity reasons and since it's only a demo the "data to be signed" is equal to "signed data"
            String signature = params.getFirst(PARAM_SIGNATURE);
            AuthnChallengeInfo info = challenges.findByChallengeOrUserCode(signature);

            if (info == null) {
                LOGGER.error(String.format("Invalid User Code: %s", signature));
                authenticate(context);
                return;
            }

            info.changeToAuthenticated(username);
            LOGGER.info(String.format("Got signature: %s", signature));
            LOGGER.info(String.format("Found pending challenge: %s", info));
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, createCchallengeForm(context, challenges.next()));
            return;
        } else if (COMMAND_POLL.equals(params.getFirst(PARAM_COMMAND))) {
            String challengeValue = context.getAuthenticationSession().getAuthNote(AUTH_CHALLENGE_NOTE);
            AuthnChallengeInfo info = challenges.findByChallengeOrUserCode(challengeValue);

            LoginFormsProvider form = context.form();
            form.setAttribute(ATTR_IS_AUTHENTICATED, info.isAuthenticated());
            context.challenge(form.createForm("healthid-poll-response.ftl"));

            return;
        } else if (COMMAND_FINISH.equals(params.getFirst(PARAM_COMMAND))) {
            String challengeValue = context.getAuthenticationSession().getAuthNote(AUTH_CHALLENGE_NOTE);
            AuthnChallengeInfo info = challenges.findByChallengeOrUserCode(challengeValue);
            if (info.isAuthenticated()) {
                LOGGER.info(String.format("User '%s' is authenticated", info.getUsername()));
                UserModel user = context.getSession().users().getUserByUsername(info.getUsername(), context.getRealm());
                challenges.remove(info);
                context.setUser(user);
                context.success();
            } else {
                authenticate(context);
            }
            return;
        }
        LOGGER.info("Retry challenge");

        authenticate(context);

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

}
