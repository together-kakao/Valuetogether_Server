package project.valuetogether.global.security.config.jwt;

public interface JwtProperties {
    String SECRET = "김현욱";
    int ACCESS_EXPIRATION_TIME = 1800000;  //30분
    int REFRESH_EXPIRATION_TIME = 1209600000; //2주
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_ACCESS = "Authorization_Access";
    String HEADER_REFRESH = "Authorization_Refresh";
}
