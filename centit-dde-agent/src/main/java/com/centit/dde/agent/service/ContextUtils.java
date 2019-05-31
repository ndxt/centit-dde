package com.centit.dde.agent.service;

import org.springframework.context.ApplicationContext;

public class ContextUtils {
    public static ApplicationContext context;

    private ContextUtils() {
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static <T> T getBean(Class<T> t) {
        return context.getBean(t);
    }
}
