package com.centit.dde.routemeta;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDao;
import com.centit.support.common.CachedObject;
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
public class RouteMetadataServiceImpl implements RouteMetadataService{
    protected static final Logger logger = LoggerFactory.getLogger(RouteMetadataServiceImpl.class);
    private final CachedObject<ApiRouteTree> optTreeNodeCache;

    @Autowired
    private DataPacketDao dataPacketDao;

    public static List<String> praiseUrl(String uri) {
        String[] uriPieces = uri.split("/");
        List<String> path = new ArrayList<>();
        try {
            for (String uriPiece : uriPieces) {
                path.add(URLDecoder.decode(uriPiece, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            return path;
        }
        return path;
    }

    public RouteMetadataServiceImpl() {
        optTreeNodeCache = new CachedObject<>(this::buildMetadataTree,
            ICachedObject.DEFAULT_REFRESH_PERIOD);
    }

    private ApiRouteTree buildMetadataTree(){
        final ApiRouteTree routeTree = new ApiRouteTree();
        JSONArray apiList = dataPacketDao.listApiWithRoute();
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
                List<String> pieces = praiseUrl(routeUrl);
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
    public void rebuildMetadataTree(){
        optTreeNodeCache.evictCache();
    }

    @Override
    public Pair<String, List<String>> mapUrlToPacketId(String url, String method) {
        List<String> pieces = praiseUrl(url);
        ApiRouteTree routeNode = optTreeNodeCache.getCachedTarget();
        ApiRouteTree packetNode = routeNode.getChildNode(method);
        List<String> params = new ArrayList<>();
        for(String piece: pieces){
            if(packetNode==null) return null;
            ApiRouteTree nextNode = packetNode.getChildNode(piece);
            if(nextNode == null) {
                nextNode = packetNode.getChildNode("*");
                params.add(piece);
            }
            packetNode = nextNode;
        }
        return packetNode==null ? null:
            new ImmutablePair<>(packetNode.packetId, params);
    }

    @Override
    public boolean isExistUrl(String url, String method){
        List<String> pieces = praiseUrl(url);
        ApiRouteTree routeNode = optTreeNodeCache.getCachedTarget();
        ApiRouteTree packetNode = routeNode.getChildNode(method);
        for(String piece: pieces){
            if(packetNode==null) return false;
            ApiRouteTree nextNode = packetNode.getChildNode(piece);
            if(nextNode == null) {
                nextNode = packetNode.getChildNode("*");
            }
            packetNode = nextNode;
        }
        return packetNode!=null;
    }
}
