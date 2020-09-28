package com.centit.dde.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author zhf
 */
@Data
public class StepGraph {
    ArrayList<StepVertex> vertexs = new ArrayList<StepVertex>();
    ArrayList<StepEdge> edges = new ArrayList<StepEdge>();

    public void addVertex(StepVertex vertex) {
        vertexs.add(vertex);
    }

    public void addEdge(StepEdge edge) {
        edges.add(edge);
    }

    static void BFS(StepGraph graph) {
        ArrayList<StepVertex> vertexs = graph.vertexs;
        ArrayList<StepEdge> edges = graph.edges;
        //创建队列
        Queue<StepVertex> queue = new LinkedList<StepVertex>();
//顶节点放入队列
        queue.add(vertexs.get(0));
        //顶节点设为已阅
        vertexs.get(0).visited = true;
        System.out.print(vertexs.get(0));

        while (!queue.isEmpty()) {
            StepVertex vertex = queue.remove();
            for (StepEdge edge : edges) {
                if (edge.start.equals(vertex) && edge.end.visited == false) {
                    queue.add(edge.end);
                    edge.end.visited = true;
                    System.out.print(edge.end);
                }
            }
        }

    }

    /**
     * 深度优先 递归
     * 参数：图、点信息
     */
    static void DFS(StepGraph graph, StepVertex vertex) {
        System.out.print(vertex);
        vertex.visited2 = true;

        for (StepEdge edge : graph.edges) {
            if (edge.start.equals(vertex) && edge.end.visited2 == false) {
                DFS(graph, edge.end);
            }
        }
    }
}

