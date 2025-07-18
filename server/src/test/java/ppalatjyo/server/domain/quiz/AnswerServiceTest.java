package ppalatjyo.server.domain.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.dto.CreateAnswerRequestDto;
import ppalatjyo.server.domain.quiz.dto.UpdateAnswerRequestDto;
import ppalatjyo.server.domain.quiz.repository.AnswerRepository;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;
import ppalatjyo.server.domain.user.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;
    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private AnswerService answerService;

    @Test
    @DisplayName("정답 추가")
    void addAnswer() {
        // given
        User user = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "question");

        Long questionId = 1L;
        String content = "answer";

        CreateAnswerRequestDto requestDto = new CreateAnswerRequestDto();
        requestDto.setQuestionId(questionId);
        requestDto.setContent(content);

        when(questionRepository.findById(questionId)).thenReturn(Optional.of(question));

        // when
        answerService.create(requestDto);

        // then
        ArgumentCaptor<Answer> captor = ArgumentCaptor.forClass(Answer.class);
        verify(answerRepository, times(1)).save(captor.capture());
        Answer answer = captor.getValue();
        assertThat(answer.getQuestion()).isEqualTo(question);
        assertThat(answer.getContent()).isEqualTo(content);
        assertThat(question.getAnswers()).containsExactly(answer);
    }

    @Test
    @DisplayName("정답 수정")
    void updateAnswer() {
        // given
        User user = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, "answer");

        Long answerId = 1L;
        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        String newContent = "newContent";
        UpdateAnswerRequestDto requestDto = new UpdateAnswerRequestDto();
        requestDto.setAnswerId(answerId);
        requestDto.setContent(newContent);

        // when
        answerService.update(requestDto);

        // then
        assertThat(answer.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("정답 삭제")
    void deleteAnswer() {
        // given
        User user = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, "answer");
        Long answerId = 1L;

        when(answerRepository.findById(answerId)).thenReturn(Optional.of(answer));

        // when
        answerService.delete(answerId);

        // then
        assertThat(answer.isDeleted()).isTrue();
    }
}