import { Button, Card, CardBody } from '@heroui/react';
import { X } from 'lucide-react';

interface AnswerItemProps {
    answer: string;
    index: number;
    deletable?: boolean;
    deleteAnswer?: (index: number) => void;
}

export default function AnswerItem({
    answer,
    index,
    deletable = true,
    deleteAnswer,
}: AnswerItemProps) {
    return (
        <Card>
            <CardBody>
                <p>{answer}</p>
                {deletable && (
                    <Button
                        type="button"
                        data-testid="delete-answer-button"
                        onPress={() => deleteAnswer?.(index)}
                    >
                        <X />
                    </Button>
                )}
            </CardBody>
        </Card>
    );
}
