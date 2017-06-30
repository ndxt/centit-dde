package com.centit.dde.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by zou_wy on 2017/3/29.
 */


public class WebInitializer implements WebApplicationInitializer {


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        initializeSpringMvcConfig(servletContext);

        initializeSpringConfig(servletContext);
    }

    private void initializeSpringConfig(ServletContext servletContext){
        AnnotationConfigWebApplicationContext springContext = new AnnotationConfigWebApplicationContext();
        springContext.scan("com.centit");
        servletContext.addListener(new ContextLoaderListener(springContext));
    }

    private void initializeSpringMvcConfig(ServletContext servletContext) {

        AnnotationConfigWebApplicationContext contextSer = new AnnotationConfigWebApplicationContext();
        contextSer.register(SpringMvcConfig.class);
        contextSer.setServletContext(servletContext);
        ServletRegistration.Dynamic service  = servletContext.addServlet("service", new DispatcherServlet(contextSer));
        service.addMapping("/service/*");
        service.setLoadOnStartup(1);
        service.setAsyncSupported(true);
    }
}
