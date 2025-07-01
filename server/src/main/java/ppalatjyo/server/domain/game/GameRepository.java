package ppalatjyo.server.domain.game;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.game.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
