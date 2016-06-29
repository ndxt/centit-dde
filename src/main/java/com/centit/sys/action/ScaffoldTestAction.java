package com.centit.sys.action;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.web.StartupListener;
import com.centit.sys.po.ScaffoldTest;
import com.centit.sys.service.ScaffoldTestManager;

public class ScaffoldTestAction extends BaseEntityDwzAction<ScaffoldTest> {
    private static final Log log = LogFactory.getLog(ScaffoldTestAction.class);


    private static final long serialVersionUID = 1L;
    private ScaffoldTestManager scaffoldTestMag;

    public void setScaffoldTestManager(ScaffoldTestManager basemgr) {
        scaffoldTestMag = basemgr;
        this.setBaseEntityManager(scaffoldTestMag);
    }

    public String delete() {
        super.delete();

        return "delete";
    }

    @Override
    public String list() {
        Properties prop = StartupListener.getCurrentWebApplicationContext().getBean("systemProperties", Properties.class);
        log.info(prop);
        return super.list();
    }


}
