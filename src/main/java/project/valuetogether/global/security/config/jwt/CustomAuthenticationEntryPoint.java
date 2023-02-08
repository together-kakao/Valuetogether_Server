package project.valuetogether.global.security.config.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import project.valuetogether.global.security.error.ErrorCode;
import project.valuetogether.global.security.error.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, ErrorCode.UNKNOWN_ERROR);
        }
        if (ErrorCode.EXPIRED_JWT.getMessage().equals(exception)) {
            setResponse(response, ErrorCode.EXPIRED_JWT);
        }
        if (ErrorCode.INVALID_JWT.getMessage().equals(exception)) {
            setResponse(response, ErrorCode.INVALID_JWT);
        }
        if (ErrorCode.NON_EXISTENT.getMessage().equals(exception)) {
            setResponse(response, ErrorCode.NON_EXISTENT);
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ObjectMapper om = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, errorCode.getMessage());

        String result = om.writeValueAsString(errorResponse);

        response.getWriter().write(result);
    }
}
