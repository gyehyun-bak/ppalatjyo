import { Button } from "@heroui/react";

const GITHUB_AUTH_URL =
    "https://github.com/login/oauth/authorize" +
    "?client_id=Ov23liupnt2y8DOD180K" +
    "&redirect_uri=http://localhost:5173/callback/github" +
    "&scope=user:email";

export default function GitHubLogInButton() {
    const handleClick = () => {
        window.location.assign(GITHUB_AUTH_URL);
    };

    return (
        <Button onPress={handleClick} data-testid="github-button">
            GitHub로 계속하기
        </Button>
    );
}
