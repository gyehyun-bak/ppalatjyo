package ppalatjyo.server.quiz;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTest {

    @Test
    @DisplayName("Quiz 생성")
    void createQuiz() {
        // given
        String name = "quiz";
        User member = User.createMember("author", "test@email.com", "google");

        // when
        Quiz quiz = Quiz.createQuiz(name, member);

        // then
        assertThat(quiz.getName()).isEqualTo(name);
        assertThat(quiz.getUser()).isEqualTo(member);
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
    void changeName() {
        // given
        Quiz quiz = Quiz.createQuiz("oldName", User.createMember("n", "e", "p"));
        String name = "newName";

        // when
        quiz.changeName(name);

        // then
        assertThat(quiz.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("문제 추가")
    void addQuestion() {
        // given
        Quiz quiz = Quiz.createQuiz("quiz", User.createMember("n", "e", "p"));

        Answer answer1 = Answer.createAnswer("answer1");
        Answer answer2 = Answer.createAnswer("answer2");
        Question question = Question.create(quiz, "content", answer1, answer2);

        // when
        quiz.addQuestion(question);

        // then
        List<Question> questions = quiz.getQuestions();
        assertThat(questions.size()).isEqualTo(1);
        assertThat(questions.getFirst()).isEqualTo(question);
        assertThat(questions.getFirst().getAnswers().size()).isEqualTo(2);
    }
}