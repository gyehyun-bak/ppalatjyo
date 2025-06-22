package ppalatjyo.server.gamelog;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.gamelog.domain.GameLog;

public interface GameLogRepository extends JpaRepository<GameLog, Long> {
}
