package ppalatjyo.server.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.lobby.dto.MessageToLobbyRequestDto;
import ppalatjyo.server.lobby.exception.LobbyNotFoundException;
import ppalatjyo.server.message.MessageService;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.quiz.repository.QuizRepository;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.exception.UserNotFoundException;
import ppalatjyo.server.userlobby.UserLobbyService;

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

    // UserLobbyService로 위임
    public void joinLobby(long userId, long lobbyId) {
        userLobbyService.join(userId, lobbyId);
    }

    /**
     * User가 Lobby를 나갑니다. User가 나가고 더 이상 Lobby에 참가한 인원이 없으면 Lobby를 삭제합니다.
     */
    public void leaveLobby(long userId, long lobbyId) {
        userLobbyService.leave(userId, lobbyId);

        // 로비가 비었으면 삭제
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
        if (lobby.isEmpty()) {
            lobby.delete();
        }
    }
}
