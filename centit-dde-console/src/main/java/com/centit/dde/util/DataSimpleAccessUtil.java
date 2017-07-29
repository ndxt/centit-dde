package com.centit.dde.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-7-10
 * Time: 下午5:07
 * 表达式常量类
 */
public final class DataSimpleAccessUtil {
    private DataSimpleAccessUtil() {
    }

    private static Map<String, String> symobl = new HashMap<String, String>();

    public static final String EQ = "eq";
    public static final String NEQ = "neq";
    public static final String GT = "gt";
    public static final String LT = "lt";
    public static final String IN = "in";
    public static final String BETWEEN = "between";
    public static final String EQ_PROPERTY = "eqProperty";
    public static final String GT_PROPERTY = "gtProperty";
    public static final String LT_PROPERTY = "ltProperty";
    public static final String NEQ_PROPERTY = "neqProperty";
    public static final String IS_EMPTY = "isEmpty";
    public static final String IS_NOT_EMPTY = "isNotEmpty";
    public static final String IS_NULL = "isNull";
    public static final String IS_NOT_NULL = "isNotNull";


    static {
        symobl.put(EQ, "=");
        symobl.put(NEQ, "<>");
        symobl.put(GT, ">");
        symobl.put(LT, "<");
        symobl.put(IN, IN);
        symobl.put(BETWEEN, BETWEEN);
        symobl.put(EQ_PROPERTY, "=");
        symobl.put(GT_PROPERTY, ">");
        symobl.put(LT_PROPERTY, "<");
        symobl.put(NEQ_PROPERTY, "<>");
        symobl.put(IS_EMPTY, "is empty");
        symobl.put(IS_NOT_EMPTY, "is not empty");
        symobl.put(IS_NULL, "is NULL");
        symobl.put(IS_NOT_NULL, "is not NULL");
    }

    public static final Map<String, String> getSymobl() {
        return symobl;
    }
}
