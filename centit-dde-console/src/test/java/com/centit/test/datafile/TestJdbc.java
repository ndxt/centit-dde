package com.centit.test.datafile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.StepGraph;

import java.sql.*;


public class TestJdbc {

    public static void main(String[] args) {
String s="{\"applicationId\":\"5fJnPolOQD6tbRmth_jFpQ\",\"bufferFreshPeriod\":-1,\"dataOptDescJson\":{\"nodeList\":[{\"type\":\"map\",\"nodeName\":\"字段映射\",\"step\":\"\",\"ico\":\"el-icon-position\",\"tabtype\":\"Dmanipulation\",\"dataSetId\":\"\",\"dataSetName\":\"\",\"source\":\"operation\",\"dataSetTitle\":\"\",\"config\":[],\"id\":\"map-ae82e988aa4d47fb8fc4916a01574d43\",\"height\":50,\"x\":3885,\"width\":120,\"y\":3195},{\"id\":\"obtain-database-1515fdd883044b6a85aafa07a9de0d5e\",\"dBType\":\"MySql\",\"type\":\"obtain-database\",\"databaseName\":\"\",\"tabtype\":\"Dmanipulation\",\"nodeName\":\"数据库\",\"setType\":\"\",\"ico\":\"el-icon-coin\",\"createTime\":\"2020-08-25 13:54:11\",\"dec\":\"\",\"source\":\"obtain\",\"config\":{\"databaseCode\":\"\",\"created\":\"\",\"queryDesc\":\"\",\"queryName\":\"数据库\",\"querySQL\":\"\",\"databaseName\":\"\"},\"height\":50,\"x\":3885,\"width\":120,\"y\":3060}],\"linkList\":[{\"type\":\"link\",\"id\":\"link-3ff1884274e747d8be5253edb0acf5e7\",\"sourceId\":\"obtain-database-1515fdd883044b6a85aafa07a9de0d5e\",\"targetId\":\"map-ae82e988aa4d47fb8fc4916a01574d43\",\"label\":\"\",\"cls\":{\"linkType\":\"Flowchart\",\"linkColor\":\"#2a2929\",\"linkThickness\":2}}],\"attr\":{\"isWhile\":true,\"taskType\":1,\"dec\":\"\",\"isValid\":false,\"id\":\"flow-e8c8f4f2c0074ef68898870ab038ff62\",\"interfaceName\":\"HttP：//\",\"packName\":\"\",\"packetName\":\"测试\",\"key\":\"and\"},\"config\":{\"showGridIcon\":\"eye\",\"showGrid\":true,\"showGridText\":\"隐藏网格\"},\"status\":\"2\",\"remarks\":[]},\"dataSetDefines\":[],\"isValid\":\"\",\"isWhile\":\"\",\"lastRunTime\":\"2020-10-15 10:12:14\",\"nextRunTime\":\"2020-10-15 10:12:14\",\"packetId\":\"232916f7caf54d92adb6993b8d34bae5\",\"packetName\":\"\",\"packetParams\":[],\"recorder\":\"\",\"taskType\":\"\"}";
        JSONObject ss=JSONObject.parseObject(s).getJSONObject("dataOptDescJson");
        JSONArray node=ss.getJSONArray("nodeList");
        JSONArray link=ss.getJSONArray("linkList");
        StepGraph stepGraph=new StepGraph();
        String startId="";
        for (Object o:node) {
           stepGraph.addVertex((JSONObject) o);
           if(((JSONObject) o).get("source").equals("obtain")){
               startId=((JSONObject) o).getString("id");
           }
        }
        link.stream().map(o -> (JSONObject) o).forEach(stepGraph::addEdge);

        stepGraph.DFS(startId);
        System.out.println(s);
    }
}

