package com.centit.dde.annotation;

import java.lang.annotation.*;

/**
 * @author zhou_c
 * 用于方法的使用说明和参数说明
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited
public @interface MethodDescribe {
    //方法使用描述说明
    String describe();
    //方法参数
    String[] parameter();
}
