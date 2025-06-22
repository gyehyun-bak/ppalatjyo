package ppalatjyo.server.global.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import ppalatjyo.server.global.security.jwt.JwtAuthenticationToken;

@RequiredArgsConstructor
public class MyChannelInterceptor implements ChannelInterceptor {

    private final AuthenticationManager authenticationManager;

    /**
     * STOMP CONNECT 헤더에 포함한 엑세스 토큰을 통해 유저 정보를 찾아내 웹소켓 세션(User)에 보관합니다.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {
            attemptAuthentication(accessor);
        }

        return message;
    }

    private void attemptAuthentication(StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader("accessToken");
        JwtAuthenticationToken unauthenticated = JwtAuthenticationToken.unauthenticated(accessToken);

        Authentication authenticationResult = authenticationManager.authenticate(unauthenticated);
        accessor.setUser(authenticationResult);
    }
}
