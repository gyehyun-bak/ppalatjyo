package ppalatjyo.server.message;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ppalatjyo.server.message.event.ChatMessageSentEvent;
import ppalatjyo.server.message.event.SystemMessageSentEvent;

@Component
public class MessageEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleChatMessageSentEvent(ChatMessageSentEvent event) {

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSystemMessageSentEvent(SystemMessageSentEvent event) {

    }
}
