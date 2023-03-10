package com.carlostadeu.hellosecurity;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class HelloSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloSecurityApplication.class, args);
    }

}

@RestController
class GreetingHttpController {

    // Authentication API
    @GetMapping("/auth/greetings/{name}")
    Greeting greetAuth(@PathVariable String name) {
        var good = StringUtils.hasText(name) && Character.isUpperCase(name.charAt(0));
        if (!good) {
            throw new IllegalArgumentException("the name must start with a capital letter");
        }
        return new Greeting("Hello, " + name + "!");
    }

    @GetMapping("/customauth/greetings/{name}")
    Greeting greetCustomAuth(@PathVariable String name) {
        return new Greeting("Hello, " + name + "!. URL with custom authentication");
    }

    // No Auth API
    @GetMapping("/noauth/greetings")
    Greeting greetNoAuth() {
        return new Greeting("Hello, URL without Authentication");
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ProblemDetail onException(MethodArgumentNotValidException ex) {

        // Get the default error messages for invalid fields
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors.toString());
    }
}

record Greeting(String message) {
}
