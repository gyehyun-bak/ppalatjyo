package ppalatjyo.server.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.quiz.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
