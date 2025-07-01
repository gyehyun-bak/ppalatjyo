package ppalatjyo.server.domain.lobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.lobby.dto.MessageToLobbyRequestDto;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.userlobby.UserLobbyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LobbyServiceTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private LobbyService lobbyService;

    @Test
    @DisplayName("Lobby 생성")
    void create() {
        // given
        String name = "lobby";
        User host = User.createGuest("host");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        long hostId = 1L;
        long quizId = 1L;
        int maxUsers = 10;
        int minPerGame = 10;
        int secPerQuestion = 30;

        when(userRepository.findById(hostId)).thenReturn(Optional.of(host));
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        LobbyOptions options = LobbyOptions.createOptions(maxUsers, minPerGame, secPerQuestion);

        // when
        lobbyService.createLobby(name, hostId, quizId, options);

        // then
        ArgumentCaptor<Lobby> captor = ArgumentCaptor.forClass(Lobby.class);
        verify(lobbyRepository, times(1)).save(captor.capture());

        Lobby createdLobby = captor.getValue();
        assertThat(createdLobby.getName()).isEqualTo(name);
        assertThat(createdLobby.getHost()).isEqualTo(host);
        assertThat(createdLobby.getUserLobbies().size()).isEqualTo(1);
        assertThat(createdLobby.getQuiz()).isEqualTo(quiz);
        assertThat(createdLobby.getOptions().getMaxUsers()).isEqualTo(maxUsers);
    }

    @Test
    @DisplayName("Lobby 옵션 변경")
    void changeOptions() {
        // given
        String name = "lobby";
        User host = User.createGuest("host");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        LobbyOptions options = LobbyOptions.createOptions(10, 10, 10);
        Lobby lobby = Lobby.create(name, host, quiz, options);

        long lobbyId = 1L;
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        LobbyOptions newOptions = LobbyOptions.createOptions(20, 20, 20);

        // when
        lobbyService.changeOptions(1L, newOptions);

        // then
        assertThat(lobby.getOptions().getMaxUsers()).isEqualTo(newOptions.getMaxUsers());
        assertThat(lobby.getOptions().getMinPerGame()).isEqualTo(newOptions.getMinPerGame());
        assertThat(lobby.getOptions().getSecPerQuestion()).isEqualTo(newOptions.getSecPerQuestion());
    }

    @Test
    @DisplayName("Lobby 삭제")
    void deleteLobby() {
        // given
        String name = "lobby";
        User host = User.createGuest("host");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        LobbyOptions options = LobbyOptions.defaultOptions();
        Lobby lobby = Lobby.create(name, host, quiz, options);

        long lobbyId = 1L;
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        lobbyService.delete(lobbyId);

        // then
        assertThat(lobby.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Host 변경")
    void changeHost() {
        // given
        String name = "lobby";
        User oldHost = User.createGuest("oldHost");
        User newHost = User.createGuest("newHost");
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        LobbyOptions options = LobbyOptions.defaultOptions();
        Lobby lobby = Lobby.create(name, oldHost, quiz, options);

        long lobbyId = 1L;
        long newHostId = 1L;
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(userRepository.findById(newHostId)).thenReturn(Optional.of(newHost));

        // when
        lobbyService.changeHost(lobbyId, newHostId);

        // then
        assertThat(lobby.getHost()).isEqualTo(newHost);
    }

    @Test
    @DisplayName("Quiz 변경")
    void changeQuiz() {
        // given
        Quiz oldQuiz = Quiz.createQuiz("oldQuiz", User.createMember("n", "e", "p"));
        Quiz newQuiz = Quiz.createQuiz("newQuiz", User.createMember("n", "e", "p"));
        Lobby lobby = Lobby.create("lobby", User.createGuest("host"), oldQuiz, LobbyOptions.defaultOptions());

        long newQuizId = 1L;
        long lobbyId = 1L;
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(quizRepository.findById(newQuizId)).thenReturn(Optional.of(newQuiz));

        // when
        lobbyService.changeQuiz(lobbyId, newQuizId);

        // then
        assertThat(lobby.getQuiz()).isEqualTo(newQuiz);
    }

    @Test
    @DisplayName("Lobby에 메시지 송신 -> MessageService로 위임")
    void sendMessageToLobby() {
        // given
        String content = "content";
        Long userId = 1L;
        Long lobbyId = 1L;
        MessageToLobbyRequestDto requestDto = new MessageToLobbyRequestDto(userId, lobbyId, content);

        // when
        lobbyService.sendMessageToLobby(requestDto);

        // then
        verify(messageService, times(1)).sendChatMessage(content, userId, lobbyId);
    }
}