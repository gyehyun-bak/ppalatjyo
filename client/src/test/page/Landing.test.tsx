import userEvent from "@testing-library/user-event";
import Landing from "../../page/Landing";
import { renderWithWrapper } from "../utils/renderWithWrapper";
import "@testing-library/jest-dom";
import { screen, waitFor } from "@testing-library/react";
import { it, expect, describe } from "vitest";

describe("Landing", () => {
    afterEach(() => {
        vi.resetAllMocks();
    });

    it("닉네임 인풋이 렌더링 된다", () => {
        // given
        renderWithWrapper(<Landing />);

        // when
        const input = screen.getByLabelText("닉네임");

        // then
        expect(input).toBeInTheDocument();
    });

    it("계속하기 버튼이 렌더링 된다", () => {
        // given
        renderWithWrapper(<Landing />);

        // when
        const button = screen.getByRole("button", { name: "계속하기" });

        // then
        expect(button).toBeInTheDocument();
    });

    it('닉네임 입력 후 "계속하기" 클릭 시 게스트 가입 요청을 보낸다', async () => {
        // given
        renderWithWrapper(<Landing />);

        const input = screen.getByLabelText("닉네임");
        const button = screen.getByRole("button", { name: "계속하기" });

        const user = userEvent.setup();

        // when
        await user.type(input, "hello");
        await user.click(button);

        // then
        await waitFor(() => {
            // expect(reactQuery.useMutation).toHaveBeenCalledWith({ nickname: "hello" });
        });
    });

    it("가입 성공 시 accessToken을 받고 /home으로 이동한다", async () => {
        // given
        // when
        // then
    });

    it("오류 응답이 반환되면 오류 토스트를 띄운다", async () => {
        // given
        // when
        // then
    });
});
