package dev.spilikin.healthid.keycloak;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.MediaType;
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
    private static final String NOTE_CHALLENGE = "HEALTHID_CHALLENGE";
    private static final String PARAM_SIGNATURE = "signature";
    private static final String PARAM_COMMAND = "command";
    private static final String PARAM_DEVICE_CODE = "device_code";
    private static final String PARAM_USERNAME = "username";
    private static final String COMMAND_POLL = "poll";
    private static final String COMMAND_FINISH = "finish";
    private static final String COMMAND_CHALLENGE_RESPONSE = "challenge_response";
    private static final String COMMAND_DEMO = "demo";
    private static final String DEMO_USER_NAME = "demouser";


    HealthIDAuthenticator(KeycloakSession session) {

    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        if (context.getHttpRequest().getHttpHeaders().getAcceptableMediaTypes().contains(MediaType.APPLICATION_JSON_TYPE)) {
            respondChallengeWithJson(context);
        } else {
            respondChallengeWithQRCodePage(context);
        }

    }

    private void respondChallengeWithQRCodePage(AuthenticationFlowContext context) {
        LoginFormsProvider formProvider = newFormProvider(context);
        context.challenge(formProvider.createForm("healthid/qrcode-page.ftl"));
    }

    private void respondChallengeWithJson(AuthenticationFlowContext context) {
        LoginFormsProvider formProvider = newFormProvider(context);

        Response form = formProvider.createForm("healthid/challenge.json.ftl");

        context.challenge(Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(form.getEntity()).build());
    }

    private LoginFormsProvider newFormProvider(AuthenticationFlowContext context) {
        challenges.expire();
        AuthnChallengeInfo info = challenges.next();
        LoginFormsProvider formProvider = context.form();
        context.getAuthenticationSession().setAuthNote(NOTE_CHALLENGE, info.getChallenge());
        formProvider.setAttribute(ATTR_CHALLENGE, info);
        return formProvider;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();

        if (!COMMAND_POLL.equals(params.getFirst(PARAM_COMMAND))) {
            LOGGER.info(params);
        }

        if (COMMAND_POLL.equals(params.getFirst(PARAM_COMMAND))) {
            actionPoll(context);
        } else if (COMMAND_CHALLENGE_RESPONSE.equals(params.getFirst(PARAM_COMMAND))) {
            actionChallengeResponse(context);
        } else if (COMMAND_FINISH.equals(params.getFirst(PARAM_COMMAND))) {
            actionFinish(context);
        } else if (COMMAND_DEMO.equals(params.getFirst(PARAM_COMMAND))) {
            actionDemo(context);
        } else {
            context.challenge(Response.status(Response.Status.NOT_FOUND).build());
        }

    }

    private void actionPoll(AuthenticationFlowContext context) {
        String challengeValue = context.getAuthenticationSession().getAuthNote(NOTE_CHALLENGE);
        LoginFormsProvider formProvider = context.form();
        AuthnChallengeInfo info = challenges.findByChallenge(challengeValue);
        if (info == null) {
            context.challenge(Response.status(Response.Status.NOT_FOUND).build());
            return;
        } else {
            formProvider.setAttribute(ATTR_CHALLENGE, info);
        }
        Response form = formProvider.createForm("healthid/poll-response.ftl");
        if (info.isAuthenticated()) {

            context.challenge(Response
                    .status(Response.Status.OK)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(form.getEntity())
                    .build());
        } else {
            context.challenge(Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(form.getEntity())
                    .build());
        }
    }

    private void actionChallengeResponse(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        // see if username parameter ist provided
        String username = params.getFirst(PARAM_USERNAME);
        if (username == null) {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response
                            .status(Response.Status.BAD_REQUEST)
                            .entity(Json.createObjectBuilder()
                                    .add("error", "missing_parameter")
                                    .build())
                            .build());
            return;
        }

        // for simplicity reasons and since it's only a demo the "data to be signed" is equal to "signed data"
        String signature = params.getFirst(PARAM_SIGNATURE);
        AuthnChallengeInfo info = challenges.findByChallengeOrUserCode(signature);

        LOGGER.info(String.format("Got signature: %s", signature));
        LOGGER.info(String.format("Found pending challenge: %s", info));

        if (info == null) {
            LOGGER.error(String.format("Invalid User Code or Challenge: %s", signature));
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS,
                    Response
                            .status(Response.Status.FORBIDDEN)
                            .entity(Json.createObjectBuilder()
                                    .add("error", "access_denied")
                                    .build())
                            .build());
            return;
        }

        info.changeToAuthenticated(username);

        LoginFormsProvider formProvider = context.form();
        formProvider.setAttribute(ATTR_CHALLENGE, info);
        Response form = formProvider.createForm("healthid/poll-response.ftl");

        context.challenge(Response
                .status(Response.Status.OK)
                .type(MediaType.APPLICATION_JSON)
                .entity(form.getEntity())
                .build());

    }

    private void actionFinish(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> params = context.getHttpRequest().getDecodedFormParameters();
        String challengeValue = context.getAuthenticationSession().getAuthNote(NOTE_CHALLENGE);
        AuthnChallengeInfo info = challenges.findByChallengeOrUserCode(challengeValue);
        String deviceCode = params.getFirst(PARAM_DEVICE_CODE);
        if (!info.getDeviceCode().equals(deviceCode)) {
            challenges.remove(info);
            LOGGER.error(String.format("DeviceCode don't match: %s (remote) %s (local)", deviceCode, info.getDeviceCode()));
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, newFormProvider(context).createForm("healthid/qrcode-page.ftl"));
            return;
        }
        if (info.isAuthenticated()) {
            LOGGER.info(String.format("User '%s' is authenticated", info.getUsername()));
            challenges.remove(info);
            UserModel user = context.getSession().users().getUserByUsername(info.getUsername(), context.getRealm());
            context.setUser(user);
            context.success();
        } else {
            context.failure(AuthenticationFlowError.INVALID_CREDENTIALS, newFormProvider(context).createForm("healthid/qrcode-page.ftl"));
        }
    }

    private void actionDemo(AuthenticationFlowContext context) {
        UserModel user = context.getSession().users().getUserByUsername(DEMO_USER_NAME, context.getRealm());
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

}
