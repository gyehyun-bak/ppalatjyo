package ppalatjyo.server.game;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.game.domain.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
