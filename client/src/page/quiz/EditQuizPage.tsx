import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router";
import { deleteQuiz, editQuiz, getQuiz } from "../../api/quiz.api";
import {
    Button,
    Form,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    Radio,
    RadioGroup,
    Textarea,
    useDisclosure,
} from "@heroui/react";
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
    const { isOpen, onOpen, onOpenChange } = useDisclosure();

    const { data, isSuccess } = useQuery({
        queryKey: ["quiz", quizId],
        queryFn: () => getQuiz(quizId as string, true),
        enabled: !!quizId,
    });

    const { mutate: editMutate, isPending } = useMutation({
        mutationFn: (data: EditQuizRequest) => editQuiz(data),
        onSuccess: () => {
            navigate(`/quizzes/${quizId}`);
        },
    });

    const { mutate: deleteMutate, isPending: deletePending } = useMutation({
        mutationFn: () => deleteQuiz(quizId!),
        onSuccess: () => {
            navigate("/quizzes");
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
        editMutate(data);
    };

    const handleDelete = async () => {
        deleteMutate();
    };

    return (
        <>
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
                    type="button"
                    data-testid="delete-button"
                    onPress={onOpen}
                >
                    삭제하기
                </Button>
                <Button
                    isLoading={isPending}
                    type="submit"
                    data-testid="save-button"
                >
                    저장하기
                </Button>
            </Form>
            <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader>퀴즈 삭제하기</ModalHeader>
                            <ModalBody>
                                <p>
                                    정말로 삭제하시겠습니까? 삭제 이후에는
                                    되돌릴 수 없습니다.
                                </p>
                            </ModalBody>
                            <ModalFooter>
                                <Button
                                    isLoading={deletePending}
                                    onPress={onClose}
                                >
                                    취소하기
                                </Button>
                                <Button
                                    isLoading={deletePending}
                                    color="danger"
                                    onPress={handleDelete}
                                    data-testid="confirm-delete-button"
                                >
                                    삭제하기
                                </Button>
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </>
    );
}
