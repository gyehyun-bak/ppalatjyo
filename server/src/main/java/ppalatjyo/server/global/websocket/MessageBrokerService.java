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
     * 주어진 destination으로 메시지를 발행합니다.
     *
     * @param destination "/topic" 뒤에 붙을 문자열
     * @param dto 발행 메시지 Dto
     * @param <T> 실제 데이터 타입
     */
    public <T> void publish(String destination, PublicationDto<T> dto){
        try {
            simpMessagingTemplate.convertAndSend(TOPIC_PREFIX + destination, dto.getData());
        } catch (MessagingException ex) {
            log.error("Error sending message to {} {}", destination, ex.getMessage(), ex);
        }
    }
}
