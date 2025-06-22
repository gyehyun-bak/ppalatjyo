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

@Service
@RequiredArgsConstructor
@Transactional
public class LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final MessageService messageService;

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
}
