package ppalatjyo.server.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.quiz.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
