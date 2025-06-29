import { http, HttpResponse } from "msw";
import { baseUrl, server } from "../../mocks/server";
import type { LobbiesResponseDto } from "../../types/api/lobby/LobbiesResponseDto";
import type { ResponseDto } from "../../types/api/ResponseDto";
import { renderWithWrapper } from "../utils/renderWithWrapper";
import HomePage from "../../page/HomePage";
import { waitFor, screen } from "@testing-library/dom";
import "@testing-library/jest-dom";
import userEvent from "@testing-library/user-event";

const { mockNavigate } = vi.hoisted(() => ({
    mockNavigate: vi.fn(),
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

describe("Home", () => {
    it("로비 목록을 가져와 표시한다", async () => {
        // given
        server.use(
            http.get<never, never, ResponseDto<LobbiesResponseDto>>(
                baseUrl + "/lobbies",
                async () => {
                    return HttpResponse.json({
                        success: true,
                        status: 200,
                        data: {
                            lobbies: [
                                {
                                    id: 1,
                                    name: "로비 1",
                                    options: {
                                        maxUsers: 10,
                                        minPerGame: 2,
                                        secPerQuestion: 30,
                                    },
                                    quiz: {
                                        id: 1,
                                        title: "퀴즈 1",
                                    },
                                    host: { id: 1, nickname: "사용자1" },
                                    currentUserCount: 1111,
                                },
                                {
                                    id: 2,
                                    name: "로비 2",
                                    options: {
                                        maxUsers: 5,
                                        minPerGame: 1,
                                        secPerQuestion: 20,
                                    },
                                    quiz: {
                                        id: 2,
                                        title: "퀴즈 2",
                                    },
                                    host: { id: 2, nickname: "사용자2" },
                                    currentUserCount: 2222,
                                },
                            ],
                        },
                    });
                }
            )
        );

        // when
        renderWithWrapper(<HomePage />);

        // then
        await waitFor(() => {
            expect(screen.getByText(/로비 1/)).toBeInTheDocument();
            expect(screen.getByText(/로비 2/)).toBeInTheDocument();
            expect(screen.getByText(/퀴즈 1/)).toBeInTheDocument();
            expect(screen.getByText(/퀴즈 2/)).toBeInTheDocument();
            expect(screen.getByText(/사용자1/)).toBeInTheDocument();
            expect(screen.getByText(/사용자2/)).toBeInTheDocument();
            expect(screen.getByText(/1111/)).toBeInTheDocument();
            expect(screen.getByText(/2222/)).toBeInTheDocument();
        });
    });

    it("<새로고침> 버튼을 누르면 로비 목록을 새로고침한다", async () => {
        // given
        server.use(
            http.get<never, never, ResponseDto<LobbiesResponseDto>>(
                baseUrl + "/lobbies",
                async () => {
                    return HttpResponse.json({
                        success: true,
                        status: 200,
                        data: {
                            lobbies: [
                                {
                                    id: 1,
                                    name: "oldLobby",
                                    options: {
                                        maxUsers: 10,
                                        minPerGame: 2,
                                        secPerQuestion: 30,
                                    },
                                    quiz: {
                                        id: 1,
                                        title: "퀴즈 1",
                                    },
                                    host: { id: 1, nickname: "사용자1" },
                                    currentUserCount: 1111,
                                },
                            ],
                        },
                    });
                }
            )
        );

        renderWithWrapper(<HomePage />);
        const refreshButton = screen.getByLabelText("refresh");
        const user = userEvent.setup();

        // when
        server.use(
            http.get<never, never, ResponseDto<LobbiesResponseDto>>(
                baseUrl + "/lobbies",
                async () => {
                    return HttpResponse.json({
                        success: true,
                        status: 200,
                        data: {
                            lobbies: [
                                {
                                    id: 1,
                                    name: "newLobby",
                                    options: {
                                        maxUsers: 10,
                                        minPerGame: 2,
                                        secPerQuestion: 30,
                                    },
                                    quiz: {
                                        id: 1,
                                        title: "퀴즈 1",
                                    },
                                    host: { id: 1, nickname: "사용자1" },
                                    currentUserCount: 1111,
                                },
                            ],
                        },
                    });
                }
            )
        );
        await user.click(refreshButton);

        // then
        await waitFor(() => {
            expect(screen.getByText(/newLobby/)).toBeInTheDocument();
            expect(screen.queryByText(/oldLobby/)).not.toBeInTheDocument();
        });
    });

    it("<로비 만들기> 버튼을 누르면 로비 만들기 페이지로 이동한다", async () => {
        // given
        renderWithWrapper(<HomePage />);
        const createLobbyButton = screen.getByLabelText("create-lobby");
        const user = userEvent.setup();

        // when
        await user.click(createLobbyButton);

        // then
        await waitFor(() => {
            expect(mockNavigate).toHaveBeenCalledWith("/lobbies/create");
        });
    });
});
