package ppalatjyo.server.lobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.game.domain.Game;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.lobby.dto.MessageToLobbyRequestDto;
import ppalatjyo.server.message.MessageService;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.repository.QuizRepository;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.userlobby.UserLobbyService;

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

    @Mock
    private UserLobbyService userLobbyService;

    @InjectMocks
    private LobbyService lobbyService;

    @Test
    @DisplayName("Lobby 생성")
    void createLobby() {
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
        Lobby lobby = Lobby.createLobby(name, host, quiz, options);

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
        Lobby lobby = Lobby.createLobby(name, host, quiz, options);

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
        Lobby lobby = Lobby.createLobby(name, oldHost, quiz, options);

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
        Lobby lobby = Lobby.createLobby("lobby", User.createGuest("host"), oldQuiz, LobbyOptions.defaultOptions());

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

    @Test
    @DisplayName("채팅방 참가 -> UserLobbyService로 위임")
    void joinLobby() {
        // given
        long userId = 1L;
        long lobbyId = 1L;

        // when
        lobbyService.joinLobby(userId, lobbyId);

        // then
        verify(userLobbyService, times(1)).join(userId, lobbyId);
    }

    @Test
    @DisplayName("채팅방 나가기 -> UserLobbyService로 위임")
    void leaveLobby() {
        // given
        long userId = 1L;
        long lobbyId = 1L;

        Lobby lobby = Mockito.mock(Lobby.class);
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(lobby.isEmpty()).thenReturn(false);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        lobbyService.leaveLobby(userId, lobbyId);

        // then
        verify(userLobbyService).leave(userId, lobbyId);
        verify(lobby, never()).delete();
    }

    @Test
    @DisplayName("채팅방 나가기 -> 남은 사람이 없으면 채팅방 삭제")
    void leaveLobbyDeleteWhenEmpty() {
        // given
        long userId = 1L;
        long lobbyId = 1L;

        Lobby lobby = Mockito.mock(Lobby.class);
        Game game = Mockito.mock(Game.class);
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(lobby.isEmpty()).thenReturn(true);
        when(lobby.getGames()).thenReturn(List.of(game));

        // when
        lobbyService.leaveLobby(userId, lobbyId);

        // then
        verify(userLobbyService).leave(userId, lobbyId);
        verify(lobbyRepository).findById(lobbyId);
        verify(game).end();
        verify(lobby).delete();
    }
}