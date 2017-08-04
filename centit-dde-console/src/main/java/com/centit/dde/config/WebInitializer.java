package com.centit.dde.config;

import com.centit.framework.config.SystemSpringMvcConfig;
import com.centit.framework.filter.RequestThreadLocalFilter;
import com.centit.framework.filter.ResponseCorsFilter;
import com.centit.support.algorithm.StringRegularOpt;
import com.centit.support.file.PropertiesReader;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;

/**
 * Created by zou_wy on 2017/3/29.
 */

public class WebInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        initializeSpringConfig(servletContext);

        initializeSystemSpringMvcConfig(servletContext);

        initializeSpringMvcConfig(servletContext);

        registerRequestContextListener(servletContext);

        registerSingleSignOutHttpSessionListener(servletContext);

        registerResponseCorsFilter(servletContext);

        registerCharacterEncodingFilter(servletContext);

        registerHttpPutFormContentFilter(servletContext);

        registerHiddenHttpMethodFilter(servletContext);

        registerRequestThreadLocalFilter(servletContext);

        registerSpringSecurityFilter(servletContext);
    }
    /**
     * 加载Spring 配置
     * @param servletContext ServletContext
     */
    private void initializeSpringConfig(ServletContext servletContext){
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.register(ServiceConfig.class);
        servletContext.addListener(new ContextLoaderListener(springContext));
    }

    /**
     * 加载Servlet 配置
     * @param servletContext ServletContext
     */
    private void initializeSystemSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SystemSpringMvcConfig.class);
        ServletRegistration.Dynamic system  = servletContext.addServlet("system", new DispatcherServlet(context));
        system.addMapping("/system/*");
        system.setLoadOnStartup(1);
        system.setAsyncSupported(true);
    }

    /**
     * 加载Servlet 项目配置
     * @param servletContext ServletContext
     */
    private void initializeSpringMvcConfig(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(NormalSpringMvcConfig.class);
        ServletRegistration.Dynamic system  = servletContext.addServlet("service", new DispatcherServlet(context));
        system.addMapping("/service/*");
        system.setLoadOnStartup(1);
        system.setAsyncSupported(true);
    }

    /**
     * 注册RequestContextListener监听器 （增加request、session和global session作用域）
     * @param servletContext ServletContext
     */
    private void registerRequestContextListener(ServletContext servletContext) {
        servletContext.addListener(RequestContextListener.class);
    }

    /**
     * 注册SingleSignOutHttpSessionListener监听器 （单点登录统一登出）
     * @param servletContext ServletContext
     */
    private void registerSingleSignOutHttpSessionListener(ServletContext servletContext) {
        if( StringRegularOpt.isTrue(
                PropertiesReader.getClassPathProperties(
                        "/system.properties", "cas.sso"))) {
            servletContext.addListener(SingleSignOutHttpSessionListener.class);
        }
    }

    /**
     * 注册ResponseCorsFilter过滤器 （允许跨站脚本访问过滤器）
     * @param servletContext ServletContext
     */
    private void registerResponseCorsFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic corsFilter
                = servletContext.addFilter("corsFilter", ResponseCorsFilter.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/service/*");
    }

    /**
     * 注册CharacterEncodingFilter过滤器 （设置字符集）
     * @param servletContext ServletContext
     */
    private void registerCharacterEncodingFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic encodingFilter
                = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        encodingFilter.addMappingForUrlPatterns(null, false, "*.jsp", "*.html", "/service/*", "/system/*");
        encodingFilter.setAsyncSupported(true);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
    }

    /**
     * 注册HttpPutFormContentFilter过滤器 （让put也可以想post一样接收form表单中的数据）
     * @param servletContext ServletContext
     */
    private void registerHttpPutFormContentFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic httpPutFormContentFilter
                = servletContext.addFilter("httpPutFormContentFilter", HttpPutFormContentFilter.class);
        httpPutFormContentFilter.addMappingForUrlPatterns(null, false, "/service/*", "/system/*");
        httpPutFormContentFilter.setAsyncSupported(true);
    }

    /**
     * 注册HiddenHttpMethodFilter过滤器 （过滤请求方式）
     * @param servletContext ServletContext
     */
    private void registerHiddenHttpMethodFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic hiddenHttpMethodFilter
                = servletContext.addFilter("hiddenHttpMethodFilter", HiddenHttpMethodFilter.class);
        hiddenHttpMethodFilter.addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class), false, "/service/*", "/system/*");
        hiddenHttpMethodFilter.setAsyncSupported(true);
    }

    /**
     * 注册RequestThreadLocalFilter过滤器
     *                  (将HttpServletRequest请求与本地线程绑定，方便在非Controller层获取HttpServletRequest实例)
     * @param servletContext ServletContext
     */
    private void registerRequestThreadLocalFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic requestThreadLocalFilter
                = servletContext.addFilter("requestThreadLocalFilter", RequestThreadLocalFilter.class);
        requestThreadLocalFilter.addMappingForUrlPatterns(null, false, "/*");
        requestThreadLocalFilter.setAsyncSupported(true);
    }

    /**
     * 注册springSecurityFilterChain过滤器 （使用spring security 授权与验证）
     * @param servletContext ServletContext
     */
    private void registerSpringSecurityFilter(ServletContext servletContext) {
        javax.servlet.FilterRegistration.Dynamic springSecurityFilterChain
                = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        springSecurityFilterChain.addMappingForUrlPatterns(
                null, false, "/login/*" ,"/logout/*", "/service/*", "/system/*");
        springSecurityFilterChain.setAsyncSupported(true);
    }
}
