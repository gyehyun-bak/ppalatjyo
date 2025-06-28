import { useState } from "react";
import { addToast, Button, Input } from "@heroui/react";
import { useNavigate } from "react-router";
import { useMutation } from "@tanstack/react-query";
import { postSignUpGuest } from "../api/auth.api";
import type { JoinAsGuestRequestDto } from "../types/api/auth/JoinAsGuestRequestDto";

export default function Landing() {
    const MAX_LENGTH = 10;
    const [nickname, setNickname] = useState("");
    const navigate = useNavigate();

    const { mutate, isPending } = useMutation({
        mutationFn: ({ nickname }: JoinAsGuestRequestDto) => {
            return postSignUpGuest({ nickname });
        },
        onSuccess: (data) => {
            if (data.data) {
                localStorage.setItem("accessToken", data.data.accessToken);
                navigate("/home");
            }
        },
        onError: () => {
            addToast({
                title: "문제가 발생하였습니다.",
                color: "danger",
            });
        },
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        if (value.length <= MAX_LENGTH) {
            setNickname(value);
        }
    };

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
                onChange={(e) => handleChange(e)}
                placeholder="익명"
                endContent={
                    <div className="pointer-events-none flex-none items-center text-default-400 text-small">
                        {nickname.length}/{MAX_LENGTH}
                    </div>
                }
            />
            <Button
                color="primary"
                isLoading={isPending}
                onPress={handleContinue}
            >
                계속하기
            </Button>
        </div>
    );
}
