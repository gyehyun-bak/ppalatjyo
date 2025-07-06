import { mockUseParams } from "../../../__mocks__/react-router";
import { renderWithWrapper } from "../utils/renderWithWrapper";

vi.mock("react-router");

describe("GitHubCallbackPage", () => {
    beforeEach(() => {
        mockUseParams.mockReturnValue("1q2w3e4r");
    });

    it("닉네임을 입력하고 계속하기를 누르면 code, nickname으로 GitHub으로 가입을 요청한다", async () => {
        // given

        renderWithWrapper(<></>);
        // when

        // then
    });
});
