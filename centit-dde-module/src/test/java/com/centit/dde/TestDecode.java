package com.centit.dde;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.support.file.FileIOOpt;
import com.centit.support.security.SecurityOptUtils;

import java.io.IOException;

public class TestDecode {
    public static void main(String[] args) throws IOException {
        String packetJsonStr = FileIOOpt.readStringFromFile("/Users/codefan/Downloads/a.txt", "UTF-8");
        DataPacketDraft dataPacketDraft = JSONObject.parseObject(
            SecurityOptUtils.decodeSecurityString(packetJsonStr),
            DataPacketDraft.class);
        System.out.println(JSON.toJSONString(dataPacketDraft));
    }
}
