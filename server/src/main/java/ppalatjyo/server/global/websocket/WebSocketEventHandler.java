package ppalatjyo.server.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ppalatjyo.server.global.security.userdetails.CustomUserDetails;
import ppalatjyo.server.user.UserService;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventHandler {

    private final UserService userService;

    /**
     * 연결이 끊긴 유저에 대해 나가기 처리를 합니다.
     */
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        if (event.getUser() instanceof Authentication authentication
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails
        ) {
            Long userId = userDetails.getId();
            userService.disconnect(userId);
        }
    }
}
