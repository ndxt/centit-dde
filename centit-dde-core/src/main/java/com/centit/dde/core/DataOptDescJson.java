package com.centit.dde.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.utils.ConstantValue;

import java.util.*;

/**
 * @author zhf
 */

public class DataOptDescJson {
    private Map<String, JSONObject> nodeMap=new HashMap<>(50);
    private Map<String, List<JSONObject>> linkMap=new HashMap<>(100);
    private JSONObject currentStep;
    private int stepNo;
    private String runType;

    public DataOptDescJson(JSONObject dataOptJson) {
        stepNo = 0;
        this.runType = ConstantValue.RUN_TYPE_NORMAL;
        mapData(dataOptJson);
    }

    private void mapData(JSONObject dataOptJson) {
        JSONArray nodes = dataOptJson.getJSONArray("nodeList");
        for (Object obj : nodes) {
            if (obj instanceof JSONObject) {
                JSONObject nodeJson = (JSONObject) obj;
                nodeMap.put(nodeJson.getString("id"), nodeJson);
            }
        }
        JSONArray links = dataOptJson.getJSONArray("linkList");
        for (Object obj : links) {
            if (obj instanceof JSONObject) {
                JSONObject linkJson = (JSONObject) obj;
                String sourceId = linkJson.getString("sourceId");
                List<JSONObject> nextNodes = linkMap.get(sourceId);
                if (nextNodes != null) {
                    nextNodes.add(linkJson);
                } else {
                    List<JSONObject> jsonObjects= new ArrayList<>();
                    jsonObjects.add(linkJson);
                    linkMap.put(linkJson.getString("sourceId"),
                        jsonObjects);
                }
            }
        }
    }

    public JSONObject getStartStep() {
        for (Map.Entry<String, JSONObject> m : nodeMap.entrySet()) {
            if ("start".equals(m.getValue().getString("type"))) {
                return getOptStep(m.getKey());
            }
        }
        return null;
    }

    public JSONObject getOptStep(String id) {
        return nodeMap.get(id);
    }

    public JSONObject getNextStep(String id) {
        List<JSONObject> links = getNextLinks(id);
        if (links == null||links.size()!=1) {
            return null;
            //throw new ObjectException("不是有且只有一个后续节点");
        }
        return getOptStep(links.get(0).getString("targetId"));
    }

    public List<JSONObject> getNextLinks(String id) {
        return linkMap.get(id);
    }

    public JSONObject seekToCycleEnd(String id) {
        HashSet<String> hasAddedNode = new HashSet<>();
        ArrayDeque<String> brachNode = new ArrayDeque<>();
        String curId = id;
        int cascade = 0;
        while(true){
            List<JSONObject> nexts = linkMap.get(curId);
            if(nexts != null && nexts.size() > 0){
                for(JSONObject n : nexts){
                    String nId = n.getString("targetId");
                    if(hasAddedNode.contains(nId)){
                        continue;
                    }
                    hasAddedNode.add(nId);
                    JSONObject nextNode =
                        nodeMap.get(n.getString("targetId"));
                    String stepType = nextNode.getString("type");
                    if(ConstantValue.CYCLE_FINISH.equals(stepType)) {
                        if (cascade == 0) {
                            return nextNode;
                        } else {
                            cascade -- ;
                        }
                    } else if(ConstantValue.CYCLE.equals(stepType)) {
                        cascade ++;
                    }
                    brachNode.push(nId);
                }
            }
            if(brachNode.isEmpty()){
                break;
            }
            curId = brachNode.pop();
        }
        return null;
    }

    public JSONObject getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(JSONObject currentStep) {
        this.currentStep = currentStep;
        stepNo ++;
    }

    public int getStepNo() {
        return stepNo;
    }

    public String getRunType() {
        return runType;
    }

    public void setRunType(String runType) {
        this.runType = runType;
    }
}

