import {
    addToast,
    Button,
    Form,
    Modal,
    ModalBody,
    ModalContent,
    ModalFooter,
    ModalHeader,
    useDisclosure,
} from '@heroui/react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router';
import Input from '../../components/common/Input';
import { useEffect, useState } from 'react';
import {
    deleteQuestion,
    editQuestion,
    getQuestion,
} from '../../api/question.api';
import { X } from 'lucide-react';
import type { EditQuestionRequest } from '../../api/types/question/EditQuestionRequest';
import AnswerItem from '../../components/AnswerItem';

export default function EditQuestionPage() {
    const navigate = useNavigate();
    const [content, setContent] = useState('');
    const [answer, setAnswer] = useState('');
    const [answers, setAnswers] = useState<string[]>([]);
    const { quizId, questionId } = useParams<{
        quizId: string;
        questionId: string;
    }>();
    const { isOpen, onOpen, onOpenChange } = useDisclosure();

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

    const { mutate: deleteMutate, isPending: deleteIsPending } = useMutation({
        mutationFn: () =>
            deleteQuestion(quizId as string, questionId as string),
        onSuccess: () => {
            navigate(`/quizzes/${quizId}`);
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

    const deleteAnswer = (index: number) => {
        setAnswers(answers.filter((_, i) => i !== index));
    };

    return (
        <>
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
                <Button
                    onPress={handleAddAnswer}
                    data-testid="add-answer-button"
                >
                    추가
                </Button>
                <ul>
                    {answers.map((answer, index) => (
                        <AnswerItem
                            key={index}
                            answer={answer}
                            deleteAnswer={deleteAnswer}
                        />
                    ))}
                </ul>
                <Button
                    type="button"
                    data-testid="delete-button"
                    onPress={onOpen}
                >
                    삭제하기
                </Button>
                <Button
                    isLoading={editIsPending}
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
                            <ModalHeader>질문 삭제하기</ModalHeader>
                            <ModalBody>
                                <p>
                                    정말로 삭제하시겠습니까? 삭제 이후에는
                                    되돌릴 수 없습니다.
                                </p>
                            </ModalBody>
                            <ModalFooter>
                                <Button
                                    isLoading={deleteIsPending}
                                    onPress={onClose}
                                >
                                    취소하기
                                </Button>
                                <Button
                                    isLoading={deleteIsPending}
                                    color="danger"
                                    onPress={() => deleteMutate()}
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
