package ppalatjyo.server.domain.game;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.game.dto.*;
import ppalatjyo.server.domain.game.exception.GameNotFoundException;
import ppalatjyo.server.global.scheduler.SchedulerService;
import ppalatjyo.server.global.websocket.aop.SendAfterCommit;
import ppalatjyo.server.global.websocket.aop.SendAfterCommitDto;
import ppalatjyo.server.domain.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.domain.lobby.LobbyRepository;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.message.MessageNotFoundException;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.message.domain.Message;
import ppalatjyo.server.domain.message.MessageRepository;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.exception.QuestionNotFoundException;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;
import ppalatjyo.server.domain.usergame.UserGame;
import ppalatjyo.server.domain.usergame.UserGameNotFoundException;
import ppalatjyo.server.domain.usergame.UserGameRepository;

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
    private final MessageService messageService;
    private final SchedulerService schedulerService;

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> start(Long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);

        Game game = Game.start(lobby);
        gameRepository.save(game);

        GameInfoDto gameInfoDto = GameInfoDto.create(game);
        GameEventDto gameEventDto = GameEventDto.started(gameInfoDto);

        NewQuestionDto newQuestionDto = new NewQuestionDto(game.getCurrentQuestion().getId(), game.getCurrentQuestion().getContent());
        messageService.sendSystemMessage("게임이 시작되었습니다.", lobbyId);
        schedulerService.runAfterSeconds(SECONDS_BEFORE_FIRST_QUESTION,
                () -> publishNewQuestion(game.getId(), lobby.getId(), game.getOptions().getSecPerQuestion(), newQuestionDto));
        schedulerService.runAfterMinutes(game.getOptions().getMinPerGame(), () -> end(game.getId()));

        return new SendAfterCommitDto<>(getDestination(lobbyId), gameEventDto);
    }

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> publishNewQuestion(long gameId, long lobbyId, int secPerQuestion, NewQuestionDto newQuestionDto) {
        GameEventDto gameEventDto = GameEventDto.newQuestion(newQuestionDto);

        schedulerService.runAfterSeconds(secPerQuestion,
                () -> timeOut(gameId, newQuestionDto.getQuestionId()));

        return new SendAfterCommitDto<>(getDestination(lobbyId), gameEventDto);
    }

    public void nextQuestion(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (!game.hasNextQuestion()) {
            end(gameId);
            return;
        }

        game.nextQuestion();

        NewQuestionDto newQuestionDto = new NewQuestionDto(game.getCurrentQuestion().getId(), game.getCurrentQuestion().getContent());
        schedulerService.runAfterSeconds(SECONDS_BEFORE_NEXT_QUESTION,
                () -> publishNewQuestion(game.getId(), game.getLobby().getId(), game.getOptions().getSecPerQuestion(), newQuestionDto));
    }

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> end(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (game.isEnded()) {
            return null;
        }

        game.end();

        return new SendAfterCommitDto<>(getDestination(game.getLobby().getId()), GameEventDto.ended());
    }

    /**
     * 문제가 제시되고 정해진 시간이 지나면 호출됩니다. 현재 게임이 이미 종료되었거나, 타임 아웃된 문제가 현재 문제가 아니라면(이미 다음
     * 문제로 넘어갔다면) 조기에 리턴됩니다. 타임 아웃이 확인되면 이벤트를 발행하고 내부 {@code nextQuestion()}을 호출합니다.
     *
     * @param gameId     게임 ID
     * @param questionId 타임 아웃된 문제 ID
     */
    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> timeOut(Long gameId, Long questionId) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);

        if (game.isEnded()) {
            return null;
        }

        Question currentQuestion = game.getCurrentQuestion();
        Question timedOutQuestion = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        if (!currentQuestion.equals(timedOutQuestion)) {
            return null;
        }

        nextQuestion(gameId);

        AnswerInfoDto answerInfoDto = AnswerInfoDto.create(timedOutQuestion);
        return new SendAfterCommitDto<>(getDestination(game.getLobby().getId()), GameEventDto.timeOut(answerInfoDto));
    }

    @SendAfterCommit
    public SendAfterCommitDto<GameEventDto> submitAnswer(SubmitAnswerRequestDto requestDto) {
        Game game = gameRepository.findById(requestDto.getGameId()).orElseThrow(GameNotFoundException::new);
        UserGame userGame = userGameRepository.findById(requestDto.getUserGameId()).orElseThrow(UserGameNotFoundException::new);
        Message message = messageRepository.findById(requestDto.getMessageId()).orElseThrow(MessageNotFoundException::new);
        Question currentQuestion = game.getCurrentQuestion();

        if (!currentQuestion.isCorrect(message.getContent())) {
            return null;
        }

        userGame.increaseScoreBy(1); // 현재 1점으로 고정
        messageService.sendSystemMessage(userGame.getUser().getNickname() + "님이 정답을 맞히셨습니다.", game.getLobby().getId());

        updateLeaderboard(requestDto.getLobbyId(), requestDto.getGameId()); // 리더보드(점수보드) 업데이트
        nextQuestion(requestDto.getGameId());

        AnswerInfoDto answerInfoDto = AnswerInfoDto.create(currentQuestion, userGame.getUser(), message);
        return new SendAfterCommitDto<>(getDestination(game.getLobby().getId()), GameEventDto.correct(answerInfoDto));
    }

    @SendAfterCommit
    private SendAfterCommitDto<LeaderboardUpdateDto> updateLeaderboard(long gameId, long lobbyId) {
        List<UserGame> userGames = userGameRepository.findByGameIdOrderByScoreDesc(gameId);

        List<LeaderboardItemDto> leaderboard = userGames.stream()
                .map(LeaderboardItemDto::create)
                .toList();

        String destination = "/lobbies/" + lobbyId + "/games/leaderboard/update";
        return new SendAfterCommitDto<>(destination, new LeaderboardUpdateDto(leaderboard));
    }

    private String getDestination(Long lobbyId) {
        final String GAME_EVENT_DESTINATION_PREFIX = "/lobbies/";
        final String GAME_EVENT_DESTINATION_SUFFIX = "/games/events";

        return GAME_EVENT_DESTINATION_PREFIX + lobbyId + GAME_EVENT_DESTINATION_SUFFIX;
    }
}
