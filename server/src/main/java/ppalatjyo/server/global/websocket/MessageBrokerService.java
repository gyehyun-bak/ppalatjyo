package ppalatjyo.server.global.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ppalatjyo.server.global.websocket.dto.MessagePublicationDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageBrokerService {

    private static final String TOPIC_PREFIX = "/topic/";

    private final SimpMessagingTemplate simpMessagingTemplate;

    public <T> void publish(MessagePublicationDto<T> dto){
        try {
            simpMessagingTemplate.convertAndSend(TOPIC_PREFIX + dto.getDestination(), dto.getData());
        } catch (MessagingException ex) {
            log.error("Error sending message to {} {}", dto.getDestination(), ex.getMessage(), ex);
        }
    }
}
