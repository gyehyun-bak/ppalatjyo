package ppalatjyo.server.global.validation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
class ValidationTestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validation_success() throws Exception {
        // given
        Map<String, Object> request = new HashMap<>();
        request.put("name", "홍길동");
        request.put("email", "test@example.com");
        request.put("age", 25);
        request.put("quantity", 100);
        request.put("phone", "01012345678");
        request.put("tags", List.of("tag1", "tag2"));

        // when and then
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(document("validation-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("이름 (필수)"),
                                fieldWithPath("email").description("이메일 (필수, 이메일 형식)"),
                                fieldWithPath("age").description("나이 (0 이상, 필수)"),
                                fieldWithPath("quantity").description("주문 수량 (최대 999, 필수)"),
                                fieldWithPath("phone").description("전화번호 (10~15자리, 필수)"),
                                fieldWithPath("tags").description("태그 리스트 (최소 1개, 각 태그는 빈 문자열 불가)")
                        )
                ));
    }

    @Test
    void validation_fail() throws Exception {
        // given: 유효하지 않은 요청
        Map<String, Object> request = new HashMap<>();
        request.put("name", ""); // NotBlank 위반
        request.put("email", "invalid-email"); // Email 위반
        request.put("age", null); // NotNull 위반
        request.put("quantity", 2000); // Max 위반
        request.put("phone", "123"); // Size 위반
        request.put("tags", List.of("")); // NotBlank in a list

        // when and then
        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("validation-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("status").description("HTTP 상태 코드"),
                                fieldWithPath("error.message").description("에러 메시지"),
                                fieldWithPath("error.path").description("요청 경로"),
                                fieldWithPath("error.timestamp").description("에러 발생 시간"),
                                subsectionWithPath("error.data").description("필드별 에러 상세 정보"),
                                fieldWithPath("data").ignored()
                        )
                ));
    }
}