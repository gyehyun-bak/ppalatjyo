import { screen, waitFor } from "@testing-library/dom";
import EditProfilePage from "../../../page/profile/EditProfilePage";
import { renderWithWrapper } from "../../utils/renderWithWrapper";
import { baseUrl, server } from "../../../mocks/server";
import { http, HttpResponse } from "msw";
import type { UserResponse } from "../../../api/types/user/UserResponse";
import userEvent from "@testing-library/user-event";
import { mockNavigate } from "../../../../__mocks__/react-router";
import type { EditMeRequest } from "../../../api/types/user/EditMeRequest";

vi.mock("react-router");

describe("EditProfilePage", () => {
    it("닉네임을 불러와 닉네임 인풋에 표시한다", async () => {
        // given
        const nickname = "nickname1234";
        server.use(
            http.get<never, never, UserResponse>(`${baseUrl}/users/me`, () =>
                HttpResponse.json({
                    id: 123,
                    nickname: nickname,
                })
            )
        );

        // when
        renderWithWrapper(<EditProfilePage />);

        // then
        const nicknameInput = await screen.findByTestId("nickname-input");

        await waitFor(() => {
            expect(nicknameInput).toHaveValue(nickname);
        });
    });
    it("닉네임이 비어있으면 '저장하기' 버튼을 클릭해도 API를 호출하지 않는다", async () => {
        // given
        server.use(
            http.get<never, never, UserResponse>(`${baseUrl}/users/me`, () =>
                HttpResponse.json({
                    id: 123,
                    nickname: "nickname",
                })
            )
        );

        renderWithWrapper(<EditProfilePage />);
        const nicknameInput = await screen.findByTestId("nickname-input");
        const saveButton = await screen.findByTestId("save-button");
        const user = userEvent.setup();

        // when
        await user.clear(nicknameInput);
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).not.toHaveBeenCalled();
        });
    });
    it("닉네임을 입력하고 '저장하기' 버튼을 클릭하면 API를 호출하고 프로필 화면으로 이동한다", async () => {
        // given
        server.use(
            http.get<never, never, UserResponse>(`${baseUrl}/users/me`, () =>
                HttpResponse.json({
                    id: 123,
                    nickname: "nickname",
                })
            ),
            http.put<never, EditMeRequest, UserResponse>(
                `${baseUrl}/users/me`,
                () =>
                    HttpResponse.json({
                        id: 123,
                        nickname: "nickname",
                    })
            )
        );

        renderWithWrapper(<EditProfilePage />);
        const nicknameInput = await screen.findByTestId("nickname-input");
        const saveButton = await screen.findByTestId("save-button");
        const user = userEvent.setup();

        // when
        await user.type(nicknameInput, "hello");
        await user.click(saveButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/profile");
        });
    });
});
