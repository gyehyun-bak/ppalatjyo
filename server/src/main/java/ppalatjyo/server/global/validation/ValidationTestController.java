package ppalatjyo.server.global.validation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ppalatjyo.server.global.dto.ResponseDto;

@RestController
public class ValidationTestController {

    @PostMapping("/test/validation")
    public ResponseEntity<ResponseDto<Void>> validation(@RequestBody @Valid ValidationTestRequestDto requestDto) {
        return ResponseDto.ok();
    }
}
