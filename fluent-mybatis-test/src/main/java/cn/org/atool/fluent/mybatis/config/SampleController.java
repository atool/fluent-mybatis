package cn.org.atool.fluent.mybatis.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("test")
public class SampleController {

    @GetMapping
    public Map testGet() {
        return new HashMap<String, String>() {{
            put("name", "springboot");
        }};
    }

    @GetMapping(path = "str")
    public String testGetStr() {
        return "OK";
    }
}