package ppalatjyo.server.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.quiz.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
