package dev.spilikin.healthid.keycloak;

import org.keycloak.common.util.Base64Url;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simple cache for the pending authentication challenges
 */
public class AuthnChallengeProvider {
    /**
     * Only uppercase and numeric are used in user codes
     */
    private static final char[] SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    /**
     * Expire the old challenges after N milliseconds
     */
    final static long expireAfter = 10*60*1000;
    /**
     * The actual in-memory collection
     */
    private final Set<AuthnChallengeInfo> cache = Collections.synchronizedSet(new HashSet<>());

    /**
     * Remove all elements from the cache, which are older than N milliseconds
     */
    public synchronized void expire() {
        cache.removeIf(info -> ((System.currentTimeMillis()-info.getTimestamp()) > expireAfter));
    }

    /**
     * Find {@link AuthnChallengeInfo} by it's challenge value or by the user code
     * @param value
     * @return matching challenge or null
     */
    public AuthnChallengeInfo findByChallengeOrUserCode(String value) {
        for (AuthnChallengeInfo info: cache) {
            if (info.getChallenge().equals(value)) {
                return info;
            } else if (info.getUserCode().equals(value)) {
                return info;
            }
        }
        return null;
    }

    /**
     * Find {@link AuthnChallengeInfo} by it's challenge value
     * @param value
     * @return matching challenge or null
     */
    public AuthnChallengeInfo findByChallenge(String value) {
        for (AuthnChallengeInfo info: cache) {
            if (info.getChallenge().equals(value)) {
                return info;
            }
        }
        return null;
    }


    /**
     * Create and cache the new challenge
     * @return
     */
    public AuthnChallengeInfo next() {
        AuthnChallengeInfo info = new AuthnChallengeInfo(generateRandomString(), generateUserCode(6), generateRandomString());
        cache.add(info);
        expire();
        return info;
    }

    /**
     * Generate random user-friendly code of given length
     * @param length
     * @return
     */
    private String generateUserCode(int length) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buf.append(SYMBOLS[ThreadLocalRandom.current().nextInt(SYMBOLS.length)]);
        }
        return buf.toString();
    }

    /**
     * Generates random string
     * @return
     */
    public static String generateRandomString() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        return Base64Url.encode(bytes);
    }

    /**
     * Removes the challenge from the cache
     * @param info
     */
    public void remove(AuthnChallengeInfo info) {
        cache.remove(info);
    }


}
