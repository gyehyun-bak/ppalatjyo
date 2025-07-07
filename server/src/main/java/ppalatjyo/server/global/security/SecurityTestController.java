package ppalatjyo.server.global.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Slf4j
public class SecurityTestController {

    @GetMapping("/anonymous")
    public ResponseEntity<String> anonymous(Authentication authentication) {
        logAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/guest")
    public ResponseEntity<String> guest(Authentication authentication) {
        logAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    // Method Security에 의한 AccessDeniedException은 AuthorizationFilter가 아닌 Controller 에서 던져짐
    @PreAuthorize("hasRole('MEMBER')")
    @GetMapping("/member")
    public ResponseEntity<String> member(Authentication authentication) {
        logAuthentication(authentication);
        return ResponseEntity.ok().build();
    }

    private void logAuthentication(Authentication authentication) {
        log.info("Authentication object: {}", authentication);
    }
}
