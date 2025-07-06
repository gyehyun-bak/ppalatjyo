import userEvent from "@testing-library/user-event";
import {
    mockNavigate,
    mockUseParams,
} from "../../../../__mocks__/react-router";
import GitHubCallbackPage from "../../../page/auth/GitHubCallbackPage";
import { renderWithWrapper } from "../../utils/renderWithWrapper";
import { screen, waitFor } from "@testing-library/dom";
import { baseUrl, server } from "../../../mocks/server";
import { http, HttpResponse } from "msw";
import type { JoinAsMemberByGitHubRequest } from "../../../api/types/auth/JoinAsMemberByGitHubRequest";
import type { JoinAsMemberByGitHubResponse } from "../../../api/types/auth/JoinAsMemberByGitHubResponse";

vi.mock("react-router");

describe("GitHubCallbackPage", () => {
    const code = "code1234";

    beforeEach(() => {
        mockUseParams.mockReturnValue({ code });
    });

    it("닉네임을 입력하고 계속하기를 누르면 code, nickname으로 GitHub으로 가입을 요청하고, 성공 시 홈으로 이동한다", async () => {
        // given
        const mockAccessToken = "mock-access-token";

        server.use(
            http.post<
                never,
                JoinAsMemberByGitHubRequest,
                JoinAsMemberByGitHubResponse
            >(baseUrl + "/auth/sign-up/github", async () => {
                return HttpResponse.json({
                    accessToken: mockAccessToken,
                });
            })
        );

        renderWithWrapper(<GitHubCallbackPage />);
        const user = userEvent.setup();
        const nicknameInput = screen.getByTestId("nickname-input");
        const continueButton = screen.getByTestId("continue-button");

        // when
        await user.type(nicknameInput, "nickname123");
        await user.click(continueButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/home");
            expect(localStorage.getItem("accessToken")).toBe(mockAccessToken);
        });
    });
});
