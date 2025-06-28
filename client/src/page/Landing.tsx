import { useState } from "react";
import { Input } from "@heroui/react";

export default function Landing() {
    const [nickname, setNickname] = useState("");

    return (
        <div>
            <h1>Landing</h1>
            <Input
                label="닉네임"
                type="string"
                isClearable
                value={nickname}
                onChange={(e) => setNickname(e.target.value)}
                onClear={() => setNickname("")}
            />
        </div>
    );
}
