package ppalatjyo.server.quiz;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuizTest {

    @Test
    @DisplayName("Quiz 생성")
    void createQuiz() {
        // given
        String name = "quiz";

        // when
        Quiz quiz = Quiz.createQuiz(name);

        // then
        assertThat(quiz.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Quiz 이름 수정")
    void changeName() {
        // given
        Quiz quiz = Quiz.createQuiz("oldName");
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
        Quiz quiz = Quiz.createQuiz("quiz");

        Answer answer1 = Answer.createAnswer("answer1");
        Answer answer2 = Answer.createAnswer("answer2");
        Question question = Question.create(quiz, "content", answer1, answer2);

        // when
        quiz.addQuestion(question);

        // then
        List<Question> questions = quiz.getQuestions();
        assertThat(questions.size()).isEqualTo(1);
        assertThat(questions.get(0)).isEqualTo(question);
        assertThat(questions.get(0).getAnswers().size()).isEqualTo(2);
    }
}