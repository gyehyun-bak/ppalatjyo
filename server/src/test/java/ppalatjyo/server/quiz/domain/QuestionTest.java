package ppalatjyo.server.quiz.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    @DisplayName("Question 생성")
    void create() {
        // given
        String content = "content";
        Answer answer1 = Answer.createAnswer("answer1");
        Quiz quiz = Quiz.createQuiz("quiz");

        // when
        Question question = Question.create(quiz, content, answer1);

        // then
        assertThat(question.getContent()).isEqualTo(content);
        assertThat(question.getAnswers()).containsExactly(answer1);
        assertThat(question.getQuiz()).isEqualTo(quiz);
        assertThat(quiz.getQuestions()).containsExactly(question);
    }
}