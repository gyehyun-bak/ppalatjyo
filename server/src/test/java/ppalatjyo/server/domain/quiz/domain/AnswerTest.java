package ppalatjyo.server.domain.quiz.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("Answer 생성")
    void createAnswer() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");

        // when
        Answer answer = Answer.createAnswer(question, content);

        // then
        assertThat(answer).isNotNull();
        assertThat(answer.getContent()).isEqualTo(content);
        assertThat(answer.getQuestion()).isEqualTo(question);
        assertThat(question.getAnswers()).containsExactly(answer);
    }

    @Test
    @DisplayName("content 변경")
    void changeContent() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, content);

        String newContent = "newContent";

        // when
        answer.changeContent(newContent);

        // then
        assertThat(answer.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("Answer 삭제")
    void delete() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, content);

        // when
        answer.delete();

        // then
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("정답 확인 - 정답")
    void isCorrect() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, content);

        // when
        boolean isCorrect = answer.isCorrect(content);

        // then
        assertThat(isCorrect).isTrue();
    }

    @Test
    @DisplayName("정답 확인 - 오답")
    void isCorrectFalse() {
        // given
        User member = User.createMember("user", "", null);
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(question, content);

        String wrongAnswer = "wrongAnswer";

        // when
        boolean isCorrect = answer.isCorrect(wrongAnswer);

        // then
        assertThat(isCorrect).isFalse();
    }
}