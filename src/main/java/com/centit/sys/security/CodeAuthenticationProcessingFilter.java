package com.centit.sys.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CodeAuthenticationProcessingFilter extends UsernamePasswordAuthenticationFilter {

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

		/*String request_checkcode = request.getParameter(CaptchaImageUtil.REQUESTCHECKCODE);
		if(! "nocheckcode".equals(request_checkcode)){	
		    String session_checkcode = "";
	        Object obj = request.getSession().getAttribute(CaptchaImageUtil.SESSIONCHECKCODE);  
	        if (obj!=null)
	            session_checkcode = obj.toString();
    		if(request_checkcode==null || ! request_checkcode.equalsIgnoreCase(session_checkcode)  )
    			throw new AuthenticationServiceException("bad checkcode");   
		}*/
        return super.attemptAuthentication(request, response);
    }
}
