package ppalatjyo.server.domain.game;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.game.domain.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByLobbyId(Long lobbyId);
}
