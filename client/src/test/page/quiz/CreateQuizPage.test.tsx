import { waitFor } from '@testing-library/dom';
import userEvent from '@testing-library/user-event';
import { screen } from '@testing-library/react';
import { renderWithWrapper } from '../../utils/renderWithWrapper';
import CreateQuizPage from '../../../page/quiz/CreateQuizPage';

describe('CreateQuizPage', () => {
    it('퀴즈 이름, 설명, 공개 설정을 입력할 수 있다', async () => {
        // given
        renderWithWrapper(<CreateQuizPage />);

        const titleInput = screen.getByLabelText('퀴즈 이름');
        const descriptionInput = screen.getByLabelText('퀴즈 설명');
        const privateRadio = screen.getByLabelText('비공개');

        const user = userEvent.setup();

        // when
        await user.type(titleInput, '퀴즈 제목');
        await user.type(descriptionInput, '퀴즈 설명 내용');
        await user.click(privateRadio);

        // then
        await waitFor(() => {
            expect(titleInput).toHaveValue('퀴즈 제목');
            expect(descriptionInput).toHaveValue('퀴즈 설명 내용');
            expect(privateRadio).toBeChecked();
        });
    });
    it('필수 입력 필드를 모두 입력하지 않으면 <저장하기> 버튼이 비활성화된다', () => {});
    it('<저장하기> 버튼을 클릭하면 퀴즈 생성 API를 호출하고, 성공 시 해당 퀴즈의 상세보기 페이지로 이동한다', () => {});
});
