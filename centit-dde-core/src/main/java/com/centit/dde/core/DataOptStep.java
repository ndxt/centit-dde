package com.centit.dde.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.utils.ConstantValue;

import java.util.*;

/**
 * @author zhf
 */
public class DataOptStep {
    private Map<String, JSONObject> nodeMap = new HashMap<>(50);
    private Map<String, List<JSONObject>> linkMap = new HashMap<>(100);
    private JSONObject currentStep;
    private int stepNo;

    public DataOptStep(Map<String, JSONObject> nodeMap, Map<String, List<JSONObject>> linkMap) {
        stepNo = 0;
        this.nodeMap.putAll(nodeMap);
        this.linkMap.putAll(linkMap);
    }

    public DataOptStep(JSONObject dataOptJson) {
        stepNo = 0;
        mapData(dataOptJson);
    }

    private void mapData(JSONObject dataOptJson) {
        //这个什么意思 所有的节点吗？
        JSONArray nodes = dataOptJson.getJSONArray("nodes");
        for (Object obj : nodes) {
            if (obj instanceof JSONObject) {
                JSONObject nodeJson = (JSONObject) obj;
                nodeMap.put(nodeJson.getString("id"), nodeJson);
            }
        }
        JSONArray links = dataOptJson.getJSONArray("edges");
        for (Object obj : links) {
            if (obj instanceof JSONObject) {
                JSONObject linkJson = (JSONObject) obj;
                linkJson = linkJson.getJSONObject("properties");
                String sourceId = linkJson.getString("sourceId");
                List<JSONObject> nextNodes = linkMap.get(sourceId);
                if (nextNodes != null) {
                    nextNodes.add(linkJson);
                } else {
                    List<JSONObject> jsonObjects = new ArrayList<>();
                    jsonObjects.add(linkJson);
                    linkMap.put(linkJson.getString("sourceId"),
                        jsonObjects);
                }
            }
        }
    }

    public void setStartStep() {
        for (Map.Entry<String, JSONObject> m : nodeMap.entrySet()) {
            if ("start".equals(m.getValue().getString("type"))) {
                currentStep = getOptStep(m.getKey());
                return;
            }
        }
        setEndStep();
    }

    public JSONObject getOptStep(String id) {
        return nodeMap.get(id);
    }

    public void setEndStep() {
        this.currentStep = getEmptyObject();
        stepNo++;
    }

    public List<JSONObject> getNextLinks(String id) {
        return linkMap.get(id);
    }

    public void seekToCycleEnd(String id) {
        HashSet<String> hasAddedNode = new HashSet<>();
        ArrayDeque<String> brachNode = new ArrayDeque<>();
        String curId = id;
        int cascade = 0;
        while (true) {
            List<JSONObject> nexts = linkMap.get(curId);
            if (nexts != null && nexts.size() > 0) {
                for (JSONObject n : nexts) {
                    String nId = n.getString("targetId");
                    if (hasAddedNode.contains(nId)) {
                        continue;
                    }
                    hasAddedNode.add(nId);
                    JSONObject nextNode =
                        nodeMap.get(n.getString("targetId"));
                    String stepType = nextNode.getString("type");
                    if (ConstantValue.CYCLE_FINISH.equals(stepType)) {
                        if (cascade == 0) {
                            setCurrentStep(nextNode);
                            return;
                        } else {
                            cascade--;
                        }
                    } else if (ConstantValue.CYCLE.equals(stepType)) {
                        cascade++;
                    }
                    brachNode.push(nId);
                }
            }
            if (brachNode.isEmpty()) {
                break;
            }
            curId = brachNode.pop();
        }
        setEndStep();
    }

    public JSONObject getCurrentStep() {
        return currentStep;
    }

    public void setNextStep() {
        if (!isEndStep()) {
            setCurrentStep(
                getNextStep(currentStep.getString("id")));
        }
    }

    public JSONObject getNextStep(String id) {
        List<JSONObject> links = getNextLinks(id);
        if (links == null || links.size() != 1) {
            return getEmptyObject();
        }
        return getOptStep(links.get(0).getString("targetId"));
    }

    private JSONObject getEmptyObject() {
        return new JSONObject();
    }

    public boolean isEndStep() {
        return currentStep.isEmpty();
    }

    public void setCurrentStep(JSONObject currentStep) {
        this.currentStep = currentStep;
        stepNo++;
    }

    public int getStepNo() {
        return stepNo;
    }

    public Map<String, JSONObject> getNodeMap() {
        return this.nodeMap;
    }

    public Map<String, List<JSONObject>> getLinkMap() {
        return this.linkMap;
    }
}

