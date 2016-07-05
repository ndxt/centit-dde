package com.centit.dde.util;

import org.springframework.web.context.ContextLoaderListener;

import com.centit.framework.common.SysParametersUtils;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 14-11-12
 * Time: 上午9:39
 * 向  exchangeTaskConsole.jsp  中Js函数推送消息
 */
public class TaskConsoleWriteUtils {

    public static void write(Long taskId, String message) {
        pushAll("TASKLOG.console", String.valueOf(taskId), message);
    }

    public static void writeInfo(Long taskId, String message) {
        pushAll("TASKLOG.consoleInfo", String.valueOf(taskId), message);
    }

    public static void writeError(Long taskId, String message) {
        pushAll("TASKLOG.consoleError", String.valueOf(taskId), message);
    }

    public static void writeAlreadyProcess(Long taskId, String message) {
        pushAll("TASKLOG.alreadyProcess", String.valueOf(taskId), message);
    }

    public static void writeProcess(Long taskId, Long success, Long error, String exchangeMapinfoName) {
        pushAll("TASKLOG.process", String.valueOf(taskId), String.valueOf(success + error), String.valueOf(success),
                String.valueOf(error), exchangeMapinfoName);
    }

    public static void stop(Long taskId) {
        pushAll("TASKLOG.stop", String.valueOf(taskId));
    }

    private static IInstantMsg getInstantMsg() {
        return ContextLoaderListener.getCurrentWebApplicationContext().getBean("instantMsg", IInstantMsg.class);
    }

    public static void pushAll(String script, String... params) {
        if (isDevelopDebug()) {
            getInstantMsg().pushAll(script, params);
        }
    }

    private static boolean isDevelopDebug() {
        return "1".equals(SysParametersUtils.getValue("task.console.output"));
    }
}
