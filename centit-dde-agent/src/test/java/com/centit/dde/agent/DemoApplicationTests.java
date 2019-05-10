package com.centit.dde.agent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
        try {
            Process proc= Runtime.getRuntime().exec("java HelloWorld >2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Hello");

    }

}
