import { Button, Card, CardBody } from '@heroui/react';
import type { AnswerResponse } from '../api/types/quiz/AnswerResponse';

interface AnswerItemProps {
    answer: AnswerResponse;
    deleteAnswer: (answer: AnswerResponse) => void;
}

export default function AnswerItem({ answer, deleteAnswer }: AnswerItemProps) {
    return (
        <Card>
            <CardBody>
                <p>{answer.content}</p>
                <Button
                    type="button"
                    data-testid="delete-answer-button"
                    onPress={() => deleteAnswer(answer)}
                >
                    <X />
                </Button>
            </CardBody>
        </Card>
    );
}
