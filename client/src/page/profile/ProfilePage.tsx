import { useQuery } from "@tanstack/react-query";
import { Button } from "@heroui/react";
import { useNavigate } from "react-router";
import { getMe } from "../../api/user.api";

export default function ProfilePage() {
    const navigate = useNavigate();

    const { data, isSuccess } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
    });

    return (
        <div>
            {isSuccess && (
                <div>
                    <p>nickname: {data.nickname}</p>
                </div>
            )}
            <Button
                aria-label="edit-nickname"
                onPress={() => navigate("/profile/edit")}
            >
                프로필 수정
            </Button>
            <Button aria-label="log-out" onPress={() => navigate("/logout")}>
                로그아웃
            </Button>
            <Button
                data-testid="to-member-button"
                onPress={() => navigate("/profile/to-member")}
            >
                멤버로 전환
            </Button>
        </div>
    );
}
