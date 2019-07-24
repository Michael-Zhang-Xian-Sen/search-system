package nju.software.ocr;

import nju.software.ocr.util.ReadFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标注主程序类
 */
@RestController
@SpringBootApplication
public class HelloWorld {
    public static void main(String[] args) {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        // 启动Spring应用
        SpringApplication.run(HelloWorld.class,args);
    }
}
