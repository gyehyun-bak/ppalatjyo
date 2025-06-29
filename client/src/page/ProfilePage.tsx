import { useQuery } from "@tanstack/react-query";
import { getMe } from "../api/user.api";
import { Button } from "@heroui/react";
import { useNavigate } from "react-router";

export default function ProfilePage() {
    const navigate = useNavigate();

    const { data } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
    });

    return (
        <div>
            {data && data.data && (
                <div>
                    <p>nickname: {data.data.nickname}</p>
                </div>
            )}
            <Button
                aria-label="edit-nickname"
                onPress={() => navigate("/profile/edit/nickname")}
            >
                닉네임 수정
            </Button>
            <Button aria-label="log-out" onPress={() => navigate("/logout")}>
                로그아웃
            </Button>
        </div>
    );
}
