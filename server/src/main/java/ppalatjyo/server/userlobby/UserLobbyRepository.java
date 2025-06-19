package ppalatjyo.server.userlobby;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLobbyRepository extends JpaRepository<UserLobby, Long> {
    Optional<UserLobby> findByUserIdAndLobbyId(Long userId, Long lobbyId);
}
