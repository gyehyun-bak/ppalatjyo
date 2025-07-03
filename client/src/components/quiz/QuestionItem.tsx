import type { QuestionResponse } from '../../api/types/quiz/QuestionResponse';

interface QuestionItemProps {
    question: QuestionResponse;
}

export default function QuestionItem({ question }: QuestionItemProps) {
    return (
        <li>
            <p>{question.id}</p>
            <p>{question.content}</p>
        </li>
    );
}
