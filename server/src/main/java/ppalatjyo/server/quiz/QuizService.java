package ppalatjyo.server.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.quiz.repository.QuizRepository;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;
import ppalatjyo.server.user.exception.UserNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public void create(String title, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Quiz quiz = Quiz.createQuiz(title, user);
        quizRepository.save(quiz);
    }

    public void changeTitle(Long quizId, String title) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
        quiz.changeTitle(title);
    }

    public void delete(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
        quiz.delete();
    }
}
