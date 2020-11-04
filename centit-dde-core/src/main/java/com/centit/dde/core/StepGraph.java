package com.centit.dde.core;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.bizopt.BuiltInOperation;
import com.centit.support.algorithm.StringBaseOpt;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author zhf
 */
@Data
public class StepGraph {
    ArrayList<JSONObject> vertexs = new ArrayList<JSONObject>();
    ArrayList<JSONObject> edges = new ArrayList<JSONObject>();

    public void addVertex(JSONObject vertex) {
        vertexs.add(vertex);
    }

    public void addEdge(JSONObject edge) {
        edges.add(edge);
    }

//    static void BFS(StepGraph graph) {
//        ArrayList<StepVertex> vertexs = graph.vertexs;
//        ArrayList<StepEdge> edges = graph.edges;
//        //创建队列
//        Queue<StepVertex> queue = new LinkedList<StepVertex>();
////顶节点放入队列
//        queue.add(vertexs.get(0));
//        //顶节点设为已阅
//        vertexs.get(0).visited = true;
//        System.out.print(vertexs.get(0));
//
//        while (!queue.isEmpty()) {
//            StepVertex vertex = queue.remove();
//            for (StepEdge edge : edges) {
//                if (edge.start.equals(vertex) && edge.end.visited == false) {
//                    queue.add(edge.end);
//                    edge.end.visited = true;
//                    System.out.print(edge.end);
//                }
//            }
//        }
//
//    }

    /**
     * 深度优先 递归
     * 参数：图、点信息
     */
    public void DFS(String id) {
        System.out.print(id);
        if (StringBaseOpt.isNvl(id)) return;
        for (JSONObject edge : edges) {
            String start = BuiltInOperation.getJsonFieldString(edge, "sourceId", "");
            String end = BuiltInOperation.getJsonFieldString(edge, "targetId", "");
            if (start.equals(id)) {
                for (Object o : vertexs) {
                    if (((JSONObject) o).get("id").equals(id)) {
                        //runStep
                    }
                }
                if (!"".equals(end)) {
                    DFS(end);
                }
            }
        }
    }
}

