import { addToast, Button, Form } from '@heroui/react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router';
import Input from '../../components/common/Input';
import { useEffect, useState } from 'react';
import { editQuestion, getQuestion } from '../../api/question.api';
import { X } from 'lucide-react';
import type { EditQuestionRequest } from '../../api/types/question/EditQuestionRequest';

export default function EditQuestionPage() {
    const navigate = useNavigate();
    const [content, setContent] = useState('');
    const [answer, setAnswer] = useState('');
    const [answers, setAnswers] = useState<string[]>([]);
    const { quizId, questionId } = useParams<{
        quizId: string;
        questionId: string;
    }>();

    const { data, isSuccess } = useQuery({
        queryKey: ['question', questionId],
        queryFn: () =>
            getQuestion(quizId as string, questionId as string, true),
        enabled: !!quizId && !!questionId,
    });

    const { mutate: editMutate, isPending: editIsPending } = useMutation({
        mutationFn: (data: EditQuestionRequest) =>
            editQuestion(quizId as string, questionId as string, data),
        onSuccess: () => {
            navigate(`/quizzes/${quizId}/questions/${questionId}`);
        },
    });

    useEffect(() => {
        if (isSuccess && data) {
            setContent(data.content);
            setAnswers(data.answers.map((answer) => answer.content));
        }
    }, [data, isSuccess]);

    const handleAddAnswer = () => {
        if (answer.trim() === '') return;
        setAnswers((prev) => [...prev, answer.trim()]);
        setAnswer('');
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        if (answers.length == 0) {
            addToast({ title: '최소 1개의 답이 있어야 합니다.' });
            return;
        }

        const data: EditQuestionRequest = {
            content,
            answers,
        };

        editMutate(data);
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Input
                isRequired
                value={content}
                onValueChange={setContent}
                data-testid="content-input"
            />
            <Input
                value={answer}
                onValueChange={setAnswer}
                data-testid="answer-input"
            />
            <Button onPress={handleAddAnswer} data-testid="add-answer-button">
                추가
            </Button>
            <ul>
                {answers.map((answer, index) => (
                    <li key={index}>
                        <p>{answer}</p>
                        <Button
                            type="button"
                            onPress={() =>
                                setAnswers(
                                    answers.filter((_, i) => i !== index)
                                )
                            }
                        >
                            <X />
                        </Button>
                    </li>
                ))}
            </ul>
            <Button
                isLoading={editIsPending}
                type="submit"
                data-testid="save-button"
            >
                저장하기
            </Button>
        </Form>
    );
}
