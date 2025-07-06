import { http, HttpResponse } from "msw";
import { baseUrl, server } from "../../../mocks/server";
import { mockUseParams } from "../../../../__mocks__/react-router";
import type { QuizResponse } from "../../../api/types/quiz/QuizResponse";
import type { QuizVisibility } from "../../../api/types/quiz/QuizVisibility";

vi.mock("react-router");

describe("EditQuizPage", () => {
    const quizId = 123;

    beforeEach(() => {
        mockUseParams.mockReturnValue({ quizId });
    });

    it("기존 퀴즈 데이터를 받아와 입력창에 표시합니다", async () => {
        const title = "title1234";
        const description = "description1234";
        const visibility: QuizVisibility = "PRIVATE";

        server.use(
            http.get<never, never, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}`,
                () =>
                    HttpResponse.json({
                        id: quizId,
                        title: title,
                        authorNickname: "",
                        description: description,
                        totalQuestions: 0,
                        questions: [],
                        visibility: visibility,
                    })
            )
        );
    });
    it('"저장하기"를 클릭하면 업데이트를 요청하고 성공 시 "퀴즈 상세 보기" 페이지로 이동합니다', async () => {});
    it('"삭제하기" 버튼을 클릭하면 경고 모달이 표시됩니다', async () => {});
    it('경고 모달의 "삭제하기" 버튼을 클릭하면 삭제를 요청하고 성공 시 "퀴즈 목록" 페이지로 이동합니다', async () => {});
});
