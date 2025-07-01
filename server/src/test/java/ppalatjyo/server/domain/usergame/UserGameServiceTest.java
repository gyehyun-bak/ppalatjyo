package ppalatjyo.server.domain.usergame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.usergame.UserGame;
import ppalatjyo.server.domain.usergame.UserGameRepository;
import ppalatjyo.server.domain.usergame.UserGameService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserGameServiceTest {

    @Mock
    private UserGameRepository userGameRepository;

    @InjectMocks
    private UserGameService userGameService;

    @Test
    @DisplayName("점수 추가")
    void increaseScoreBy() {
        // given
        User user = User.createGuest("user");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("m", "email", "password"));
        Lobby lobby = Lobby.createLobby(
                "lobby",
                user,
                quiz,
                LobbyOptions.defaultOptions());
        Question.create(quiz, "question");
        Game game = Game.start(lobby);
        UserGame userGame = game.getUserGames().getFirst();
        Long userGameId = 1L;
        when(userGameRepository.findById(userGameId)).thenReturn(Optional.of(userGame));

        // when
        userGameService.increaseScoreBy(userGameId, 1);

        // then
        assertThat(userGame.getScore()).isEqualTo(1);
    }
}