package com.centit.dde.agent.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhf
 */
@RestController
public class HeartController {
    @RequestMapping(value = "/heart")
    public String testHeart() {
        return "ok";
    }

}
