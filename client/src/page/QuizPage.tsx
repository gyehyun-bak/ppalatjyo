import { useQuery } from "@tanstack/react-query";
import { getQuizzes } from "../api/quiz.api";
import QuizItem from "../components/quiz/QuizItem";

export default function QuizPage() {
    const { data } = useQuery({
        queryKey: ["quizzes"],
        queryFn: getQuizzes,
    });

    return (
        <div>
            {data && data.data && data.data.quizzes.length > 0 ? (
                <ul>
                    {data.data.quizzes.map((quiz) => (
                        <QuizItem key={quiz.id} quiz={quiz} />
                    ))}
                </ul>
            ) : (
                <div>아직 퀴즈가 없습니다...</div>
            )}
        </div>
    );
}
