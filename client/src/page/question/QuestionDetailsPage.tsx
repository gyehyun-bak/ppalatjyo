import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router';
import { getQuestion } from '../../api/question.api';
import AnswerItem from '../../components/AnswerItem';

export default function QuestionDetailsPage() {
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

    return (
        <>
            {isSuccess && (
                <div>
                    <p>{data.content}</p>
                    <ul>
                        {data.answers.map((answer, index) => (
                            <AnswerItem
                                answer={answer.content}
                                index={index}
                                deletable={false}
                            />
                        ))}
                    </ul>
                </div>
            )}
        </>
    );
}
