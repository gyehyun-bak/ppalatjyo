package ppalatjyo.server.domain.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.domain.Quiz;
import ppalatjyo.server.domain.quiz.dto.CreateQuestionRequestDto;
import ppalatjyo.server.domain.quiz.dto.UpdateQuestionRequestDto;
import ppalatjyo.server.domain.quiz.exception.QuestionNotFoundException;
import ppalatjyo.server.domain.quiz.exception.QuizNotFoundException;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;
import ppalatjyo.server.domain.quiz.repository.QuizRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public void create(CreateQuestionRequestDto requestDto) {
        Quiz quiz = quizRepository.findById(requestDto.getQuizId()).orElseThrow(QuizNotFoundException::new);
        Question question = Question.create(quiz, requestDto.getContent());
        questionRepository.save(question);
    }

    public void updateQuestion(UpdateQuestionRequestDto requestDto) {
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
