import { useEffect } from "react";
import { useAuthStore } from "../../store/useAuthStore";
import { useNavigate } from "react-router";
import { useMutation } from "@tanstack/react-query";
import { logOut } from "../../api/auth.api";

export default function LogOutPage() {
    const { setAuthenticated } = useAuthStore();
    const navigate = useNavigate();

    const { mutate } = useMutation({
        mutationFn: logOut,
        onSuccess: () => {
            setAuthenticated(false);
            localStorage.removeItem("accessToken");
            navigate("/landing");
        },
    });

    useEffect(() => {
        mutate();
    }, [mutate]);

    return <></>;
}
