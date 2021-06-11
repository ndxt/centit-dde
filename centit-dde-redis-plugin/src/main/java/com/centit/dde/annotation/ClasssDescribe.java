package com.centit.dde.annotation;

import java.lang.annotation.*;

/**
 * @author zhou_c
 * 用于扫描类的方方法和方法使用说明
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface ClasssDescribe {
}
