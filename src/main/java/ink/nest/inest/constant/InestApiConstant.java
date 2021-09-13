package ink.nest.inest.constant;

public class InestApiConstant {
    // Path Controllers
    public static final String API_V1_PATH = "/api/v1";
    public static final String API_AUTH_PATH = API_V1_PATH + "/auth";

    // Expires token at 1 week
    public static final Integer JWT_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;

    // Header Constants
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_BEARER = "Bearer ";

}
