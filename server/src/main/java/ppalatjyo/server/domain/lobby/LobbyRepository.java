package ppalatjyo.server.domain.lobby;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.lobby.domain.Lobby;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
}
