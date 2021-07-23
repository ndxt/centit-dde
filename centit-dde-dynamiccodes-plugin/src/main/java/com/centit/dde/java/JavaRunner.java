package com.centit.dde.java;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.domain.CodeInfo;
import com.centit.dde.domain.Task;
import com.centit.dde.exception.CodeRuntimeException;
import com.centit.dde.util.MapCount;
import com.sun.org.apache.bcel.internal.classfile.CodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.Map.Entry;


public class JavaRunner {

    private static final Logger LOG = LoggerFactory.getLogger(JavaRunner.class);

    private Task task;

    private CodeInfo codeInfo;

    private Object objInstance;

    public JavaRunner(Task task) {
        this.task = task;
        this.codeInfo = task.codeInfo();
    }

    /**
     * compile to class if , class in task , it nothing to do!
     *
     * @return
     * @throws CodeException
     */
    public JavaRunner compile() {
        if (codeInfo.getClassz() != null) {
            return this;
        }
        synchronized (codeInfo) {
            if (codeInfo.getClassz() == null) {
                try {
                    String code = task.getCode();
                    DynamicEngine de = DynamicEngine.getInstance();
                    String pack = JavaSourceUtil.findPackage(code);
                    String className = JavaSourceUtil.findClassName(code);
                    LOG.info("to compile " + pack + "." + className);
                    if (className == null) {
                        throw new Exception("not find className");
                    }
                    codeInfo.setClassLoader(de.getParentClassLoader());
                    Class<?> clz = de.javaCodeToClass(pack + "." + className, code);
                    codeInfo.setClassz(clz);
                    MapCount<String> mc = new MapCount<>();
//					// set execute method
                    for (Method method : clz.getMethods()) {
                        if (!Modifier.isPublic(method.getModifiers()) || method.isBridge() || method.getDeclaringClass() != clz) {
                            continue;
                        }
                        mc.add(method.getName());
                    }
                    if (mc.size() == 0) {
                        throw new CodeRuntimeException("you must set a Execute or DefaultExecute annotation for execute");
                    }
                    StringBuilder sb = null;
                    for (Entry<String, Double> entry : mc.get().entrySet()) {
                        if (entry.getValue() > 1) {
                            if (sb == null) {
                                sb = new StringBuilder();
                            }
                            sb.append(entry.getKey() + " ");
                        }
                    }
                    if (sb != null && sb.length() > 0) {
                        sb.append(" Execute method name repetition");
                        throw new CodeRuntimeException(sb.toString());
                    }
                    codeInfo.setClassz(clz);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CodeRuntimeException(e);
                }
            }
        }
        return this;
    }

    /**
     * instance and inject objcet if object is created , it nothing to do !
     *
     * @return
     */
    public JavaRunner instance() {
        if (!codeInfo.isSingle()) {// if not single .it only instance by run
            _instance();
            return this;
        }
        if (codeInfo.getJavaObject() != null ) {
            this.objInstance = codeInfo.getJavaObject();
            return this;
        }
        synchronized (codeInfo) {
            if (codeInfo.getJavaObject() == null ) {
                _instance();
            } else {
                this.objInstance = codeInfo.getJavaObject();
            }
        }
        return this;
    }

    private void _instance() {
        try {
            LOG.info("to instance with ioc className: " + codeInfo.getClassz().getName());
            objInstance = codeInfo.getClassz().newInstance();
            if (codeInfo.isSingle()) {
                codeInfo.setJavaObject(objInstance);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new CodeRuntimeException(e);
        }
    }

    /**
     * @return
     */
    public Task getTask() {
        return this.task;
    }

    private static final Object[] DEFAULT_ARG = new Object[0];

    /**
     * execte task defaultExecute if not found , it execute excutemehtod ， if not found it throw Exception
     *
     * @return
     *
     * @throws CodeException
     */
    public Object execute(String methodName, JSONObject params) {
        Method[] declaredMethods = codeInfo.getClassz().getDeclaredMethods();
        Method method=null;
        if (declaredMethods.length>=2){//当类中有2个或者2个以上的方法时必须填写方法名
            if (StrUtil.isBlank(methodName)){
                return "类中存在多个方法，请指定执行的方法名！";
            }
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(methodName)){
                    method=declaredMethod;
                }
            }
        }
        if (declaredMethods.length==0){
            return "类中没有可执行的方法！";
        }
        method=method==null?declaredMethods[0]:method;
        long start = System.currentTimeMillis();
        Parameter[] parameters = method.getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            args[i]=params.get(parameters[i].getName());
        }
        try {
            Object invoke = method.invoke(objInstance, args);
            String endInfo = "Execute OK  " + task.getName() + "/" + method.getName() + " succesed ! use Time : " + (System.currentTimeMillis() - start);
            LOG.info(endInfo);
            return invoke;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CodeRuntimeException(e);
        }
    }

    /**
     * to compile it and validate some function
     *
     * @return it always return true
     * @throws CodeException
     * @throws IOException
     */
    public boolean check() {
        try {
            String code = task.getCode();
            DynamicEngine de = DynamicEngine.getInstance();
            String pack = JavaSourceUtil.findPackage(code);
            String className = JavaSourceUtil.findClassName(code);
            if (className == null) {
                throw new Exception("not find className");
            }
            Class<?> clz =de.javaCodeToClass(pack + "." + className, code);
            MapCount<String> mc = new MapCount<>();
            // set execute method
            for (Method method : clz.getMethods()) {
                if (!Modifier.isPublic(method.getModifiers()) || method.isBridge() || method.getDeclaringClass() != clz) {
                    continue;
                }
                mc.add(method.getName());
            }
            if (mc.size() == 0) {
                throw new CodeRuntimeException("you must set a Execute or DefaultExecute annotation for execute");
            }
            StringBuilder sb = null;
            for (Entry<String, Double> entry : mc.get().entrySet()) {
                if (entry.getValue() > 1) {
                    if (sb == null) {
                        sb = new StringBuilder();
                    }
                    sb.append(entry.getKey() + " ");
                }
            }
            if (sb != null && sb.length() > 0) {
                sb.append(" Execute method name repetition");
                throw new CodeRuntimeException(sb.toString());
            }
            return true;
        }catch(Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}





