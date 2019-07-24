package nju.software.ocr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello() throws IOException {
        return "Hello world!";
    }

}
