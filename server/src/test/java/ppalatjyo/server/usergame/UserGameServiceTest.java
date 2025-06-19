package ppalatjyo.server.usergame;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

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
    void increaseScore() {
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
        userGameService.addScore(userGameId);

        // then
        assertThat(userGame.getScore()).isEqualTo(1);
    }
}