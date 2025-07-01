package ppalatjyo.server.domain.userlobby;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLobbyRepository extends JpaRepository<UserLobby, Long> {
    Optional<UserLobby> findByUserIdAndLobbyId(Long userId, Long lobbyId);

    List<UserLobby> findByUserId(Long userId);

    int countByLobbyIdAndLeftAtIsNull(Long lobbyId);

    boolean existsByUserIdAndLobbyIdAndLeftAtIsNull(Long userId, Long lobbyId);
}
