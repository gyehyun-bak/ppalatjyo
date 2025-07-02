import { Button, Form, Radio, RadioGroup, Textarea } from '@heroui/react';
import { useState } from 'react';
import Input from '../../components/common/Input';
import { useMutation } from '@tanstack/react-query';
import { createQuiz } from '../../api/quiz.api';
import type { CreateQuizRequestDto } from '../../types/api/quiz/CreateQuizRequestDto';
import { useNavigate } from 'react-router';

export default function CreateQuizPage() {
    const navigate = useNavigate();

    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [visibility, setVisibility] = useState<'PUBLIC' | 'PRIVATE'>(
        'PUBLIC'
    );

    const { mutate } = useMutation({
        mutationFn: (data: CreateQuizRequestDto) => createQuiz(data),
        onSuccess: (data) => {
            if (data) {
                navigate(`/quizzes/${data.id}`);
            }
        },
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const data: CreateQuizRequestDto = {
            title,
            description,
            visibility,
        };

        mutate(data);
    };

    return (
        <Form onSubmit={handleSubmit}>
            <Input
                isRequired
                type="text"
                label="퀴즈 이름"
                value={title}
                onValueChange={setTitle}
            />
            <Textarea
                label="퀴즈 설명"
                type="text"
                value={description}
                onValueChange={setDescription}
            />
            <RadioGroup
                isRequired
                id="visibility"
                label="공개 설정"
                value={visibility}
                onValueChange={(value) =>
                    setVisibility(value as 'PUBLIC' | 'PRIVATE')
                }
            >
                <Radio
                    value="PUBLIC"
                    description="누구나 퀴즈를 조회하고 로비를 만들 수 있습니다."
                >
                    공개
                </Radio>
                <Radio
                    value="PRIVATE"
                    description="작성자만 퀴즈를 조회하고 로비를 만들 수 있습니다."
                >
                    비공개
                </Radio>
            </RadioGroup>
            <Button type="submit">저장하기</Button>
        </Form>
    );
}
