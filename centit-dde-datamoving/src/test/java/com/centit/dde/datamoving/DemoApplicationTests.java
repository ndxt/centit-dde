package com.centit.dde.datamoving;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.datamoving.service.TaskRun;
import com.centit.product.dataopt.core.BizModel;
import com.centit.support.database.utils.DatabaseAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.sql.SQLException;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class DemoApplicationTests {

    //@Test
    public static void main(String[] args) throws SQLException {
        if(args==null || args.length<1){
            return;
        }
        String taskLogId = args[0];
        // taskLogId = "1";

        System.out.println(taskLogId);
        ConfigurableApplicationContext context= SpringApplication.run(DataMovingApplication.class, args);
        JSONObject jsObject = new JSONObject();
        jsObject.put("source","test");
        jsObject.put("target","test");
        jsObject.put("operation","map");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("PACKET_ID", "strcat("+ DatabaseAccess.mapColumnNameToField("PACKET_ID")+",\'test\')");
        jsonObject2.put("Owner_Type",DatabaseAccess.mapColumnNameToField("Owner_Type"));
        jsonObject2.put("Owner_Code",DatabaseAccess.mapColumnNameToField("Owner_Code"));
        jsonObject2.put("HAS_DATA_OPT",DatabaseAccess.mapColumnNameToField("HAS_DATA_OPT"));
        jsObject.put("fieldsMap",jsonObject2);
        jsObject.put("operation","filter");
        jsObject.put("filter","ownerType='D'");
        jsObject.put("operation","append");
        /*JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("sum","ownerType+' and '+ownerCode");
        jsObject.put("fieldsMap",jsonObject2);
        jsObject.put("operation","stat");
        jsObject.put("groupBy",new String[]{"ownerType","hasDataOpt"});
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("owner","hasDataOpt:sum");
        jsObject.put("fieldsMap",jsonObject2);
        jsObject.put("operation","analyse");
        jsObject.put("groupBy","hasDataOpt");
        jsObject.put("orderBy","hasDataOpt");
        JSONObject jsonObject2 = new JSONObject();*/

        jsonObject2.put("owner","(toNumber(hasDataOpt) - toNumber(hasDataOpt._1) / toNumber(hasDataOpt.1)) * 100 + '%'");
        jsObject.put("fieldsMap",jsonObject2);
        jsObject.put("operation","cross");
        jsObject.put("rowHeader","ownerType");
        jsObject.put("colHeader","hasDataOpt");
        jsObject.put("operation","compare");
        jsObject.put("source2","2");
        jsObject.put("primaryKey","packetId");
        jsObject.put("operation","join");
        jsObject.put("source2","2");
        jsObject.put("primaryKey","packetId");
        jsObject.put("operation","filterExt");
        jsObject.put("source2","2");
        jsObject.put("primaryKey","packetId");
        jsObject.put("filter","ownerType='D'");
        jsObject.put("operation","check");
        JSONArray jsonArray = new JSONArray();
        /*JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("checkType","length");
        jsonObject2.put("checkParams",new String[]{"ownerType","1","10"});
        jsonObject2.put("errorMsg","");
        jsonArray.add(jsonObject2);
        jsObject.put("rules",jsonArray);
        jsObject.put("operation","static");
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("dataSetName","test");
        jsonObject2.put("data","1");*/
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("dataSetName","test2");
        jsonObject3.put("data","2");
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);
        jsObject.put("data",jsonArray);

        jsObject.put("operation","persistence");
        jsObject.put("databaseCode","0000000063");
        jsObject.put("tableName","q_data_packet3");
        TaskRun taskRun =context.getBean(TaskRun.class);
        BizModel bizModel= taskRun.runStep(jsObject);
        //"082d83be953e4073b0f9995f5b49bfb1");
            //taskRun.runTask(taskLogId,null);
        System.out.println(bizModel.getModelName());
        //SpringApplication.exit(context);
    }

}
