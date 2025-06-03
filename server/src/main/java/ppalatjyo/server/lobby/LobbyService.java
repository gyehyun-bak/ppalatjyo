package ppalatjyo.server.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.lobby.domain.Lobby;
import ppalatjyo.server.lobby.domain.LobbyOptions;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.QuizRepository;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

@Service
@RequiredArgsConstructor
@Transactional
public class LobbyService {

    private LobbyRepository lobbyRepository;
    private UserRepository userRepository;
    private QuizRepository quizRepository;

    public void createLobby(long hostId, long quizId, LobbyOptions options) {
        User host = userRepository.findById(hostId).orElseThrow();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        Lobby lobby = Lobby.createLobby(host, quiz, options);
        lobbyRepository.save(lobby);
    }

    public void changeOptions(long lobbyId, LobbyOptions options) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();
        lobby.changeOptions(options);
    }

    public void delete(long lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();
        lobby.delete();
    }

    public void changeHost(long lobbyId, long newHostId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();
        User newHost = userRepository.findById(newHostId).orElseThrow();

        lobby.changeHost(newHost);
    }

    public void changeQuiz(long lobbyId, long quizId) {
        Lobby lobby = lobbyRepository.findById(lobbyId).orElseThrow();
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();

        lobby.changeQuiz(quiz);
    }
}
