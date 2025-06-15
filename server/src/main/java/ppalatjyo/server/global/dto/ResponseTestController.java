package ppalatjyo.server.global.dto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResponseTestController {

    @GetMapping("/test/ok")
    public ResponseEntity<ResponseDto<Void>> testOk() {
        return ResponseDto.ok("ok.");
    }
}
