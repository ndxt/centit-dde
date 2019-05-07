package com.centit.dde.datamoving;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.product.dataopt.core.BizModel;
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
        jsObject.put("source","1");
        jsObject.put("target","2");
        /*jsObject.put("operation","map");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("PACKET_ID", DatabaseAccess.mapColumnNameToField("PACKET_ID"));
        jsonObject2.put("Owner_Type",DatabaseAccess.mapColumnNameToField("Owner_Type"));
        jsonObject2.put("Owner_Code",DatabaseAccess.mapColumnNameToField("Owner_Code"));
        jsonObject2.put("HAS_DATA_OPT",DatabaseAccess.mapColumnNameToField("HAS_DATA_OPT"));
        jsObject.put("fieldsMap",jsonObject2);*/
        /*jsObject.put("operation","filter");
        jsObject.put("filter","ownerType='D'");*/
        /*jsObject.put("operation","append");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("sum","ownerType+' and '+ownerCode");
        jsObject.put("fieldsMap",jsonObject2);*/
//        jsObject.put("operation","stat");
//        jsObject.put("groupBy",new String[]{"ownerType","hasDataOpt"});
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject2.put("owner","hasDataOpt:sum");
//        jsObject.put("fieldsMap",jsonObject2);
//        jsObject.put("operation","analyse");
//        jsObject.put("groupBy","hasDataOpt");
//        jsObject.put("orderBy","hasDataOpt");
//        JSONObject jsonObject2 = new JSONObject();
        //分析函数 :1 表示在同组中向前找，:_1表示向后找还没有实现
//        jsonObject2.put("owner","(toNumber(hasDataOpt) - 1 / toNumber(hasDataOpt)) * 100 + '%'");
//        jsObject.put("fieldsMap",jsonObject2);
//        jsObject.put("operation","cross");
//        jsObject.put("rowHeader","ownerType");
//        jsObject.put("colHeader","hasDataOpt");
//        jsObject.put("operation","compare");
//        jsObject.put("source2","2");
//        jsObject.put("primaryKey","packetId");
//        jsObject.put("operation","join");
//        jsObject.put("source2","2");
//        jsObject.put("primaryKey","packetId");
//        jsObject.put("operation","filterExt");
//        jsObject.put("source2","2");
//        jsObject.put("primaryKey","packetId");
//        jsObject.put("filter","ownerType='D'");
//        jsObject.put("operation","check");
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonObject2 = new JSONObject();
//        jsonObject2.put("checkType","length");
//        jsonObject2.put("checkParams",new String[]{"ownerType","1","10"});
//        jsonObject2.put("errorMsg","");
//        jsonArray.add(jsonObject2);
//        jsObject.put("rules",jsonArray);
        jsObject.put("operation","static");
        JSONArray jsonArray = new JSONArray();
        Map jsonObject2 = new HashMap();
        jsonObject2.put("dataSetName","test");
        jsonObject2.put("data",new String[]{"ownerType","1","10"});
        jsonArray.add(jsonObject2);
        jsObject.put("data",jsonArray);
        TaskRun taskRun =context.getBean(TaskRun.class);
        BizModel bizModel=taskRun.runTaks(taskLogId,jsObject);
        System.out.println(bizModel.getModelName());

    }
}
