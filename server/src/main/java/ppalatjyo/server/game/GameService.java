package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.dto.SubmitAnswerRequestDto;
import ppalatjyo.server.game.exception.GameNotFoundException;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.gameevent.GameEventService;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.MessageNotFoundException;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameNotFoundException;
import ppalatjyo.server.usergame.UserGameRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final LobbyRepository lobbyRepository;
    private final UserGameRepository userGameRepository;
    private final MessageRepository messageRepository;
    private final GameRepository gameRepository;
    private final GameEventService gameEventService;

    public void start(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Game game = Game.start(lobby);

        gameRepository.save(game);
        gameEventService.started(game.getId());
    }

    public void nextQuestion(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        game.nextQuestion();
    }

    public void end(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        game.end();
    }

    public void submitAnswer(SubmitAnswerRequestDto requestDto) {
        Game game = gameRepository.findById(requestDto.getGameId()).orElseThrow(GameNotFoundException::new);
        UserGame userGame = userGameRepository.findById(requestDto.getUserGameId()).orElseThrow(UserGameNotFoundException::new);
        Message message = messageRepository.findById(requestDto.getMessageId()).orElseThrow(MessageNotFoundException::new);

        boolean isCorrect = game.getCurrentQuestion().isCorrect(message.getContent());
        if (isCorrect) {
            gameEventService.rightAnswer(game.getId(), userGame.getId(), requestDto.getMessageId());
        } else {
            gameEventService.wrongAnswer(game.getId(), userGame.getId(), requestDto.getMessageId());
        }
    }
}
