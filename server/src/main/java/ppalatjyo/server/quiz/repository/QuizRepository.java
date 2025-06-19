package ppalatjyo.server.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ppalatjyo.server.quiz.domain.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
