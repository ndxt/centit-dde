package com.centit.sys.util;

import org.springframework.web.context.ContextLoader;

public class InstantMsgFactory {
    public static IInstantMsg getInstance() {

        return ContextLoader.getCurrentWebApplicationContext().getBean("instantMsg", IInstantMsg.class);
    }
}
