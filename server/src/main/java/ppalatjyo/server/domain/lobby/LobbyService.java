package ppalatjyo.server.domain.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.game.GameService;
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
import ppalatjyo.server.domain.userlobby.UserLobbyRepository;

/**
 * Lobby 도메인의 상태와 생명 주기를 관리합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserLobbyRepository userLobbyRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final MessageService messageService;
    private final GameService gameService;

    public void createLobby(String name, long hostId, long quizId, LobbyOptions options) {
        User host = userRepository.findById(hostId).orElseThrow(UserNotFoundException::new);
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);

        Lobby lobby = Lobby.create(name, host, quiz, options);
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

    public void deleteIfEmpty(long lobbyId) {
        if (userLobbyRepository.countByLobbyIdAndLeftAtIsNull(lobbyId) == 0) {
            Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow(LobbyNotFoundException::new);
            gameService.endGamesByLobbyId(lobbyId);
            lobby.delete();
        }
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
}
