package ppalatjyo.server.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.global.websocket.MessageBrokerService;
import ppalatjyo.server.global.websocket.dto.MessagePublicationDto;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.event.ChatMessageSentEvent;
import ppalatjyo.server.message.event.SystemMessageSentEvent;

/**
 * <p> {@link MessageService}가 발행한 이벤트를 처리합니다. {@link MessageService} 각 메서드의 트랜잭션 성공 여부에 따라 처리를 분기합니다. 브로커를 통한 실제 메시지 발행을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class MessageEventHandler {

    private final MessageBrokerService messageBrokerService;
    private final MessageRepository messageRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatMessageSentEvent(ChatMessageSentEvent event) {
        Message message = messageRepository.findById(event.getMessageId()).orElseThrow(MessageNotFoundException::new);

        MessageDto messageDto = MessageDto.chatMessage(message);

        MessagePublicationDto<MessageDto> publicationDto = new MessagePublicationDto<>();
        publicationDto.setDestination("lobbies/" + message.getLobby().getId() + "/messages/new");
        publicationDto.setData(messageDto);

        messageBrokerService.publish(publicationDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSystemMessageSentEvent(SystemMessageSentEvent event) {
        Message message = messageRepository.findById(event.getMessageId()).orElseThrow(MessageNotFoundException::new);

        MessageDto messageDto = MessageDto.systemMessage(message);

        MessagePublicationDto<MessageDto> publicationDto = new MessagePublicationDto<>();
        publicationDto.setDestination("lobbies/" + message.getLobby().getId() + "/messages/new");
        publicationDto.setData(messageDto);

        messageBrokerService.publish(publicationDto);
    }
}
