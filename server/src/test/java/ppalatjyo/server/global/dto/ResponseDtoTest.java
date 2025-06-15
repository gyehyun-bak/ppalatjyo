package ppalatjyo.server.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseDtoTest {

    @Test
    @DisplayName("성공 응답 - 메시지")
    void okWithMessage() {
        // given
        String message = "Okay.";

        // when
        ResponseEntity<ResponseDto<Void>> response = ResponseDto.ok(message);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ResponseDto.class);
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().getMessage()).isEqualTo(message);
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getError()).isNull();
    }

    @Test
    @DisplayName("성공 응답 - 메시지 + 데이터")
    void okWithMessageAndData() {
        // given
        String message = "Okay with data.";
        String content = "content";
        TestResponseDto dataDto = new TestResponseDto(content);

        // when
        ResponseEntity<ResponseDto<TestResponseDto>> response = ResponseDto.ok(message, dataDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ResponseDto.class);
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getBody().getMessage()).isEqualTo(message);
        assertThat(response.getBody().getError()).isNull();

        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData()).isInstanceOf(TestResponseDto.class);
        assertThat(response.getBody().getData().getContent()).isEqualTo(content);
    }

    @Data
    @AllArgsConstructor
    static
    class TestResponseDto {
        private String content;
    }
}