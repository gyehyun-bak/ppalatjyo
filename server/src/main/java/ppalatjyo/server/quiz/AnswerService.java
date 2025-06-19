package ppalatjyo.server.quiz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ppalatjyo.server.quiz.domain.Answer;
import ppalatjyo.server.quiz.domain.Question;
import ppalatjyo.server.quiz.dto.AnswerCreateRequestDto;
import ppalatjyo.server.quiz.dto.AnswerUpdateRequestDto;
import ppalatjyo.server.quiz.repository.AnswerRepository;
import ppalatjyo.server.quiz.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    public void create(AnswerCreateRequestDto requestDto) {
        Question question = questionRepository.findById(requestDto.getQuestionId()).orElseThrow();
        Answer answer = Answer.createAnswer(requestDto.getContent(), question);
        answerRepository.save(answer);
    }

    public void update(AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(requestDto.getAnswerId()).orElseThrow();
        answer.changeContent(requestDto.getContent());
    }

    public void delete(Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow();
        answer.delete();
    }
}
