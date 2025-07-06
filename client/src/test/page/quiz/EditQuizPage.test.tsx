import { http, HttpResponse } from "msw";
import { baseUrl, server } from "../../../mocks/server";
import {
    mockNavigate,
    mockUseParams,
} from "../../../../__mocks__/react-router";
import type { QuizResponse } from "../../../api/types/quiz/QuizResponse";
import type { QuizVisibility } from "../../../api/types/quiz/QuizVisibility";
import { renderWithWrapper } from "../../utils/renderWithWrapper";
import EditQuizPage from "../../../page/quiz/EditQuizPage";
import { screen, waitFor } from "@testing-library/dom";
import userEvent from "@testing-library/user-event";
import type { EditQuizRequest } from "../../../api/types/quiz/EditQuizRequest";

vi.mock("react-router");

describe("EditQuizPage", () => {
    const quizId = 123;
    const user = userEvent.setup();

    beforeEach(() => {
        mockUseParams.mockReturnValue({ quizId });
    });

    it("기존 퀴즈 데이터를 받아와 입력창에 표시합니다", async () => {
        // given
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

        // when
        renderWithWrapper(<EditQuizPage />);

        // then
        const titleInput = await screen.findByTestId("title-input");
        const descriptionInput = await screen.findByTestId("description-input");
        const publicRadio = await screen.findByTestId("public-radio");
        const privateRadio = await screen.findByTestId("private-radio");

        await waitFor(() => {
            expect(titleInput).toHaveValue(title);
            expect(descriptionInput).toHaveValue(description);
            expect(privateRadio).toHaveAttribute("data-selected", "true");
            expect(publicRadio).not.toHaveAttribute("data-selected", "true");
        });
    });

    it('"저장하기"를 클릭하면 업데이트를 요청하고 성공 시 "퀴즈 상세 보기" 페이지로 이동합니다', async () => {
        // given
        server.use(
            http.put<never, EditQuizRequest, QuizResponse>(
                `${baseUrl}/quizzes/${quizId}`,
                () =>
                    HttpResponse.json({
                        id: quizId,
                        title: "",
                        authorNickname: "",
                        description: "",
                        totalQuestions: 0,
                        visibility: "PUBLIC",
                        questions: [],
                    })
            )
        );

        renderWithWrapper(<EditQuizPage />);

        const titleInput = await screen.findByTestId("title-input");
        const descriptionInput = await screen.findByTestId("description-input");
        const publicRadio = await screen.findByTestId("public-radio");
        await user.type(titleInput, "제목");
        await user.type(descriptionInput, "설명");
        await user.click(publicRadio);

        const saveButton = await screen.findByTestId("save-button");

        // when
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toBeCalledWith(`/quizzes/${quizId}`);
        });
    });

    it('"삭제하기" 버튼을 클릭하면 경고 모달이 표시됩니다', async () => {
        // given
        renderWithWrapper(<EditQuizPage />);
        const deleteButton = await screen.findByTestId("delete-button");

        // when
        await user.click(deleteButton);

        // then
        expect(
            await screen.findByTestId("confirm-delete-button")
        ).toBeInTheDocument();
    });

    it('경고 모달의 "삭제하기" 버튼을 클릭하면 삭제를 요청하고 성공 시 "퀴즈 목록" 페이지로 이동합니다', async () => {
        // given
        server.use(
            http.delete(`${baseUrl}/quizzes/${quizId}`, () =>
                HttpResponse.text("삭제 완료")
            )
        );

        renderWithWrapper(<EditQuizPage />);

        const deleteButton = await screen.findByTestId("delete-button");
        await user.click(deleteButton);
        const confirmDeleteButton = await screen.findByTestId(
            "confirm-delete-button"
        );

        // when
        await user.click(confirmDeleteButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toBeCalledWith("/quizzes");
        });
    });
});
