package ppalatjyo.server.message;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.message.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
