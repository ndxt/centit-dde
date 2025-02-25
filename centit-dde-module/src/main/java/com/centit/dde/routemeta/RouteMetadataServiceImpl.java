package com.centit.dde.routemeta;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.support.common.CachedMap;
import com.centit.support.common.ICachedObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RouteMetadataServiceImpl implements RouteMetadataService {
    protected static final Logger logger = LoggerFactory.getLogger(RouteMetadataServiceImpl.class);
    private final CachedMap<String, ApiRouteTree> optTreeNodeCache;

    @Autowired
    private DataPacketDao dataPacketDao;

    public static List<String> parseUrl(String uri) {
        String[] uriPieces = uri.split("/");
        List<String> path = new ArrayList<>();
        try {
            int i = 0, n = uriPieces.length;
            //过滤掉api前面的url，这些可能因为服务的配置不同而不同
            while (i < n && !"api".equals(uriPieces[i])) {
                i++;
            }
            i++;
            while (i < n){
                String piece = URLDecoder.decode(uriPieces[i], "UTF-8");
                if(StringUtils.isNotBlank(piece)) {
                    path.add(piece);
                }
                i++;
            }
        } catch (UnsupportedEncodingException e) {
            return path;
        }
        return path;
    }

    public RouteMetadataServiceImpl() {
        optTreeNodeCache = new CachedMap<>(this::buildMetadataTree,
            ICachedObject.DEFAULT_REFRESH_PERIOD);
    }

    private ApiRouteTree buildMetadataTree(String topUnit){
        final ApiRouteTree routeTree = new ApiRouteTree();
        JSONArray apiList = dataPacketDao.listApiWithRoute(topUnit);
        for (Object obj : apiList){
            if(obj instanceof JSONObject){
                JSONObject apiJson = (JSONObject) obj;
                //任务类型 1：GET请求，2：表示定时任务,3：POST请求,4：消息触发 5：PUT请求 6：DELETE请求 7：子模块
                String method = apiJson.getString("taskType");
                String routeUrl = apiJson.getString("routeUrl");
                String packetId = apiJson.getString("packetId");
                if("1".equals(method)){
                    method = "GET";
                } else if("3".equals(method)){
                    method = "POST";
                } else if("5".equals(method)){
                    method = "PUT";
                } else if("6".equals(method)){
                    method = "DELETE";
                } else {
                  continue;
                }
                if(StringUtils.isBlank(routeUrl) || StringUtils.isBlank(packetId)) {
                    continue;
                }
                List<String> pieces = parseUrl(routeUrl);
                ApiRouteTree packetNode = routeTree.fetchChildNode(method);
                for(String piece: pieces){
                    packetNode = packetNode.fetchChildNode(piece);
                }
                if(StringUtils.isBlank(packetNode.getPacketId())){
                    packetNode.setPacketId(packetId);
                } else {
                    logger.error("API route url 冲突， url：{} packetId：{} - {}", routeUrl, packetId, packetNode.getPacketId());
                }
            }
        }
        return routeTree;
    }

    @Override
    public void rebuildMetadataTree(String topUnit){
        optTreeNodeCache.evictIdentifiedCache(topUnit);
    }

    @Override
    public Pair<String, List<String>> mapUrlToPacketId(String url, String method) {
        List<String> pieces = parseUrl(url);
        if(pieces==null || pieces.isEmpty()) return null;
        ApiRouteTree routeNode = optTreeNodeCache.getCachedValue(pieces.get(0));
        ApiRouteTree packetNode = routeNode.getChildNode(method);
        List<String> params = new ArrayList<>();
        for(int i=1; i<pieces.size(); i++){
            if(packetNode==null) return null;
            ApiRouteTree nextNode = packetNode.getChildNode(pieces.get(i));
            if(nextNode == null) {
                nextNode = packetNode.getChildNode("*");
                params.add(pieces.get(i));
            }
            packetNode = nextNode;
        }
        return packetNode==null ? null:
            new ImmutablePair<>(packetNode.packetId, params);
    }

    @Override
    public String getPublishPacketId(String topUnit, String url, String method){
        List<String> pieces = parseUrl(url);
        ApiRouteTree routeNode = optTreeNodeCache.getCachedValue(topUnit);
        ApiRouteTree packetNode = routeNode.getChildNode(method);
        for(String piece: pieces){
            if(packetNode==null) return null;
            ApiRouteTree nextNode = packetNode.getChildNode(piece);
            if(nextNode == null) {
                nextNode = packetNode.getChildNode("*");
            }
            packetNode = nextNode;
        }
        return packetNode==null?null:packetNode.getPacketId();
    }
}
