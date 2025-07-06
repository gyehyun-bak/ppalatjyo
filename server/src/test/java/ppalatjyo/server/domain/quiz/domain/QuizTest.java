package ppalatjyo.server.domain.quiz.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.exception.GuestCannotCreateQuizException;
import ppalatjyo.server.domain.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTest {

    @Test
    @DisplayName("Quiz 생성")
    void createQuiz() {
        // given
        String name = "quiz";
        User member = User.createMember("author", "test@email.com", "google");
        String description = "description";
        QuizVisibility visibility = QuizVisibility.PUBLIC;

        // when
        Quiz quiz = Quiz.createQuiz(name, member, description, visibility);

        // then
        assertThat(quiz.getTitle()).isEqualTo(name);
        assertThat(quiz.getUser()).isEqualTo(member);
        assertThat(quiz.getDescription()).isEqualTo(description);
        assertThat(quiz.getVisibility()).isEqualTo(visibility);
    }

    @Test
    @DisplayName("Quiz 생성 - Guest가 생성 시도 시 예외 발생")
    void createQuizExceptionWhenGuest() {
        // given
        String name = "quiz";
        User user = User.createGuest("guest");

        // when
        // then
        Assertions.assertThatThrownBy(() -> Quiz.createQuiz(name, user))
                .isInstanceOf(GuestCannotCreateQuizException.class);
    }

    @Test
    @DisplayName("Quiz 이름 수정")
    void changeTitle() {
        // given
        Quiz quiz = Quiz.createQuiz("oldName", User.createMember("n", "e", "p"));
        String name = "newName";

        // when
        quiz.changeTitle(name);

        // then
        assertThat(quiz.getTitle()).isEqualTo(name);
    }

    @Test
    @DisplayName("문제 추가")
    void addQuestion() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));
        Question question = Question.create(quiz, "content");
        Answer.createAnswer(question, "answer1");
        Answer.createAnswer(question, "answer2");


        // when
        quiz.addQuestion(question);

        // then
        List<Question> questions = quiz.getQuestions();
        assertThat(questions.size()).isEqualTo(1);
        assertThat(questions.getFirst()).isEqualTo(question);
        assertThat(questions.getFirst().getAnswers().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("문제 삭제")
    void delete() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));

        // when
        quiz.delete();

        // then
        assertThat(quiz.isDeleted()).isTrue();
    }
}