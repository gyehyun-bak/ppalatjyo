import { Button } from '@heroui/react';
import type { QuestionResponse } from '../../api/types/quiz/QuestionResponse';
import { useNavigate, useParams } from 'react-router';

interface QuestionItemProps {
    question: QuestionResponse;
}

export default function QuestionItem({ question }: QuestionItemProps) {
    const { quizId } = useParams();
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/quizzes/${quizId}/questions/${question.id}`);
    };

    return (
        <Button onPress={handleClick} data-testid={`question-${question.id}`}>
            <p>{question.content}</p>
        </Button>
    );
}
