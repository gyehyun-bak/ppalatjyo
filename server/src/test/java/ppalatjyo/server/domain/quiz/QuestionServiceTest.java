package ppalatjyo.server.domain.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.dto.CreateQuestionRequestDto;
import ppalatjyo.server.domain.quiz.dto.UpdateQuestionRequestDto;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;
import ppalatjyo.server.domain.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    @DisplayName("Question 생성, Quiz에 추가")
    void createQuestion() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("title", member);

        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        CreateQuestionRequestDto createQuestionRequestDto = new CreateQuestionRequestDto();
        createQuestionRequestDto.setQuizId(quizId);
        createQuestionRequestDto.setContent("content");

        // when
        questionService.create(createQuestionRequestDto);

        // then
        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository, times(1)).save(captor.capture());

        Question question = captor.getValue();
        assertThat(question).isNotNull();
        assertThat(question.getQuiz()).isEqualTo(quiz);
        assertThat(quiz.getQuestions().getFirst()).isEqualTo(question);
        assertThat(question.getContent()).isEqualTo(createQuestionRequestDto.getContent());
    }

    @Test
    @DisplayName("Question 수정")
    void updateQuestion() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("title", member);
        Question question = Question.create(quiz, "content");
        Long questionId = 1L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        UpdateQuestionRequestDto updateQuestionRequestDto = new UpdateQuestionRequestDto();
        updateQuestionRequestDto.setQuestionId(questionId);
        updateQuestionRequestDto.setContent("newContent");

        // when
        questionService.updateQuestion(updateQuestionRequestDto);

        // then
        assertThat(question.getContent()).isEqualTo(updateQuestionRequestDto.getContent());
    }

    @Test
    @DisplayName("Question 삭제")
    void delete() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("title", member);
        Question question = Question.create(quiz, "content");
        Long questionId = 1L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // when
        questionService.delete(questionId);

        // then
        assertThat(question.isDeleted()).isTrue();
    }
}