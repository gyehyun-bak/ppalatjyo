package ppalatjyo.server.domain.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.domain.QuizVisibility;
import ppalatjyo.server.domain.quiz.dto.CreateQuizRequestDto;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;
import ppalatjyo.server.domain.user.UserRepository;
import ppalatjyo.server.domain.user.domain.User;

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
        String description = "quiz description";
        QuizVisibility visibility = QuizVisibility.PUBLIC;
        User member = User.createMember("user", "", null);

        Long userId = 1L;

        CreateQuizRequestDto createQuizRequestDto = new CreateQuizRequestDto(userId, title, description, visibility);

        when(userRepository.findById(userId)).thenReturn(Optional.of(member));

        // when
        quizService.create(createQuizRequestDto);

        // then
        ArgumentCaptor<Quiz> captor = ArgumentCaptor.forClass(Quiz.class);
        verify(quizRepository).save(captor.capture());
        Quiz quiz = captor.getValue();
        assertThat(quiz.getTitle()).isEqualTo(title);
        assertThat(quiz.getDescription()).isEqualTo(description);
        assertThat(quiz.getVisibility()).isEqualTo(visibility);
    }

    @Test
    @DisplayName("Quiz 이름 수정")
    void changeTitle() {
        // given
        User member = User.createMember("user", "", null);
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
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("title", member);

        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // when
        quizService.delete(quizId);

        // then
        assertThat(quiz.isDeleted()).isTrue();
    }
}