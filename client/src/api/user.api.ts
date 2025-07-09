import type { UserResponse } from "./types/user/UserResponse";
import { api } from "./axios";
import type { EditMeRequest } from "./types/user/EditMeRequest";

export const getMe = async (): Promise<UserResponse> => {
    return (await api.get<UserResponse>("/users/me")).data;
};

export const editMe = async (data: EditMeRequest): Promise<UserResponse> => {
    return (await api.put<UserResponse>("/users/me", data)).data;
};
