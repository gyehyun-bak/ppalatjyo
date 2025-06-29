import { useQuery } from "@tanstack/react-query";
import { getQuizzes } from "../api/quiz.api";
import QuizItem from "../components/quiz/QuizItem";
import { Button } from "@heroui/react";
import { useNavigate } from "react-router";

export default function QuizPage() {
    const navigate = useNavigate();

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
            <Button
                aria-label="create-quiz"
                onPress={() => navigate("/quizzes/create")}
            >
                퀴즈 만들기
            </Button>
        </div>
    );
}
