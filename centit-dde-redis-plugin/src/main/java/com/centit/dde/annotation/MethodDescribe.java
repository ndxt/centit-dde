package com.centit.dde.annotation;

import java.lang.annotation.*;

/**
 * @author zhou_c
 * 用于扫描类的方方法和方法使用说明
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
