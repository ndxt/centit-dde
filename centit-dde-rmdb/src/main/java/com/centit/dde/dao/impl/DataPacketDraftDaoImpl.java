package com.centit.dde.dao.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.dao.DataPacketDraftDao;
import com.centit.dde.adapter.po.DataPacketDraft;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.database.utils.PageDesc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 */
@Repository("dataPacketDraftDao")
public class DataPacketDraftDaoImpl extends BaseDaoImpl<DataPacketDraft, String> implements DataPacketDraftDao{
    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();
        filterField.put("(like)packetName", "(packet_name like :packetName or packet_id like :packetName)");
        filterField.put("osId", CodeBook.EQUAL_HQL_ID);
        filterField.put("optId",CodeBook.EQUAL_HQL_ID);
        filterField.put("isDisable",CodeBook.EQUAL_HQL_ID);
        filterField.put("taskType",CodeBook.EQUAL_HQL_ID);
        filterField.put("(like)packetId",CodeBook.LIKE_HQL_ID);
        return filterField;
    }

    public JSONArray listDataPacketDraft(Map<String, Object> params, PageDesc pageDesc){
        String orderBySql = this.fetchSelfOrderSql(params);
        if(StringUtils.isBlank(orderBySql)){
            orderBySql = "a.update_date desc";
        }else {
            if(orderBySql.indexOf('.')<0)
                orderBySql = "a." + orderBySql;
        }

        String sqlSen = "select a.packet_id, a.packet_name, a.packet_desc, a.owner_type, a.owner_code, " +
            "a.recorder, a.record_date, a.update_date, a.os_id, a.opt_id, " +
            "a.task_type, a.task_cron, a.is_valid, a.publish_date, a.opt_code," +
            "a.log_level, a.is_disable, b.last_run_time, b.next_run_time " +
            " from q_data_packet_draft a left join q_data_packet b on a.packet_id=b.packet_id" +
            " where 1=1 [:(like)packetName | and (a.packet_name like :packetName or a.packet_id like :packetName) ]" +
            " [:osId| and a.os_id=:osId]"+
            " [:optId| and a.opt_id=:optId]"+
            " [:isDisable| and a.is_disable=:isDisable]"+
            " [:taskType| and a.task_type=:taskType]"+
            " [:(like)packetId| and a.packet_id like :packetId]" +
            " order by " + orderBySql;

        JSONArray jsonArray = DatabaseOptUtils.listObjectsByParamsDriverSqlAsJson(this,
            sqlSen, params, pageDesc);
        if(jsonArray == null){
            return null;
        }
        for(Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.put("isValid", BooleanBaseOpt.castObjectToBoolean(jsonObject.get("isValid"), true));
            jsonObject.put("isDisable", BooleanBaseOpt.castObjectToBoolean(jsonObject.get("isDisable"), false));
        }
        return jsonArray;
    }

    @Override
    public List<DataPacketDraft> listNeedPublishDataPacket(String osId) {
        return this.listObjectsByFilter(" where os_id = ? and update_date > publish_date",
            new Object[]{osId});
    }

    @Override
    public void publishDataPacket(String optCode, DataPacketDraft dataPacketCopy) {
        String sql = "update q_data_packet_draft SET publish_date=?, update_date =?, opt_code=?  WHERE  PACKET_ID=? ";
        this.getJdbcTemplate().update(sql, new Object[]{dataPacketCopy.getPublishDate(), dataPacketCopy.getUpdateDate(),
            optCode, dataPacketCopy.getPacketId()});
    }

    @Override
    public int[] batchUpdateOptIdByApiId(String optId, List<String> apiIds) {
        String sql="UPDATE q_data_packet_draft SET OPT_ID=?, PUBLISH_DATE =?, IS_DISABLE='F' WHERE PACKET_ID = ? ";
        int[] dataPacket = this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, optId);
                ps.setTimestamp(2, DatetimeOpt.currentSqlTimeStamp());
                ps.setString(3, apiIds.get(i));
            }

            @Override
            public int getBatchSize() {
                return apiIds.size();
            }
        });
        return dataPacket;
    }

    @Override
    public void updateDisableStatus(String packetId, String disable) {
        String sql ="UPDATE q_data_packet_draft SET IS_DISABLE=? WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().update(sql, new Object[]{disable,packetId});
    }

    @Override
    public void batchDeleteByPacketIds(String[] packetIds) {
        String delSql ="DELETE FROM q_data_packet_draft WHERE PACKET_ID = ? ";
        this.getJdbcTemplate().batchUpdate(delSql,new BatchPreparedStatementSetter(){
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                ps.setString(1, packetIds[i]);
            }
            public int getBatchSize() {
                return packetIds.length;
            }
        });
    }

    @Override
    public int clearTrashStand(String osId) {
        String delSql ="DELETE FROM q_data_packet_draft WHERE IS_DISABLE = 'T' AND  OS_ID=? ";
        int delCount = DatabaseOptUtils.doExecuteSql(this, delSql, new Object[]{osId});
        return  delCount;
    }

    @Override
    public void updateDataPacketOptJson(String packetId, String dataPacketOptJson) {
        DatabaseOptUtils.batchUpdateObject(this, DataPacketDraft.class,
            CollectionsOpt.createHashMap("dataOptDescJson", dataPacketOptJson,
                    "updateDate", DatetimeOpt.truncateToSecond(DatetimeOpt.currentUtilDate())),
            CollectionsOpt.createHashMap("packetId", packetId)
        );
    }

}
