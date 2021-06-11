package com.centit.dde.annotation;

import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.*;

public class MethodDescribeImpl {

    /**
     * 获取自定义注解信息
     * @param packageUrl 包路径
     * @return
     */
    public static List<Map> getCustomizeAnnotInfo(String packageUrl){
        List<Map> result = new ArrayList<>();
        Reflections reflections = new Reflections(packageUrl);
        Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(ClasssDescribe.class);
        for (Class clazz : classesList) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(MethodDescribe.class))continue;
                Map<String, Object> describeMap = new HashMap<>();
                String methodName = method.getName();
                String describe = method.getAnnotation(MethodDescribe.class).describe();
                String[] parameter = method.getAnnotation(MethodDescribe.class).parameter();
                describeMap.put("methodName",methodName);
                describeMap.put("describe",describe);
                describeMap.put("parameter",parameter);
                result.add(describeMap);
            }
        }
        return result;
    }
}
