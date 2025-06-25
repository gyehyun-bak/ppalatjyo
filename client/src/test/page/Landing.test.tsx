vi.mock('./api/auth');
vi.mock('react-router', () => ({
    useNavigate: () => vi.fn(),
}));
vi.mock('@heroui/react');

describe('게스트로 가입', () => {
    it('닉네임 입력 후 "계속하기" 클릭 시 게스트 가입 요청을 보내야 한다', async () => {
        // given
        // when
        // 닉네임 입력 + 버튼 클릭
        // then
        // 게스트 가입 호출 발생
    });

    it('가입 성공 시 accessToken을 받고 /home으로 이동해야 한다', async () => {
        // given
        // 성공 응답 mock
        // when
        // 닉네임 입력 후 계속하기 클릭
        // then
        // accessToken 저장과 페이지 이동 검증
    });

    it('오류 응답이 반환되면 오류 토스트를 띄워야 한다', async () => {
        // given
        // 오류 응답 mock
        // when
        // 닉네임 입력 후 계속하기 클릭
        // then
        // 오류 토스트 렌더링
    });
});
