package com.centit.sys.util;

import java.io.File;

import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContextException;

import com.centit.core.web.StartupListener;

/**
 * 此启动监听为了检测项目日志文件目录是否存在，不存在就抛出异常禁止启动
 */
public class WebStartupLinstener extends StartupListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        String logHome = SysParametersUtils.getLogHome();
        File logHomeDirectory = new File(logHome);
        if (!logHomeDirectory.canRead() || !logHomeDirectory.isDirectory()) {
            String exceptionText = "系统启动时检测日志目录不存在";
            System.err.println(exceptionText);
            throw new ApplicationContextException(exceptionText);
        }

        super.contextInitialized(event);
    }
}
