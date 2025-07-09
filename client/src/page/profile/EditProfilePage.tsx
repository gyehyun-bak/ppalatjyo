import { useEffect, useState } from "react";
import Input from "../../components/common/Input";
import { useMutation, useQuery } from "@tanstack/react-query";
import { editMe, getMe } from "../../api/user.api";
import ErrorPage from "../error/ErrorPage";
import { Button, Form } from "@heroui/react";
import { useNavigate } from "react-router";

export default function EditProfilePage() {
    const navigate = useNavigate();
    const [nickname, setNickname] = useState("");

    const { data, isSuccess } = useQuery({
        queryKey: ["me"],
        queryFn: getMe,
    });

    const { mutate, isPending } = useMutation({
        mutationFn: () => editMe({ nickname }),
        onSuccess: () => {
            navigate("/profile");
        },
    });

    useEffect(() => {
        if (isSuccess && data) {
            setNickname(data.nickname);
        }
    }, [data, isSuccess]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        mutate();
    };

    if (!isSuccess) {
        return <ErrorPage />;
    }

    return (
        <div>
            <Form onSubmit={handleSubmit}>
                <Input
                    isRequired
                    data-testid="nickname-input"
                    value={nickname}
                    onValueChange={setNickname}
                />
                <Button
                    isLoading={isPending}
                    data-testid="save-button"
                    type="submit"
                >
                    저장하기
                </Button>
            </Form>
        </div>
    );
}
