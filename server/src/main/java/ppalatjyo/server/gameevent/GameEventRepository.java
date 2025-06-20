package ppalatjyo.server.gameevent;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.gameevent.domain.GameEvent;

public interface GameEventRepository extends JpaRepository<GameEvent, Long> {
}
