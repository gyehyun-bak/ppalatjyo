package ppalatjyo.server.global.validation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationTestController {

    @PostMapping("/test/validation")
    public ResponseEntity<Void> validation(@RequestBody @Valid ValidationTestRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }
}
