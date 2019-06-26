package com.centit.dde.datamoving;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.product.dataopt.core.BizModel;
import com.centit.product.metadata.service.MetaDataService;
import com.centit.support.database.utils.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.naming.Context;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = {"com.centit"},
    excludeFilters = @ComponentScan.Filter(value = org.springframework.stereotype.Controller.class))
public class DataMovingApplication  {

    /**
     * 这个只做一个任务的一次交换
     * 由 agent 调度
     * @param args args[0] 为任务ID
     */


    public static void main(String[] args) throws SQLException {
        if(args==null || args.length<1){
            return;
        }
        ConfigurableApplicationContext context=SpringApplication.run(DataMovingApplication.class, args);
        TaskRun taskRun =context.getBean(TaskRun.class);
        BizModel bizModel=taskRun.runTask(args[0]);
        System.out.println(bizModel.getModelName());
    }
}
