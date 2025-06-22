package ppalatjyo.server.gamelog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.GameRepository;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.exception.GameNotFoundException;
import ppalatjyo.server.gamelog.domain.GameLog;
import ppalatjyo.server.message.MessageNotFoundException;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameNotFoundException;
import ppalatjyo.server.usergame.UserGameRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class GameLogService {

    private final GameLogRepository gameLogRepository;
    private final GameRepository gameRepository;
    private final UserGameRepository userGameRepository;
    private final MessageRepository messageRepository;

    public void started(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        GameLog gameLog = GameLog.started(game);
        gameLogRepository.save(gameLog);
    }

    public void ended(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        GameLog gameLog = GameLog.ended(game);
        gameLogRepository.save(gameLog);
    }

    public void timeOut(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        GameLog gameLog = GameLog.timeOut(game);
        gameLogRepository.save(gameLog);
    }

    public void rightAnswer(Long gameId, Long userGameId, Long messageId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow(UserGameNotFoundException::new);
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);

        GameLog gameLog = GameLog.rightAnswer(game, userGame, message);
        gameLogRepository.save(gameLog);
    }

    public void wrongAnswer(Long gameId, Long userGameId, Long messageId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        UserGame userGame = userGameRepository.findById(userGameId).orElseThrow(UserGameNotFoundException::new);
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);

        GameLog gameLog = GameLog.wrongAnswer(game, userGame, message);
        gameLogRepository.save(gameLog);
    }
}
