package ppalatjyo.server.domain.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.domain.quiz.domain.Answer;
import ppalatjyo.server.domain.quiz.domain.Question;
import ppalatjyo.server.domain.quiz.dto.CreateAnswerRequestDto;
import ppalatjyo.server.domain.quiz.dto.UpdateAnswerRequestDto;
import ppalatjyo.server.domain.quiz.exception.AnswerNotFoundException;
import ppalatjyo.server.domain.quiz.exception.QuestionNotFoundException;
import ppalatjyo.server.domain.quiz.repository.AnswerRepository;
import ppalatjyo.server.domain.quiz.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void create(CreateAnswerRequestDto requestDto) {
        Question question = questionRepository.findById(requestDto.getQuestionId()).orElseThrow(QuestionNotFoundException::new);
        Answer answer = Answer.createAnswer(question, requestDto.getContent());
        answerRepository.save(answer);
    }

    public void update(UpdateAnswerRequestDto requestDto) {
        Answer answer = answerRepository.findById(requestDto.getAnswerId()).orElseThrow(AnswerNotFoundException::new);
        answer.changeContent(requestDto.getContent());
    }

    public void delete(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        answer.delete();
    }
}
