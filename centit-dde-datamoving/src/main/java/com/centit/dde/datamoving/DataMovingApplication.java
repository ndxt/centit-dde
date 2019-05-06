package com.centit.dde.datamoving;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.service.TaskRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import javax.naming.Context;
import java.sql.SQLException;

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
        String taskLogId = args[0];
       // taskLogId = "1";

        System.out.println(taskLogId);
        //通过 TaskLogId 获取到 TaskId
        // 通过TaskId 获取 DataPacketId
        // 通过 DataPacketId创建 DBPacketBizSupplier
        // 通过TaskInfo 创建 DatabaseBizOperation
        // 这行 DatabaseBizOperation 就完成工作了
        // 每一步需要编写日志

        ConfigurableApplicationContext context=SpringApplication.run(DataMovingApplication.class, args);
        JSONObject jsObject = new JSONObject();
        jsObject.put("operation","map");
        jsObject.put("source","1");
        jsObject.put("target","2");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("PACKET_ID","packetId");
        jsonObject2.put("Owner_Type","ownerType");
        jsonObject2.put("Owner_Code","ownerCode");
        jsonObject2.put("HAS_DATA_OPT","hasDataOpt");
        jsObject.put("fieldsMap",jsonObject2);
        TaskRun taskRun =context.getBean(TaskRun.class);
        taskRun.runTaks(taskLogId,jsObject);


    }
}
