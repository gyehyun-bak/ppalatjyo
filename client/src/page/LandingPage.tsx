import { useState } from "react";
import { addToast, Button } from "@heroui/react";
import { useNavigate } from "react-router";
import { useMutation } from "@tanstack/react-query";
import { postSignUpGuest } from "../api/auth.api";
import type { JoinAsGuestRequest } from "../api/types/auth/JoinAsGuestRequest";
import Input from "../components/common/Input";
import GitHubLogInButton from "../components/landing/GitHubLoginButton";

export default function LandingPage() {
    const MAX_LENGTH = 10;
    const [nickname, setNickname] = useState("");
    const navigate = useNavigate();

    const { mutate, isPending } = useMutation({
        mutationFn: ({ nickname }: JoinAsGuestRequest) => {
            return postSignUpGuest({ nickname });
        },
        onSuccess: (data) => {
            localStorage.setItem("accessToken", data.accessToken);
            navigate("/home");
        },
        onError: () => {
            addToast({
                title: "문제가 발생하였습니다.",
                color: "danger",
            });
        },
    });

    const validateNickname = () => {
        return nickname.trim() != "";
    };

    const handleContinue = async () => {
        if (validateNickname()) {
            mutate({ nickname });
        } else {
            mutate({ nickname: "익명" });
        }
    };

    return (
        <div>
            <h1>Landing</h1>
            <Input
                label="닉네임"
                type="string"
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                placeholder="익명"
                maxLength={MAX_LENGTH}
                showLength={true}
            />
            <Button
                color="primary"
                isLoading={isPending}
                onPress={handleContinue}
            >
                계속하기
            </Button>
            <GitHubLogInButton />
        </div>
    );
}
