package ppalatjyo.server.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ppalatjyo.server.global.security.jwt.JwtAuthenticationConverter;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventHandler {

    private final AuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    private final AuthenticationManager authenticationManager;
    // TODO: 유저 연결 이벤트 처리

    /**
     * STOMP CONNECT 헤더에 포함한 엑세스 토큰을 통해 유저 정보를 찾아내 웹소켓 세션에 보관합니다.
     */
    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        Principal userDetails = null;
        accessor.setUser(userDetails);
    }

    // TODO: 유저 연결 끊김 이벤트 처리
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {

    }
}
