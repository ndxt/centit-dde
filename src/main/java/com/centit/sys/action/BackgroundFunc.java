package com.centit.sys.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.centit.sys.service.CodeRepositoryManager;

public class BackgroundFunc extends HttpServlet {

    private static final long serialVersionUID = 6664783192905386843L;
    /**
     * Constructor of the object.
     */
    private CodeRepositoryManager codeRepo;

    public void setCodeRepoManager(CodeRepositoryManager crm) {
        this.codeRepo = crm;
    }

    public BackgroundFunc() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to get.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doPost(request, response);
    }

    /**
     * The doPost method of the servlet. <br>
     * <p/>
     * This method is called when a form has its tag value method equals to post.
     *
     * @param request  the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException      if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String sFuncName = request.getParameter("func");
        if ("refreshcode".equals(sFuncName)) {
            ServletContext application;
            WebApplicationContext wac;
            application = getServletContext();
            wac = WebApplicationContextUtils.getWebApplicationContext(application);//获取spring的context
            codeRepo = (CodeRepositoryManager) wac.getBean("codeRepositoryManager");
            codeRepo.refreshAll();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("data repository has been updated!!!");
            out.close();
        }
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
