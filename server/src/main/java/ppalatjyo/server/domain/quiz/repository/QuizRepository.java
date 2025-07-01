package ppalatjyo.server.domain.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.domain.quiz.domain.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
