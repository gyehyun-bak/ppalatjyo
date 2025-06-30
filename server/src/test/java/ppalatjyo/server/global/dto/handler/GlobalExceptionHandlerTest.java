package ppalatjyo.server.global.dto.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ppalatjyo.server.TestSecurityConfig;
import ppalatjyo.server.global.docs.RestDocsTestController;
import ppalatjyo.server.global.dto.ResponseDtoTestController;
import ppalatjyo.server.global.security.SecurityConfig;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {GlobalExceptionHandler.class, ResponseDtoTestController.class})
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(TestSecurityConfig.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test_noSuchElementException_handling() throws Exception {
        mockMvc.perform(get("/test/notfound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error.message").value("User Not Found"))
                .andDo(document("no-such-element-exception",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부 (false)"),
                                fieldWithPath("status").description("HTTP 상태 코드 (404)"),
                                fieldWithPath("data").description("데이터 (null)"),
                                fieldWithPath("error.message").description("에러 메시지"),
                                fieldWithPath("error.timestamp").description("에러 발생 시간"),
                                fieldWithPath("error.data").description("각 필드별 에러 메시지 (null)"),
                                fieldWithPath("error.path").description("요청된 경로")
                        )
                ));
    }

    @Test
    void test_exception_handling() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error.message").value("Server Error"))
                .andDo(document("internal-server-error",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부 (false)"),
                                fieldWithPath("status").description("HTTP 상태 코드 (500)"),
                                fieldWithPath("data").description("데이터 (null)"),
                                fieldWithPath("error.message").description("에러 메시지"),
                                fieldWithPath("error.timestamp").description("에러 발생 시간"),
                                fieldWithPath("error.data").description("각 필드별 에러 메시지 (null)"),
                                fieldWithPath("error.path").description("요청된 경로")
                        )
                ));
    }
}