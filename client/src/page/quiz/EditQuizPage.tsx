import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router";
import { editQuiz, getQuiz } from "../../api/quiz.api";
import { Button, Form, Radio, RadioGroup, Textarea } from "@heroui/react";
import Input from "../../components/common/Input";
import React, { useEffect, useState } from "react";
import type { QuizVisibility } from "../../api/types/quiz/QuizVisibility";
import type { EditQuizRequest } from "../../api/types/quiz/EditQuizRequest";

export default function EditQuizPage() {
    const { quizId } = useParams<{ quizId: string }>();
    const navigate = useNavigate();
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [visibility, setVisibility] = useState<QuizVisibility>("PRIVATE");

    const { data, isSuccess } = useQuery({
        queryKey: ["quiz", quizId],
        queryFn: () => getQuiz(quizId as string, true),
        enabled: !!quizId,
    });

    const { mutate, isPending } = useMutation({
        mutationFn: (data: EditQuizRequest) => editQuiz(data),
        onSuccess: () => {
            navigate(`/quizzes/${quizId}`);
        },
    });

    useEffect(() => {
        if (isSuccess) {
            setTitle(data.title);
            setDescription(data.description);
            setVisibility(data.visibility);
        }
    }, [isSuccess, data]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!quizId) return;
        const data: EditQuizRequest = {
            id: quizId,
            title: title,
            description: description,
            visibility: visibility,
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
