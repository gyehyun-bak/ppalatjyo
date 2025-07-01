package ppalatjyo.server.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.quiz.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
