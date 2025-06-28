import userEvent from "@testing-library/user-event";
import Landing from "../../page/Landing";
import { renderWithWrapper } from "../utils/renderWithWrapper";
import "@testing-library/jest-dom";
import { screen, waitFor } from "@testing-library/react";
import { it, expect, describe } from "vitest";
import { baseUrl, server } from "../../mocks/server";
import { http, HttpResponse } from "msw";
import type { JoinAsGuestRequestDto } from "../../types/api/auth/JoinAsGuestRequestDto";
import type { ResponseDto } from "../../types/api/ResponseDto";
import type { JoinAsGuestResponseDto } from "../../types/api/auth/JoinAsGuestResponseDto";

const { mockNavigate, mockAddToast } = vi.hoisted(() => ({
    mockNavigate: vi.fn(),
    mockAddToast: vi.fn(),
}));

vi.mock("react-router", async () => {
    const actual = await vi.importActual<typeof import("react-router")>(
        "react-router"
    );
    return {
        ...actual,
        useNavigate: () => mockNavigate,
    };
});

vi.mock("@heroui/react", async () => {
    const actual = await vi.importActual<typeof import("@heroui/react")>(
        "@heroui/react"
    );
    return {
        ...actual,
        addToast: mockAddToast,
    };
});

describe("Landing", () => {
    afterEach(() => {
        vi.clearAllMocks();
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

    it("닉네임을 입력하고 버튼을 눌러 가입 성공 시 엑세스 토큰을 저장하고 /home으로 navigate 된다", async () => {
        // given
        server.use(
            http.post<
                never,
                JoinAsGuestRequestDto,
                ResponseDto<JoinAsGuestResponseDto>
            >(baseUrl + "/auth/sign-up/guest", async () => {
                return HttpResponse.json({
                    success: true,
                    status: 200,
                    data: {
                        accessToken: "mock-access-token",
                    },
                });
            })
        );

        renderWithWrapper(<Landing />);

        const input = screen.getByLabelText("닉네임");
        const button = screen.getByRole("button", { name: "계속하기" });

        const user = userEvent.setup();

        // when
        await user.type(input, "hello");
        await user.click(button);

        // then
        await waitFor(() =>
            expect(localStorage.getItem("accessToken")).toBe(
                "mock-access-token"
            )
        );
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/home");
        });
    });

    it("오류 응답이 반환되면 오류 토스트를 띄운다", async () => {
        // given
        server.use(
            http.post<
                never,
                JoinAsGuestRequestDto,
                ResponseDto<JoinAsGuestResponseDto>
            >(baseUrl + "/auth/sign-up/guest", async () => {
                return HttpResponse.json(
                    {
                        success: true,
                        status: 500,
                        data: {
                            accessToken: "mock-access-token",
                        },
                    },
                    { status: 500 }
                );
            })
        );

        renderWithWrapper(<Landing />);

        const input = screen.getByLabelText("닉네임");
        const button = screen.getByRole("button", { name: "계속하기" });

        const user = userEvent.setup();

        // when
        await user.type(input, "error");
        await user.click(button);

        // then
        await waitFor(() => {
            expect(mockAddToast).toHaveBeenCalled();
        });
    });
});
