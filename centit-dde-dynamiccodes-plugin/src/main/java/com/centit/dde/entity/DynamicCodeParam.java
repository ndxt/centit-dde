package com.centit.dde.entity;

import com.alibaba.fastjson.JSONObject;

/**
 * 动态代码片段执行器参数封装
 */
public class DynamicCodeParam {
    //标签节点id
    private String id;
    //数据集
    private String source;
    //java 代码，可以是类，也可以是方法（如果是方法需要手动的指定导入类  如：import com.alibaba.fastjson.JSONObject;）
    private String javaCode;
    //方法依赖的类 （如果传入的是类，该参数不填）
    private String importClass;
    //第三方依赖(maven 包)
    private  String mavenDeps;
    //表达式
    private  String expression;
    //方法名
    private  String methodName;
    //方法参数（后台内部使用，前端不需要展示）
    private JSONObject methodParams;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public JSONObject getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(JSONObject methodParams) {
        this.methodParams = methodParams;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getJavaCode() {
        return javaCode;
    }

    public void setJavaCode(String javaCode) {
        this.javaCode = javaCode;
    }

    public String getImportClass() {
        return importClass;
    }

    public void setImportClass(String importClass) {
        this.importClass = importClass;
    }

    public String getMavenDeps() {
        return mavenDeps;
    }

    public void setMavenDeps(String mavenDeps) {
        this.mavenDeps = mavenDeps;
    }
}
