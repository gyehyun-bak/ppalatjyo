package ppalatjyo.server.global.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
public class ResponseDtoTestController {

    @GetMapping("/test/response/ok")
    public ResponseEntity<ResponseDto<Void>> testOk() {
        return ResponseDto.ok();
    }

    @GetMapping("/test/notfound")
    public void testNotFound() {
        throw new NoSuchElementException("User Not Found");
    }

    @GetMapping("/test/error")
    public void testError() {
        throw new RuntimeException("Something went wrong");
    }
}
