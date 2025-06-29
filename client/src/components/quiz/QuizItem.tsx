import type { QuizResponseDto } from "../../types/api/quiz/QuizResponseDto";

interface QuizItemProps {
    quiz: QuizResponseDto;
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
