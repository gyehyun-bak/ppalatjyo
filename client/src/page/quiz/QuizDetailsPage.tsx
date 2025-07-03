import { useQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router';
import { getQuiz } from '../../api/quiz.api';
import QuestionItem from '../../components/quiz/QuestionItem';
import { Button } from '@heroui/react';

export default function QuizDetailsPage() {
    const { quizId } = useParams<{ quizId: string }>();
    const navigate = useNavigate();

    const { data, isSuccess } = useQuery({
        queryKey: ['quiz', quizId],
        queryFn: () => getQuiz(quizId as string, true),
        enabled: !!quizId,
    });

    const handleCreateQuestion = () => {
        navigate(`/quizzes/${quizId}/questions/create`);
    };

    const handleEditQuiz = () => {
        navigate(`/quizzes/${quizId}/edit`);
    };

    const handleCreateLobby = () => {
        navigate(`/lobbies/create?quizId=${quizId}`);
    };

    return (
        <>
            {isSuccess && (
                <div>
                    <div>
                        <h1>{data.authorNickname}</h1>
                        <p>{data.title}</p>
                        <p>{data.description}</p>
                        <p>{data.totalQuestions}</p>
                    </div>
                    <Button
                        onPress={handleCreateQuestion}
                        data-testid={'create-question'}
                    >
                        문제 추가
                    </Button>
                    <ul>
                        {data.questions.map((question) => (
                            <QuestionItem
                                key={question.id}
                                question={question}
                            />
                        ))}
                    </ul>
                    <Button
                        onPress={handleCreateLobby}
                        data-testid={'create-lobby'}
                    >
                        로비 만들기
                    </Button>
                    <Button onPress={handleEditQuiz} data-testid={'edit-quiz'}>
                        수정하기
                    </Button>
                </div>
            )}
        </>
    );
}
