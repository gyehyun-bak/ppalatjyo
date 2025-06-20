package ppalatjyo.server.gameevent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.GameRepository;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gameevent.domain.GameEvent;

@Service
@Transactional
@RequiredArgsConstructor
public class GameEventService {

    private final GameEventRepository gameEventRepository;
    private final GameRepository gameRepository;

    public void start(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        GameEvent gameEvent = GameEvent.started(game);
        gameEventRepository.save(gameEvent);
    }
}
