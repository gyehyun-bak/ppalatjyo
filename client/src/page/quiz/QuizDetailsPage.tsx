import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router';
import { getQuiz } from '../../api/quiz.api';
import QuestionItem from '../../components/quiz/QuestionItem';

export default function QuizDetailsPage() {
    const { quizId } = useParams<{ quizId: string }>();

    const { data, isSuccess } = useQuery({
        queryKey: ['quiz', quizId],
        queryFn: () => getQuiz(quizId as string, true),
        enabled: !!quizId,
    });

    return (
        <div>
            {isSuccess && (
                <div>
                    <div>
                        <h1>{data.authorNickname}</h1>
                        <p>{data.title}</p>
                        <p>{data.description}</p>
                        <p>{data.totalQuestions}</p>
                    </div>
                    <ul>
                        {data.questions.map((question) => (
                            <QuestionItem
                                key={question.id}
                                question={question}
                            />
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}
