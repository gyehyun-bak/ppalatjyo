package ppalatjyo.server.gameevent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.GameRepository;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.gameevent.domain.GameEvent;
import ppalatjyo.server.message.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class GameEventService {

    private final GameEventRepository gameEventRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;
    private final MessageRepository messageRepository;

    public void started(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        GameEvent gameEvent = GameEvent.started(game);
        gameEventRepository.save(gameEvent);
    }

    public void ended(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        GameEvent gameEvent = GameEvent.ended(game);
        gameEventRepository.save(gameEvent);
    }

    public void timeOut(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        GameEvent gameEvent = GameEvent.timeOut(game);
        gameEventRepository.save(gameEvent);
    }

    public void rightAnswer(Long gameId, Long userGameId, Long messageId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow();
        Message message = messageRepository.findById(messageId).orElseThrow();

        GameEvent gameEvent = GameEvent.rightAnswer(game, userGame, message);
        gameEventRepository.save(gameEvent);
    }

    public void wrongAnswer(Long gameId, Long userGameId, Long messageId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow();
        Message message = messageRepository.findById(messageId).orElseThrow();

        GameEvent gameEvent = GameEvent.wrongAnswer(game, userGame, message);
        gameEventRepository.save(gameEvent);
    }
}
