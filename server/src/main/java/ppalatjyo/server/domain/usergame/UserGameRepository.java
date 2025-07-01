package ppalatjyo.server.domain.usergame;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByGameIdOrderByScoreDesc(Long gameId);
}
