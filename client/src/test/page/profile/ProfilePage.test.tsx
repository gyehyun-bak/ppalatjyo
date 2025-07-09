import { screen, waitFor } from "@testing-library/dom";
import { http, HttpResponse } from "msw";
import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";
import { mockNavigate } from "../../../../__mocks__/react-router";
import type { UserResponse } from "../../../api/types/user/UserResponse";
import { server, baseUrl } from "../../../mocks/server";
import ProfilePage from "../../../page/profile/ProfilePage";
import { renderWithWrapper } from "../../utils/renderWithWrapper";

vi.mock("react-router");

describe("ProfilePage", () => {
    it("사용자 데이터를 불러와 표시한다", async () => {
        // given
        server.use(
            http.get<never, never, UserResponse>(
                baseUrl + "/users/me",
                async () => {
                    return HttpResponse.json({
                        id: 1,
                        nickname: "user1",
                    });
                }
            )
        );

        // when
        renderWithWrapper(<ProfilePage />);

        // then
        await waitFor(() => {
            expect(screen.getByText(/user1/)).toBeInTheDocument();
        });
    });

    it("<닉네임 변경 버튼>을 누르면 닉네임 변경 페이지로 이동한다", async () => {
        // given
        renderWithWrapper(<ProfilePage />);
        const editNicknameButton = screen.getByLabelText("edit-nickname");
        const user = userEvent.setup();

        // when
        await user.click(editNicknameButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/profile/edit");
        });
    });

    it("<로그아웃 버튼>을 누르면 로그아웃 페이지로 이동한다", async () => {
        // given
        renderWithWrapper(<ProfilePage />);
        const logOutButton = screen.getByLabelText("log-out");
        const user = userEvent.setup();

        // when
        await user.click(logOutButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/logout");
        });
    });
});
