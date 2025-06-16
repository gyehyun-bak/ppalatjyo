package ppalatjyo.server.global.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
public class PermittedUrlChecker {
    private final List<String> permittedUrls  = List.of(
            "/",
            "/error",
            "/test/**",
            "/docs/**",
            "/favicon.ico",
            "/api/auth/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean isPermitted(String requestUri) {
        return permittedUrls .stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }

    public String[] toArray() {
        return permittedUrls.toArray(new String[0]);
    }
}
