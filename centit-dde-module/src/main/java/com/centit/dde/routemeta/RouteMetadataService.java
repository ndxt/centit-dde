package com.centit.dde.routemeta;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface RouteMetadataService {
    void rebuildMetadataTree();

    boolean isExistUrl(String url, String method);

    Pair<String, List<String>> mapUrlToPacketId(String url, String method);
}
