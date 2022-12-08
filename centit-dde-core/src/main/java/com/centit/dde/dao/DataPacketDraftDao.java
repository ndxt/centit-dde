package com.centit.dde.dao;

import com.centit.dde.po.DataPacketDraft;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhf
 */
@Repository
public class DataPacketDraftDao extends BaseDaoImpl<DataPacketDraft, String> {
    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("packetName","(packet_name=:packetName or packet_id=:packetName)");
        filterField.put("osId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optId",CodeBook.EQUAL_HQL_ID);
        filterField.put("isDisable",CodeBook.EQUAL_HQL_ID);
        filterField.put("taskType",CodeBook.EQUAL_HQL_ID);
        filterField.put("packetId",CodeBook.LIKE_HQL_ID);
        return filterField;
    }

}
