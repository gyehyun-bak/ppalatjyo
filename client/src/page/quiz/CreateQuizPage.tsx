import { Button, Form, Radio, RadioGroup, Textarea } from "@heroui/react";
import { useState } from "react";
import Input from "../../components/common/Input";
import { useMutation } from "@tanstack/react-query";
import { createQuiz } from "../../api/quiz.api";
import type { CreateQuizRequest } from "../../api/types/quiz/CreateQuizRequest";
import { useNavigate } from "react-router";
import type { QuizVisibility } from "../../api/types/quiz/QuizVisibility";

export default function CreateQuizPage() {
    const navigate = useNavigate();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [visibility, setVisibility] = useState<QuizVisibility>("PUBLIC");

    const { mutate } = useMutation({
        mutationFn: (data: CreateQuizRequest) => createQuiz(data),
        onSuccess: (data) => {
            if (data) {
                navigate(`/quizzes/${data.id}`);
            }
        },
    });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const data: CreateQuizRequest = {
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
                data-testid="title-input"
            />
            <Textarea
                label="퀴즈 설명"
                type="text"
                value={description}
                onValueChange={setDescription}
                data-testid="description-input"
            />
            <RadioGroup
                isRequired
                id="visibility"
                label="공개 설정"
                value={visibility}
                onValueChange={(value) =>
                    setVisibility(value as "PUBLIC" | "PRIVATE")
                }
                data-testid="visibility-radio"
            >
                <Radio
                    value="PUBLIC"
                    description="누구나 퀴즈를 조회하고 로비를 만들 수 있습니다."
                    data-testid="public-radio"
                >
                    공개
                </Radio>
                <Radio
                    value="PRIVATE"
                    description="작성자만 퀴즈를 조회하고 로비를 만들 수 있습니다."
                    data-testid="private-radio"
                >
                    비공개
                </Radio>
            </RadioGroup>
            <Button type="submit">저장하기</Button>
        </Form>
    );
}
