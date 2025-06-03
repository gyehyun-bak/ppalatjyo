package ppalatjyo.server.lobby;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.lobby.domain.Lobby;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {
}
