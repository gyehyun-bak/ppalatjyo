package ppalatjyo.server.quiz.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionTest {

    @Test
    @DisplayName("Question 생성")
    void create() {
        // given
        String content = "content";
        User user = User.createMember("author", "<EMAIL>", "google");
        Quiz quiz = Quiz.createQuiz("quiz", user);

        // when
        Question question = Question.create(quiz, content);

        // then
        assertThat(question.getContent()).isEqualTo(content);
        assertThat(question.getQuiz()).isEqualTo(quiz);
        assertThat(quiz.getQuestions()).containsExactly(question);
    }

    @Test
    @DisplayName("content 변경")
    void changeContent() {
        // given
        User user = User.createMember("author", "<EMAIL>", "google");
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "content");

        String newContent = "newContent";

        // when
        question.changeContent(newContent);

        // then
        assertThat(question.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("Question 삭제")
    void delete() {
        // given
        User user = User.createMember("author", "<EMAIL>", "google");
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "content");

        // when
        question.delete();

        // then
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("정답 확인 - 정답")
    void isCorrect() {
        // given
        User user = User.createMember("author", "<EMAIL>", "google");
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "question");

        String content = "content";
        Answer.createAnswer(question, content);

        // when
        boolean isCorrect = question.isCorrect(content);

        // then
        assertThat(isCorrect).isTrue();
    }

    @Test
    @DisplayName("정답 확인 - 오답")
    void isCorrectFalse() {
        // given
        User user = User.createMember("author", "<EMAIL>", "google");
        Quiz quiz = Quiz.createQuiz("quiz", user);
        Question question = Question.create(quiz, "question");

        String content = "content";
        Answer.createAnswer(question, content);

        // when
        boolean isCorrect = question.isCorrect("wrongAnswer");

        // then
        assertThat(isCorrect).isFalse();
    }
}