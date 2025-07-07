import { addToast, Button, Form } from '@heroui/react';
import React, { useState } from 'react';
import Input from '../../components/common/Input';
import { useMutation } from '@tanstack/react-query';
import { createQuestion } from '../../api/question.api';
import type { CreateQuestionRequest } from '../../api/types/question/CreateQuestionRequest';
import { useNavigate, useParams } from 'react-router';
import { X } from 'lucide-react';

export default function CreateQuestionPage() {
    const { quizId } = useParams<{ quizId: string }>();
    const navigate = useNavigate();
    const [content, setContent] = useState('');
    const [answer, setAnswer] = useState('');
    const [answers, setAnswers] = useState<string[]>([]);

    const { mutate, isPending } = useMutation({
        mutationFn: (data: CreateQuestionRequest) =>
            createQuestion(quizId!, data),
        onSuccess: (data) => {
            navigate(`/quizzes/${quizId}/questions/${data.id}`);
        },
    });

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

        const data: CreateQuestionRequest = {
            content,
            answers,
        };

        mutate(data);
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
                isLoading={isPending}
                type="submit"
                data-testid="save-button"
            >
                저장하기
            </Button>
        </Form>
    );
}
