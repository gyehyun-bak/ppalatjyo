package ppalatjyo.server.domain.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.dto.CreateQuizRequestDto;
import ppalatjyo.server.domain.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;
import ppalatjyo.server.domain.user.exception.UserNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public void create(CreateQuizRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(UserNotFoundException::new);
        Quiz quiz = Quiz.createQuiz(requestDto.getTitle(), user, requestDto.getDescription(), requestDto.getVisibility());
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
