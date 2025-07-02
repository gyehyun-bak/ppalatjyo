import { addToast, Button, NumberInput } from '@heroui/react';
import Input from '../../components/common/Input';
import { useLobbyCreateStore } from '../../store/useLobbyCreateStore';
import { useState } from 'react';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useNavigate, useSearchParams } from 'react-router';
import { getQuiz } from '../../api/quiz.api';
import QuizItem from '../../components/quiz/QuizItem';
import { createLobby } from '../../api/lobby.api';
import type { CreateLobbyRequestDto } from '../../types/api/lobby/CreateLobbyRequestDto';

export default function CreateLobbyPage() {
    const store = useLobbyCreateStore();
    const navigate = useNavigate();

    const [name, setName] = useState(store.name);
    const [password, setPassword] = useState('');
    const [maxUsers, setMaxUsers] = useState(store.maxUsers);
    const [minPerGame, setMinPerGame] = useState(store.minPerGame);
    const [secPerQuestion, setSecPerQuestion] = useState(store.secPerQuestion);

    const [searchParams] = useSearchParams();
    const quizId = searchParams.get('quizId');

    const { data, isLoading, isSuccess } = useQuery({
        enabled: quizId !== null,
        queryKey: ['quiz', quizId],
        queryFn: () => getQuiz(Number(quizId)),
    });

    const { mutate } = useMutation({
        mutationFn: (data: CreateLobbyRequestDto) => createLobby(data),
        onSuccess: (data) => {
            navigate(`/lobbies/${data.id}`);
        },
        onError: (error) => {
            console.error('로비 생성 중 오류 발생:', error);
            addToast({ title: '로비 생성에 실패했습니다.', color: 'danger' });
        },
    });

    const handleSelectQuiz = () => {
        store.setName(name);
        store.setMaxUsers(maxUsers);
        store.setMinPerGame(minPerGame);
        store.setSecPerQuestion(secPerQuestion);

        navigate('/quizzes');
    };

    const isFormValid =
        name.trim() !== '' &&
        maxUsers > 0 &&
        minPerGame > 0 &&
        secPerQuestion > 0;

    const handleCreateLobby = async () => {
        if (!isFormValid || quizId === null) {
            return;
        }

        mutate({
            name,
            password,
            maxUsers,
            minPerGame,
            secPerQuestion,
            quizId,
        });
    };

    return (
        <form>
            <Input
                required
                label={'로비 이름'}
                value={name}
                onValueChange={setName}
            />
            <Input
                label={'비밀번호'}
                value={password}
                onValueChange={setPassword}
            />
            <NumberInput
                label={'최대 인원'}
                minValue={1}
                maxValue={100}
                value={maxUsers}
                onValueChange={setMaxUsers}
            />
            <NumberInput
                label={'게임 시간 (분)'}
                minValue={1}
                maxValue={60}
                value={minPerGame}
                onValueChange={setMinPerGame}
            />
            <NumberInput
                label={'문제당 제한 시간 (초)'}
                minValue={1}
                maxValue={300}
                value={secPerQuestion}
                onValueChange={setSecPerQuestion}
            />
            {isSuccess ? (
                <QuizItem quiz={data} />
            ) : (
                <Button
                    aria-label="select-quiz"
                    isLoading={isLoading}
                    onPress={handleSelectQuiz}
                >
                    퀴즈 선택하기
                </Button>
            )}
            <Button onPress={handleCreateLobby} isDisabled={!isFormValid}>
                로비 만들기
            </Button>
        </form>
    );
}
