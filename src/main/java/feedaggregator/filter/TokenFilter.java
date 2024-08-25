package feedaggregator.filter;

import feedaggregator.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class TokenFilter extends HttpFilter {

    private final TokenService tokenService;

    public TokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String pathInfo = request.getRequestURI();
        if (pathInfo != null && !pathInfo.startsWith("/api/token")) {
            String header = request.getHeader("Authorization");
            if (header == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            if (!header.startsWith("Bearer")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            String token = StringUtils.substringAfter(header, "Bearer ");
            Long userId = tokenService.getUserIdFromToken(token);
            if (userId == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            request.setAttribute("userId", userId);
        }
        super.doFilter(request, response, chain);
    }
}
