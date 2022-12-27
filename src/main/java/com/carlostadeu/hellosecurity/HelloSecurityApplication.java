package com.carlostadeu.hellosecurity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class HelloSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloSecurityApplication.class, args);
    }

}

@RestController
class GreetingHttpController {

    @GetMapping("/greetings/{name}")
    Greeting greet(@PathVariable String name) {
        var good = StringUtils.hasText(name) && Character.isUpperCase(name.charAt(0));
        if (!good) {
            throw new IllegalArgumentException("the name must start with a capital letter");
        }
        return new Greeting("Hello, " + name + "!");
    }
}

@ControllerAdvice
@Slf4j
class ProblemDetailErrorHandlingControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ProblemDetail onException(HttpServletRequest request) {
        request.getAttributeNames().asIterator()
                .forEachRemaining(attributeName -> log.info("attributeName" + attributeName));
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "the name is invalid");
    }
}

record Greeting(String message) {
}
