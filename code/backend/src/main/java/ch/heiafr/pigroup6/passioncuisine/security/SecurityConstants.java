package ch.heiafr.pigroup6.passioncuisine.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String ROLE_KEY = "roles";

    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String HEADER_STRING = "Authorization";

    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String LOGIN_URL = "/users/login";
}
