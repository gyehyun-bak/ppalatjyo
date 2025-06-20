package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gameevent.GameEventService;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final LobbyRepository lobbyRepository;
    private final GameRepository gameRepository;
    private final GameEventService gameEventService;

    public void start(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();

        Game game = Game.start(lobby);

        gameRepository.save(game);
        gameEventService.started(game.getId());
    }

    public void nextQuestion(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.nextQuestion();
    }

    public void end(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        game.end();
    }
}
