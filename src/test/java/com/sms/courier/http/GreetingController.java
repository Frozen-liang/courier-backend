package com.sms.courier.http;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping(value = "/greeting")
    public @ResponseBody
    Greeting greeting(
        @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @PostMapping(value = "/greeting", consumes = "application/json", produces =
        "application/json")
    public Greeting addGreeting(
        @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return greeting(name);
    }

    @DeleteMapping(value = "/greeting", consumes = "application/json", produces =
        "application/json")
    public String updateGreeting(
        @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return "ok";
    }

    @PutMapping(value = "/greeting", consumes = "application/json", produces =
        "application/json")
    public String deleteGreeting(
        @RequestParam(value = "name", required = false, defaultValue = "World") String name) {
        return "ok";
    }

    @PostMapping(value = "/greetingPost", consumes = APPLICATION_FORM_URLENCODED_VALUE)
    public Greeting postGreeting(@RequestParam("name") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @PostMapping(value = "/greetingPostJson", consumes = "application/json")
    public Greeting postGreetingJson(@RequestBody Greeting greeting) {
        return greeting;
    }
}