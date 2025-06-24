package ppalatjyo.server.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ppalatjyo.server.global.websocket.dto.PublicationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageBrokerService {

    private static final String TOPIC_PREFIX = "/topic";

    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 주어진 destination으로 data를 발행합니다.
     *
     * @param destination "/topic" 뒤에 붙을 문자열
     * @param data 발행 데이터
     */
    public void publish(String destination, Object data){
        try {
            simpMessagingTemplate.convertAndSend(TOPIC_PREFIX + destination, data);
        } catch (MessagingException ex) {
            log.error("Error sending message to {} {}", destination, ex.getMessage(), ex);
        }
    }
}
