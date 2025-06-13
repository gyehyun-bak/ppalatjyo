package ppalatjyo.server.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestDocTestController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello World!";
    }
}
