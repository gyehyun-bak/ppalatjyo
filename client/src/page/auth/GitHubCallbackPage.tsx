import { useNavigate, useParams } from "react-router";
import Input from "../../components/common/Input";
import { Button, Form } from "@heroui/react";
import { useState } from "react";
import { useMutation } from "@tanstack/react-query";
import { joinAsMemberByGitHub } from "../../api/auth.api";
import type { JoinAsMemberByGitHubRequest } from "../../api/types/auth/JoinAsMemberByGitHubRequest";
import type { JoinAsMemberByGitHubResponse } from "../../api/types/auth/JoinAsMemberByGitHubResponse";

export default function GitHubCallbackPage() {
    const { code } = useParams<{ code: string }>();
    const navigate = useNavigate();
    const [nickname, setNickname] = useState("");

    const { mutate, isPending } = useMutation({
        mutationFn: (data: JoinAsMemberByGitHubRequest) =>
            joinAsMemberByGitHub(data),
        onSuccess: (data: JoinAsMemberByGitHubResponse) => {
            localStorage.setItem("accessToken", data.accessToken);
            navigate("/home");
        },
    });

    if (!code || code.trim() == "") {
        navigate("/");
        return;
    }

    const handleSumbit = async (e: React.FormEvent) => {
        e.preventDefault();

        const data: JoinAsMemberByGitHubRequest = {
            code: code,
            nickname: nickname.trim() == "" ? "익명" : nickname,
        };

        mutate(data);
    };

    return (
        <Form onSubmit={handleSumbit}>
            <Input
                isRequired
                label="닉네임"
                value={nickname}
                onValueChange={setNickname}
                placeholder="익명"
                data-testid="nickname-input"
            />
            <Button
                data-testid="continue-button"
                isLoading={isPending}
                type="submit"
            ></Button>
        </Form>
    );
}
