package ppalatjyo.server.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.domain.Quiz;
import ppalatjyo.server.quiz.dto.QuestionCreateRequestDto;
import ppalatjyo.server.quiz.dto.QuestionUpdateRequestDto;
import ppalatjyo.server.quiz.exception.QuestionNotFoundException;
import ppalatjyo.server.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.quiz.repository.QuestionRepository;
import ppalatjyo.server.quiz.repository.QuizRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public void create(QuestionCreateRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(requestDto.getQuizId()).orElseThrow(QuizNotFoundException::new);
        Question question = Question.create(quiz, requestDto.getContent());
        questionRepository.save(question);
    }

    public void updateQuestion(QuestionUpdateRequestDto requestDto) {
        Question question = questionRepository.findById(requestDto.getQuestionId()).orElseThrow(QuestionNotFoundException::new);

        String content = requestDto.getContent();
        if (!content.isBlank()) {
            question.changeContent(content);
        }
    }

    public void delete(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        question.delete();
    }
}
