import { waitFor } from "@testing-library/dom";
import { mockNavigate } from "../../../../__mocks__/react-router";
import { useAuthStore } from "../../../store/useAuthStore";
import { renderWithWrapper } from "../../utils/renderWithWrapper";
import LogOutPage from "../../../page/auth/LogOutPage";
import { baseUrl, server } from "../../../mocks/server";
import { http, HttpResponse } from "msw";

vi.mock("react-router");
vi.mock("zustand");

describe("LogOutPage", () => {
    beforeEach(() => {
        server.use(
            http.post(`${baseUrl}/auth/log-out`, () => HttpResponse.text("ok"))
        );
    });

    it("마운트 시 useAuthStore의 현재 인증 정보를 지운다", async () => {
        // given
        const { isAuthenticated } = useAuthStore.getState();

        // when
        renderWithWrapper(<LogOutPage />);

        // then
        await waitFor(() => {
            expect(isAuthenticated).toBeFalsy();
            expect(mockNavigate).toHaveBeenCalledWith("/landing");
        });
    });

    it("마운트 시 localStorage의 엑세스토큰을 삭제한다", async () => {
        // given

        // when
        renderWithWrapper(<LogOutPage />);

        // then
        await waitFor(() => {
            expect(localStorage.getItem("accessToken")).toBeNull();
        });
    });

    it("로그아웃을 요청하고 성공 시 랜딩 페이지로 이동한다", async () => {
        // given

        // when
        renderWithWrapper(<LogOutPage />);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/landing");
        });
    });
});
