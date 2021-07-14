package com.centit.dde.config;

import com.centit.framework.config.SystemSpringMvcConfig;
import com.centit.framework.config.WebConfig;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;

import javax.annotation.Nonnull;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 *
 * @author zou_wy
 * @date 2017/3/29
 */

public class WebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(@Nonnull ServletContext servletContext) throws ServletException {
        //webservices  start
        ServletRegistration.Dynamic cxfServlet = servletContext.addServlet("cxfServlet", CXFServlet.class);
        cxfServlet.addMapping("/webservice/*");
        cxfServlet.setLoadOnStartup(1);
        //webservices  end
        WebConfig.registerSpringConfig(servletContext, ServiceConfig.class);

        String [] servletUrlPatterns = {"/system/*","/dde/*"};
        WebConfig.registerServletConfig(servletContext, "system",
            "/system/*",
            SystemSpringMvcConfig.class, SwaggerConfig.class);
        WebConfig.registerServletConfig(servletContext, "dde",
            "/dde/*",
            DdeSpringMvcConfig.class,SwaggerConfig.class);

        WebConfig.registerRequestContextListener(servletContext);
        WebConfig.registerSingleSignOutHttpSessionListener(servletContext);
        WebConfig.registerCharacterEncodingFilter(servletContext, servletUrlPatterns);
        WebConfig.registerHttpPutFormContentFilter(servletContext, servletUrlPatterns);
        WebConfig.registerHiddenHttpMethodFilter(servletContext, servletUrlPatterns);
        WebConfig.registerRequestThreadLocalFilter(servletContext);
        WebConfig.registerSpringSecurityFilter(servletContext, servletUrlPatterns);
    }


}
