package com.centit.dde.agent;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

@Test
public  void test() {
    Map<String, Object> param = new HashMap<>();
    param.put("hi", "112" );
    param.put("ee",null);
    String text = JSONObject.toJSONString(param,WriteMapNullValue);
    System.out.println(text);
    }
}

