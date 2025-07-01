package ppalatjyo.server.domain.message;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.message.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
