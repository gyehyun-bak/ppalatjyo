import { screen, waitFor } from "@testing-library/dom";
import userEvent from "@testing-library/user-event";
import { renderWithWrapper } from "../../utils/renderWithWrapper";
import ToMemberPage from "../../../page/profile/ToMemberPage";

vi.mock("react-router");

describe("ToMemberPage", () => {
    it('"GitHub으로 계속하기" 버튼을 클릭하면 GitHub 인증 페이지로 이동한다', async () => {
        // given
        const assignMock = vi.fn();
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        delete (window as any).location;
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        (window as any).location = { assign: assignMock };

        renderWithWrapper(<ToMemberPage />);
        const gitHubButton = await screen.findByTestId("github-button");
        const user = userEvent.setup();

        // when
        await user.click(gitHubButton);

        // then
        await waitFor(() => {
            expect(assignMock).toHaveBeenCalled();
        });
    });
});
