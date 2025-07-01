package ppalatjyo.server.domain.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.game.domain.Game;
import ppalatjyo.server.domain.lobby.domain.Lobby;
import ppalatjyo.server.domain.lobby.domain.LobbyOptions;
import ppalatjyo.server.domain.lobby.dto.MessageToLobbyRequestDto;
import ppalatjyo.server.domain.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.domain.message.MessageService;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;
import ppalatjyo.server.domain.userlobby.UserLobbyService;
import ppalatjyo.server.domain.userlobby.exception.LobbyIsFullException;

@Service
@RequiredArgsConstructor
@Transactional
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final MessageService messageService;
    private final UserLobbyService userLobbyService;

    public void createLobby(String name, long hostId, long quizId, LobbyOptions options) {
        User host = userRepository.findById(hostId).orElseThrow(UserNotFoundException::new);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);

        Lobby lobby = Lobby.createLobby(name, host, quiz, options);
        lobbyRepository.save(lobby);
    }

    public void changeOptions(long lobbyId, LobbyOptions options) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        lobby.changeOptions(options);
    }

    public void delete(long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        lobby.delete();
    }

    public void changeHost(long lobbyId, long newHostId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        User newHost = userRepository.findById(newHostId).orElseThrow(UserNotFoundException::new);

        lobby.changeHost(newHost);
    }

    public void changeQuiz(long lobbyId, long quizId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);

        lobby.changeQuiz(quiz);
    }

    // MessageService로 위임
    public void sendMessageToLobby(MessageToLobbyRequestDto requestDto) {
        messageService.sendChatMessage(requestDto.getContent(), requestDto.getUserId(), requestDto.getLobbyId());
    }

    /**
     * Lobby에 참가합니다. {@link UserLobbyService}로 위임합니다. 로비가 이미 가득 찬 경우 {@link LobbyIsFullException}을 던집니다.
     */
    public void joinLobby(long userId, long lobbyId) throws LobbyIsFullException {
        userLobbyService.join(userId, lobbyId);
    }

    /**
     * <p> User가 Lobby를 나갑니다. User가 나가고 더 이상 Lobby에 참가한 인원이 없으면 Lobby를 삭제합니다.
     * <p> Lobby에서 실행되는 게임을 모두 종료합니다. (진행 중인 게임이 있는 경우 끝까지 진행되지 않게 하기 위함)
     */
    public void leaveLobby(long userId, long lobbyId) {
        userLobbyService.leave(userId, lobbyId);

        // 로비가 비었으면 게임 종료 후 삭제
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        if (lobby.isEmpty()) {
            lobby.getGames().forEach(Game::end);
            lobby.delete();
        }
    }
}
