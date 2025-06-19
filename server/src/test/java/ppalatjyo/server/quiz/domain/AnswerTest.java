package ppalatjyo.server.quiz.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    @Test
    @DisplayName("Answer 생성")
    void createAnswer() {
        // given
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");

        // when
        Answer answer = Answer.createAnswer(content, question);

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
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(content, question);

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
        User member = User.createMember("user", "", "");
        Quiz quiz = Quiz.createQuiz("quiz", member);
        String content = "answer";
        Question question = Question.create(quiz, "question");
        Answer answer = Answer.createAnswer(content, question);

        // when
        answer.delete();

        // then
        assertThat(answer.isDeleted()).isTrue();
    }
}