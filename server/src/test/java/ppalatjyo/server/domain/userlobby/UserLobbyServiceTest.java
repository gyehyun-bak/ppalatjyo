package ppalatjyo.server.domain.userlobby;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.LobbyRepository;
import ppalatjyo.server.domain.lobby.LobbyService;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.userlobby.dto.JoinLobbyRequestDto;
import ppalatjyo.server.domain.userlobby.exception.LobbyIsFullException;
import ppalatjyo.server.domain.userlobby.exception.LobbyIsInGameException;
import ppalatjyo.server.domain.userlobby.exception.WrongLobbyPasswordException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class UserLobbyServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private LobbyRepository lobbyRepository;
    @Mock private UserLobbyRepository userLobbyRepository;
    @Mock private LobbyService lobbyService;
    @Mock private MessageService messageService;

    @InjectMocks
    private UserLobbyService userLobbyService;

    @Test
    @DisplayName("User가 Lobby에 참가")
    void join() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.create("lobby", host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(userLobbyRepository.existsByUserIdAndLobbyIdAndLeftAtIsNull(userId, lobbyId)).thenReturn(false);
        when(userLobbyRepository.countByLobbyIdAndLeftAtIsNull(lobbyId)).thenReturn(1);

        // when
        userLobbyService.join(new JoinLobbyRequestDto(userId, lobbyId));

        // then
        ArgumentCaptor<UserLobby> captor = ArgumentCaptor.forClass(UserLobby.class);
        verify(userLobbyRepository).save(captor.capture());
        verify(messageService).sendSystemMessage(any(), any());

        UserLobby userLobby = captor.getValue();

        assertThat(userLobby.getUser()).isEqualTo(user);
        assertThat(userLobby.getLobby()).isEqualTo(lobby);
        assertThat(userLobby.getJoinedAt()).isNotNull();
    }

    @Test
    @DisplayName("참가 시 게임 중이면 예외 발생")
    void shouldThrowExceptionWhenLobbyIsInGame() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.create("lobby", host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        Question.create(lobby.getQuiz(), "question");
        User user = User.createGuest("user");

        Game.start(lobby); // 게임 시작

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        // then
        assertThatThrownBy(() -> userLobbyService.join(new JoinLobbyRequestDto(userId, lobbyId)))
                .isInstanceOf(LobbyIsInGameException.class);
    }

    @Test
    @DisplayName("참가 시 로비가 이미 가득 찼으면 예외 발생")
    void shouldThrowExceptionWhenLobbyIsFull() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        int maxUsers = 10;
        LobbyOptions options = LobbyOptions.createOptions(maxUsers, 10, 10);

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.create("lobby", host, Quiz.createQuiz("quiz", host), options);
        User user = User.createGuest("user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(userLobbyRepository.existsByUserIdAndLobbyIdAndLeftAtIsNull(userId, lobbyId)).thenReturn(false);
        when(userLobbyRepository.countByLobbyIdAndLeftAtIsNull(lobbyId)).thenReturn(maxUsers);

        // when
        // then
        assertThatThrownBy(() -> userLobbyService.join(new JoinLobbyRequestDto(userId, lobbyId)))
                .isInstanceOf(LobbyIsFullException.class);
    }

    @Test
    @DisplayName("비밀번호가 설정된 로비 참가")
    void joinWithPassword() {
        // given
        String password = "1234";

        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.withPassword("lobby", password, host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(userLobbyRepository.existsByUserIdAndLobbyIdAndLeftAtIsNull(userId, lobbyId)).thenReturn(false);
        when(userLobbyRepository.countByLobbyIdAndLeftAtIsNull(lobbyId)).thenReturn(1);

        // when
        userLobbyService.join(new JoinLobbyRequestDto(userId, lobbyId, password));

        // then
        ArgumentCaptor<UserLobby> captor = ArgumentCaptor.forClass(UserLobby.class);
        verify(userLobbyRepository).save(captor.capture());
        verify(messageService).sendSystemMessage(any(), any());
        UserLobby userLobby = captor.getValue();
        assertThat(userLobby.getUser()).isEqualTo(user);
        assertThat(userLobby.getLobby()).isEqualTo(lobby);
        assertThat(userLobby.getJoinedAt()).isNotNull();
    }

    @Test
    @DisplayName("참가 시 비밀번호가 틀리면 예외 발생")
    void shouldThrowExceptionWhenWrongPassword() {
        // given
        String password = "1234";
        String wrongPassword = "123";

        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.withPassword("lobby", password, host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        // when
        // then
        assertThatThrownBy(() -> userLobbyService.join(new JoinLobbyRequestDto(userId, lobbyId, wrongPassword)))
                .isInstanceOf(WrongLobbyPasswordException.class);
    }

    @Test
    @DisplayName("User가 Lobby 나감")
    void leave() {
        // given
        Long userId = 1L;
        Long lobbyId = 1L;

        User host = User.createMember("host", "email", null);
        Lobby lobby = Lobby.create("lobby", host, Quiz.createQuiz("quiz", host), LobbyOptions.defaultOptions());
        User user = User.createGuest("user");

        UserLobby userLobby = UserLobby.join(user, lobby);

        when(userLobbyRepository.findByUserIdAndLobbyId(userId, lobbyId)).thenReturn(Optional.of(userLobby));

        // when
        userLobbyService.leave(userId, lobbyId);

        // then
        verify(lobbyService).deleteIfEmpty(lobbyId);
        verify(userLobbyRepository).flush();
        verify(messageService).sendSystemMessage(any(), any());
        assertThat(userLobby.isLeft()).isTrue();
    }
}