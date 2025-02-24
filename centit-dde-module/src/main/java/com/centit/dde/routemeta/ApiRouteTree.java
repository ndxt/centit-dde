package com.centit.dde.routemeta;

import java.util.HashMap;
import java.util.Map;

public class ApiRouteTree {
    public String packetId;
    public Map<String, ApiRouteTree> childList;

    public ApiRouteTree(){
        packetId = null;
        childList = null;
    }

    public ApiRouteTree getChildNode(String urlPiece){
        if(childList == null) return null;
        return childList.get(urlPiece);
    }

    public ApiRouteTree fetchChildNode(String urlPiece){
        if(childList == null){
            childList = new HashMap<>();
        }
        return childList.computeIfAbsent(urlPiece, k -> new ApiRouteTree());
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }
}
