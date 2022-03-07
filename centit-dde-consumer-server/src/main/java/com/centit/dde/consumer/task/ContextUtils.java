package com.centit.dde.consumer.task;

import org.springframework.context.ApplicationContext;

/**
 * @author zhf
 */
public class ContextUtils {
    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }
}
