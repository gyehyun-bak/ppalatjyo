package ppalatjyo.server.quiz;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.dto.QuestionCreateRequestDto;
import ppalatjyo.server.quiz.dto.QuestionUpdateRequestDto;
import ppalatjyo.server.quiz.repository.QuestionRepository;
import ppalatjyo.server.quiz.repository.QuizRepository;
import ppalatjyo.server.user.domain.User;

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
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("title", member);

        Long quizId = 1L;
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        QuestionCreateRequestDto questionCreateRequestDto = new QuestionCreateRequestDto();
        questionCreateRequestDto.setQuizId(quizId);
        questionCreateRequestDto.setContent("content");

        // when
        questionService.create(questionCreateRequestDto);

        // then
        ArgumentCaptor<Question> captor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository, times(1)).save(captor.capture());

        Question question = captor.getValue();
        assertThat(question).isNotNull();
        assertThat(question.getQuiz()).isEqualTo(quiz);
        assertThat(quiz.getQuestions().getFirst()).isEqualTo(question);
        assertThat(question.getContent()).isEqualTo(questionCreateRequestDto.getContent());
    }

    @Test
    @DisplayName("Question 수정")
    void updateQuestion() {
        // given
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("title", member);
        Question question = Question.create(quiz, "content");
        Long questionId = 1L;

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        QuestionUpdateRequestDto questionUpdateRequestDto = new QuestionUpdateRequestDto();
        questionUpdateRequestDto.setQuestionId(questionId);
        questionUpdateRequestDto.setContent("newContent");

        // when
        questionService.updateQuestion(questionUpdateRequestDto);

        // then
        assertThat(question.getContent()).isEqualTo(questionUpdateRequestDto.getContent());
    }
}