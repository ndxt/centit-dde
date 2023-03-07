package com.centit.dde.core;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.utils.ConstantValue;
import com.centit.support.common.LeftRightPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author zhf
 */
public class DataOptStep {

    protected static final Logger logger = LoggerFactory.getLogger(DataOptStep.class);

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
        this.currentStep = createEmptyObject();
        stepNo++;
    }

    public List<JSONObject> getNextLinks(String id) {
        return linkMap.get(id);
    }

    public void seekToCycleEnd(String id) {
        HashSet<String> hasAddedNode = new HashSet<>();
        ArrayDeque<LeftRightPair<String, Integer>> brachNode = new ArrayDeque<>();
        hasAddedNode.add(id);
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
                            brachNode.push(new LeftRightPair<>(nId, cascade-1));;
                        }
                    } else if (ConstantValue.CYCLE.equals(stepType)) {
                        brachNode.push(new LeftRightPair<>(nId, cascade+1));;
                    } else {
                        brachNode.push(new LeftRightPair<>(nId, cascade));
                    }
                }
            }
            if (brachNode.isEmpty()) {
                break;
            }
            LeftRightPair<String, Integer> branch = brachNode.pop();
            curId = branch.getLeft();
            cascade = branch.getRight();
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
            logger.error("当前节点("+ id +")没有有效的后续节点或者有多个后续节点。" );
            return createEmptyObject();
        }
        return getOptStep(links.get(0).getString("targetId"));
    }

    private JSONObject createEmptyObject() {
        return new JSONObject();
    }

    public boolean isEndStep() {
        if(currentStep==null){
            return true;
        }
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

