import { useState } from "react";
import { Button, Input } from "@heroui/react";
import { useNavigate } from "react-router";

export default function Landing() {
    const MAX_LENGTH = 10;
    const [nickname, setNickname] = useState("");
    const navigate = useNavigate();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const value = e.target.value;
        if (value.length <= MAX_LENGTH) {
            setNickname(value);
        }
    };

    const handleContinue = async () => {
        navigate("/home");
    };

    return (
        <div>
            <h1>Landing</h1>
            <Input
                label="닉네임"
                type="string"
                value={nickname}
                onChange={(e) => handleChange(e)}
                endContent={
                    <div className="pointer-events-none flex-none items-center text-default-400 text-small">
                        {nickname.length}/{MAX_LENGTH}
                    </div>
                }
            />
            <Button onPress={handleContinue}>계속하기</Button>
        </div>
    );
}
