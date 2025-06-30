package ppalatjyo.server.game;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.game.dto.*;
import ppalatjyo.server.game.event.LeaderboardUpdateEvent;
import ppalatjyo.server.game.event.*;
import ppalatjyo.server.game.exception.GameNotFoundException;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.aop.SendAfterCommit;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.gamelog.GameLogService;
import ppalatjyo.server.lobby.LobbyRepository;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.message.MessageNotFoundException;
import ppalatjyo.server.message.MessageService;
import ppalatjyo.server.message.domain.Message;
import ppalatjyo.server.message.MessageRepository;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.exception.QuestionNotFoundException;
import ppalatjyo.server.quiz.repository.QuestionRepository;
import ppalatjyo.server.usergame.UserGame;
import ppalatjyo.server.usergame.UserGameNotFoundException;
import ppalatjyo.server.usergame.UserGameRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GameService {

    private final int SECONDS_BEFORE_FIRST_QUESTION = 10;
    private final int SECONDS_BEFORE_NEXT_QUESTION = 5;

    private final LobbyRepository lobbyRepository;
    private final UserGameRepository userGameRepository;
    private final MessageRepository messageRepository;
    private final QuestionRepository questionRepository;
    private final GameRepository gameRepository;
    private final GameLogService gameLogService;
    private final MessageService messageService;
    private final ApplicationEventPublisher eventPublisher;
    private final SchedulerService schedulerService;

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> start(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Game game = Game.start(lobby);

        gameRepository.save(game);
        gameLogService.started(game.getId());

        messageService.sendSystemMessage("게임이 시작되었습니다.", lobbyId);

        GameInfoDto gameInfoDto = GameInfoDto.create(game);
        GameEventDto gameEventDto = GameEventDto.started(gameInfoDto);

        NewQuestionDto newQuestionDto = new NewQuestionDto(game.getCurrentQuestion().getId(), game.getCurrentQuestion().getContent());
        schedulerService.runAfterSeconds(SECONDS_BEFORE_FIRST_QUESTION,
                () -> publishNewQuestion(lobby.getId(), game.getOptions().getSecPerQuestion(), newQuestionDto));

        return new SendAfterCommitDto<>("/lobbies/" + lobby.getId(), gameEventDto);
    }

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> publishNewQuestion(long lobbyId, int secPerQuestion, NewQuestionDto newQuestionDto) {
        GameEventDto gameEventDto = GameEventDto.newQuestion(newQuestionDto);

        return new SendAfterCommitDto<>("/lobbies/" + lobbyId, gameEventDto);
    }

    public void nextQuestion(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (!game.hasNextQuestion()) {
            end(gameId);
            return;
        }

        game.nextQuestion();

        eventPublisher.publishEvent(NextQuestionEvent.create(game));
    }

    public void end(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (game.isEnded()) {
            return;
        }

        game.end();

        eventPublisher.publishEvent(new GameEndedEvent(game.getId(), game.getLobby().getId()));
    }

    /**
     * 문제가 제시되고 정해진 시간이 지나면 호출됩니다. 현재 게임이 이미 종료되었거나, 타임 아웃된 문제가 현재 문제가 아니라면(이미 다음
     * 문제로 넘어갔다면) 조기에 리턴됩니다. 타임 아웃이 확인되면 이벤트를 발행하고 내부 {@code nextQuestion()}을 호출합니다.
     *
     * @param gameId     게임 ID
     * @param questionId 타임 아웃된 문제 ID
     */
    public void timeOut(Long gameId, Long questionId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (game.isEnded()) {
            return;
        }

        Question currentQuestion = game.getCurrentQuestion();
        Question timedOutQuestion = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if (!currentQuestion.equals(timedOutQuestion)) {
            return;
        }

        gameLogService.timeOut(gameId);

        eventPublisher.publishEvent(TimeOutEvent.create(game, timedOutQuestion));

        nextQuestion(gameId);
    }

    public void submitAnswer(SubmitAnswerRequestDto requestDto) {
        Game game = gameRepository.findById(requestDto.getGameId()).orElseThrow(GameNotFoundException::new);
        UserGame userGame = userGameRepository.findById(requestDto.getUserGameId()).orElseThrow(UserGameNotFoundException::new);
        Message message = messageRepository.findById(requestDto.getMessageId()).orElseThrow(MessageNotFoundException::new);
        Question currentQuestion = game.getCurrentQuestion();
        boolean isCorrect = currentQuestion.isCorrect(message.getContent());
        if (isCorrect) {
            gameLogService.rightAnswer(game.getId(), userGame.getId(), requestDto.getMessageId());

            eventPublisher.publishEvent(RightAnswerEvent.create(game, message, currentQuestion));

            userGame.increaseScoreBy(1); // 현재 1점으로 고정

            updateLeaderboard(requestDto.getLobbyId(), requestDto.getGameId()); // 리더보드(점수보드) 업데이트

            nextQuestion(requestDto.getGameId());
        } else {
            gameLogService.wrongAnswer(game.getId(), userGame.getId(), requestDto.getMessageId());
        }
    }

    private void updateLeaderboard(long gameId, long lobbyId) {
        List<UserGame> userGames = userGameRepository.findByGameIdOrderByScoreDesc(gameId);

        List<LeaderboardItemDto> leaderboard = userGames.stream()
                .map(LeaderboardItemDto::create)
                .toList();

        eventPublisher.publishEvent(new LeaderboardUpdateEvent(lobbyId, leaderboard));
    }
}
