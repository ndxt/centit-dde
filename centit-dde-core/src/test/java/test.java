import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {

        ArrayDeque<Integer> intDeque = new ArrayDeque<>();
        intDeque.push(1);
        intDeque.push(2);
        intDeque.push(3);
        System.out.println(intDeque.pop());
        intDeque.push(4);
        System.out.println(intDeque.pop());
    }
    public static void mainTest() {
       String s="{\"applicationId\":\"5fJnPolOQD6tbRmth_jFpQ\",\"bufferFreshPeriod\":-1,\"dataOptDescJson\":{\"setlist\":[{\"value\":\"字段映射8\"}],\"nodeList\":[{\"type\":\"map\",\"typeName\":\"字段映射\",\"nodeName\":\"字段映射\",\"step\":\"\",\"ico\":\"el-icon-position\",\"tabtype\":\"Dmanipulation\",\"dataSetId\":\"\",\"dataSetName\":\"字段映射8\",\"dataSets\":[],\"processName\":\"字段映射\",\"SetsName\":\"字段映射8\",\"SetsCname\":\"\",\"source\":\"\",\"dataSetTitle\":\"\",\"config\":[{\"dataType\":\"string\",\"isStatData\":\"F\",\"columnName\":\"45456\",\"cName\":\"454564\",\"paramValidateRegex\":\"45456\",\"index\":0}],\"id\":\"map-b61560f4d41d4b1a85ce9e3d53981a87\",\"height\":50,\"x\":3726,\"width\":120,\"y\":3030}],\"linkList\":[],\"attr\":{\"isWhile\":true,\"taskType\":1,\"dec\":\"\",\"isValid\":false,\"id\":\"flow-e8c8f4f2c0074ef68898870ab038ff62\",\"interfaceName\":\"HttP：//\",\"packName\":\"\",\"packetName\":\"测试\",\"key\":\"and\"},\"config\":{\"showGridIcon\":\"eye\",\"showGrid\":true,\"showGridText\":\"隐藏网格\"},\"status\":\"2\",\"remarks\":[]},\"dataSetDefines\":[],\"hasDataOpt\":\"F\",\"interfaceName\":\"http://\",\"isValid\":false,\"isWhile\":false,\"lastRunTime\":\"2020-10-15 10:12:14\",\"nextRunTime\":\"2020-10-15 10:12:14\",\"packetId\":\"232916f7caf54d92adb6993b8d34bae5\",\"packetName\":\"ceshi\",\"packetParams\":[{\"paramDefaultValue\":\"15\",\"paramDisplayStyle\":\"N\",\"paramLabel\":\"测试\",\"paramName\":\"ceshi\",\"paramOrder\":0,\"paramReferenceData\":\"dataSet\",\"paramReferenceType\":\"6\",\"paramType\":\"S\",\"paramValidateInfo\":\"暂无\",\"paramValidateRegex\":\"c<a\",\"index\":0}],\"recorder\":\"\",\"taskCron\":\"****j**\",\"taskType\":2}";
        JSONObject jsonObject= (JSONObject) JSONObject.parse(s);
        JSONArray ss=jsonObject.getJSONObject("dataOptDescJson").getJSONArray("nodeList").getJSONObject(0).getJSONArray("config");
        Map<String,String> t=new HashMap<>();
        for (Object o : ss) {
           JSONObject temp=(JSONObject)o;
            t.put(temp.getString("columnName"),temp.getString("paramValidateRegex"));
        }
        System.out.println(t);
    }
}
