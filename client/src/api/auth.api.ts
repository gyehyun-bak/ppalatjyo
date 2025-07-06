import type { JoinAsGuestRequest } from "./types/auth/JoinAsGuestRequest";
import type { JoinAsGuestResponse } from "./types/auth/JoinAsGuestResponse";
import type { TokenReissueResponse } from "./types/auth/TokenReissueResponse";
import { api } from "./axios";
import type { JoinAsMemberByGitHubRequest } from "./types/auth/JoinAsMemberByGitHubRequest";
import type { JoinAsMemberByGitHubResponse } from "./types/auth/JoinAsMemberByGitHubResponse";

export const postSignUpGuest = async (
    requestDto: JoinAsGuestRequest
): Promise<JoinAsGuestResponse> => {
    return (
        await api.post<JoinAsGuestResponse>("/auth/sign-up/guest", requestDto)
    ).data;
};

export const getNewTokens = async (): Promise<TokenReissueResponse> => {
    return (await api.get<JoinAsGuestResponse>("/auth/tokens")).data;
};

export const joinAsMemberByGitHub = async (
    data: JoinAsMemberByGitHubRequest
): Promise<JoinAsMemberByGitHubResponse> => {
    return (
        await api.post<JoinAsMemberByGitHubResponse>(
            "/auth/sign-up/github",
            data
        )
    ).data;
};
