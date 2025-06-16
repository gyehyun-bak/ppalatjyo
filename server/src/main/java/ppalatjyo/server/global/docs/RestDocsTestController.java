package ppalatjyo.server.global.docs;

import org.springframework.web.bind.annotation.*;

@RestController
public class RestDocsTestController {

    @GetMapping("/test/restdocs/hello")
    public String hello(){
        return "Hello World!";
    }
}
