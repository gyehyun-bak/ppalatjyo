package ppalatjyo.server.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.repository.QuizRepository;
import ppalatjyo.server.user.UserRepository;
import ppalatjyo.server.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private QuizService quizService;

    @Test
    @DisplayName("Quiz 생성")
    void createQuiz() {
        // given
        String title = "quiz";
        User member = User.createMember("user", "", "");

        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(member));

        // when
        quizService.create(title, userId);

        // then
        verify(quizRepository, times(1)).save(any(Quiz.class));
    }

    @Test
    @DisplayName("Quiz 이름 수정")
    void changeTitle() {
        // given
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("title", member);

        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        String newTitle = "newTitle";

        // when
        quizService.changeTitle(quizId, newTitle);

        // then
        assertThat(quiz.getTitle()).isEqualTo(newTitle);
    }

    @Test
    @DisplayName("Quiz 삭제")
    void delete() {
        // given
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("title", member);

        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // when
        quizService.delete(quizId);

        // then
        assertThat(quiz.isDeleted()).isTrue();
    }
}