import type { QuizResponse } from '../../api/types/quiz/QuizResponse';

interface QuizItemProps {
    quiz: QuizResponse;
}

export default function QuizItem({ quiz }: QuizItemProps) {
    return (
        <li>
            <p>{quiz.id}</p>
            <p>{quiz.title}</p>
            <p>{quiz.authorNickname}</p>
            <p>{quiz.totalQuestions}</p>
        </li>
    );
}
